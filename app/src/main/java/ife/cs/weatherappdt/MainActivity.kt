package ife.cs.weatherappdt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swiperefresh.setOnRefreshListener {
            textView.setText("${++counter}")
            swiperefresh.setRefreshing(false)
        }
    }
}
