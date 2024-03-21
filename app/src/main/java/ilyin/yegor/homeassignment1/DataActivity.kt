package ilyin.yegor.homeassignment1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        val text_view = findViewById<TextView>(R.id.textView)
        val return_btn = findViewById<Button>(R.id.return_button)

        text_view.setText(intent.getStringExtra("Contact"))

        return_btn.setOnClickListener{
            finish()
        }
    }
}