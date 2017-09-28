package ru.belogurowdev.lab4

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class SecondActivity : AppCompatActivity() {

    private var mEditText: EditText? = null
    private var mButtonSendTextForPrevActivity: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        initViews()

        mButtonSendTextForPrevActivity!!.setOnClickListener {
            val prevIntent = Intent(this, MainActivity::class.java)
            prevIntent.putExtra("text", mEditText?.text.toString())
            setResult(RESULT_OK, prevIntent)
            finish()
        }

    }

    private fun initViews() {
        mButtonSendTextForPrevActivity = findViewById(R.id.button_send_text_for_prev_activity)
        mEditText = findViewById(R.id.edit)
    }
}
