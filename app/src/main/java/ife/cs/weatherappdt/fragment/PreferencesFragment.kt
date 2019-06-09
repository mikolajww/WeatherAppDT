package ife.cs.weatherappdt.fragment

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.Spanned
import android.widget.EditText
import android.widget.Toast
import androidx.preference.*
import ife.cs.weatherappdt.R
import ife.cs.weatherappdt.api.OpenWeatherApiService
import java.lang.RuntimeException

class PreferencesFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        findPreference<EditTextPreference>("latitude")!!.onBindEditTextListener = NumericEditTextListener()
        findPreference<EditTextPreference>("longitude")!!.onBindEditTextListener = NumericEditTextListener()

        with(findPreference<SeekBarPreference>("refresh_time")!!) {
            max = 100
            min = 1
            summary = "$value minutes"
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                preference.summary = "$newValue minutes"
                true
            }
        }
        with(findPreference<ListPreference>("unit")!!) {
            summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                OpenWeatherApiService.unit = when(newValue as String) {
                    "Kelvin" ->  OpenWeatherApiService.Units.K
                    "Celsius" -> OpenWeatherApiService.Units.C
                    "Fahrenheit" -> OpenWeatherApiService.Units.F
                    else -> throw RuntimeException("Unit not supported!")
                }
                true
            }
        }
    }

    private inner class NumericEditTextListener : EditTextPreference.OnBindEditTextListener {
        override fun onBindEditText(editText: EditText) {
            editText.inputType = (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED)
            editText.filters = arrayOf<InputFilter>(CustomRangeInputFilter(-180.0, 180.0, activity as Context))
        }
    }

    private inner class CustomRangeInputFilter(
        private val minValue: Double,
        private val maxValue: Double,
        private val context: Context
    ) :
        InputFilter {

        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dStart: Int,
            dEnd: Int
        ): CharSequence? {
            try {
                var newVal =
                    dest.toString().substring(0, dStart) + dest.toString().substring(dEnd, dest.toString().length)
                newVal = newVal.substring(0, dStart) + source.toString() + newVal.substring(dStart, newVal.length)
                val input = newVal.toDouble()

                if (isInRange(minValue, maxValue, input)) {
                    return null
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }

            Toast.makeText(context, "This value is invalid!", Toast.LENGTH_SHORT).show()
            return ""
        }

        private fun isInRange(a: Double, b: Double, c: Double): Boolean {
            return if (b > a) c > a && c < b else c > b && c < a
        }
    }
}