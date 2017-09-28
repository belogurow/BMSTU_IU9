package ru.belogurowdev.lab4

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var mTextViewFromExplicitIntent: TextView? = null
    private var mButtonOpenExplicitIntent: Button? = null

    private var mButtonOpenMap: Button? = null
    private var mButtonOpenWeb: Button? = null
    private var mButtonOpenPhone: Button? = null
    private var mButtonOpenMail: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        mButtonOpenExplicitIntent?.setOnClickListener {
            val explicitIntent = Intent(this, SecondActivity::class.java)
            startActivityForResult(explicitIntent, SECOND_ACTIVITY_RESULT_CODE)
        }

        mButtonOpenWeb?.setOnClickListener {
            val webpage = Uri.parse("http://www.android.com")
            val webIntent = Intent(Intent.ACTION_VIEW, webpage)
            startActivity(webIntent)
        }

        mButtonOpenMap?.setOnClickListener {
            val location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California")
            val mapIntent = Intent(Intent.ACTION_VIEW, location)
            startActivity(mapIntent)
        }

        mButtonOpenPhone?.setOnClickListener {
            val number = Uri.parse("tel:555666")
            val callIntent = Intent(Intent.ACTION_DIAL, number)
            startActivity(callIntent)
        }

        mButtonOpenMail?.setOnClickListener {
            val mailIntent = Intent(android.content.Intent.ACTION_SEND)
            mailIntent.type = "plain/text"
            mailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf("to@email.com"))
            mailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject")
            mailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text")
            startActivity(mailIntent)
        }
    }

    private fun initViews() {
        mTextViewFromExplicitIntent = findViewById(R.id.text_from_explicit_intent)
        mButtonOpenExplicitIntent = findViewById(R.id.button_open_explicit_intent)
        mButtonOpenMap = findViewById(R.id.button_open_map)
        mButtonOpenPhone = findViewById(R.id.button_open_phone)
        mButtonOpenWeb = findViewById(R.id.button_open_web)
        mButtonOpenMail = findViewById(R.id.button_open_mail)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SECOND_ACTIVITY_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                val text = data.getStringExtra("text")
                mTextViewFromExplicitIntent?.text = text
            }
        }
    }

    companion object {

        private val SECOND_ACTIVITY_RESULT_CODE = 1
    }
}
