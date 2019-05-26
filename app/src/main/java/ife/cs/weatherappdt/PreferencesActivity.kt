package ife.cs.weatherappdt

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PreferencesActivity: AppCompatActivity() {

    private fun tryInflate(name: String, context: Context, attrs: AttributeSet): View? {
        val inflater = LayoutInflater.from(context)
        return try {
            inflater.createView(name, null, attrs)
        } catch (e: Exception) {
            try {
                inflater.createView("android.widget.$name", null, attrs)
            } catch (e1: Exception) {
                null
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
/*        layoutInflater.setFactory{ name: String, context: Context, attrs: AttributeSet ->
            val v = tryInflate(name, context, attrs)
            if (v is TextView) {
                v.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.textsize))
            }
            return@setFactory v
        }*/
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        supportFragmentManager.beginTransaction()
            .replace(R.id.preferences_container, PreferencesFragment())
            .commit()
    }
}