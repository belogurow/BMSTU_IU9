package ru.belogurowdev.lab2.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import ru.belogurowdev.lab2.R
import ru.belogurowdev.lab2.model.Weather
import ru.belogurowdev.lab2.task.GetWeatherTask
import ru.belogurowdev.lab2.task.GetWeatherTask.DownloadResponse
import java.util.*


class MainActivity : AppCompatActivity() {

    private val KEY = "3cb09e739fef1e349def2c8634be954a"

    private var mEditTextCity: EditText? = null
    private var mButtonShowWeather: Button? = null
    private var mTextViewData: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()

        mButtonShowWeather?.setOnClickListener {
            getRequest()
        }
    }

    private fun initializeViews() {
        mEditTextCity = findViewById(R.id.edit_city)
        mButtonShowWeather = findViewById(R.id.button_show_weather)
        mTextViewData = findViewById(R.id.text_data)
    }

    private fun getRequest() {
        val url = "http://api.openweathermap.org/data/2.5/weather?q=${mEditTextCity?.text}&APPID=$KEY"
        GetWeatherTask(object : DownloadResponse {
            override fun processFinish(output: Weather) {
                mTextViewData?.text = output.toString()
            }

        }).execute(url)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_update -> getRequest()
            R.id.menu_en -> changeLang("en")
            R.id.menu_ru -> changeLang("ru")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeLang(lang: String) {
        /*val resources = resources
        val configuration = resources.configuration
        configuration.setLocale(Locale(lang))
        applicationContext.createConfigurationContext(configuration)
        */
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = baseContext.resources.configuration
        config.locale = locale

        baseContext.resources.updateConfiguration(config,
                baseContext.resources.displayMetrics)

        startActivity(Intent(applicationContext, MainActivity::class.java))
    }
}
