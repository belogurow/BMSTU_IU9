package ru.belogurowdev.lab9

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var buttonOpenFileManager: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        buttonOpenFileManager = findViewById(R.id.button_open_file_manager)
        buttonOpenFileManager.setOnClickListener{
            startActivity(Intent(this, FileListActivity::class.java))
        }


    }


}
