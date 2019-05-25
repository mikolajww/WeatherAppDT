package ife.cs.weatherappdt

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.text.Spanned
import android.widget.EditText
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference

class PreferencesFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        findPreference<EditTextPreference>("latitude")!!.setOnBindEditTextListener { NumericEditTextListener() }
        findPreference<EditTextPreference>("longitude")!!.setOnBindEditTextListener { NumericEditTextListener() }

        with(findPreference<SeekBarPreference>("refresh_time")!!) {
            max = 100
            min = 1
            summary = "$value + minutes"
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
                preference.summary = "$newValue minutes"
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