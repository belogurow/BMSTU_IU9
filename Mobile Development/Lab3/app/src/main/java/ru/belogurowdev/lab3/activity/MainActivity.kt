package ru.belogurowdev.lab3.activity

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_main.*
import ru.belogurowdev.lab3.R
import ru.belogurowdev.lab3.adapter.WeatherAdapter
import ru.belogurowdev.lab3.model.Weather
import ru.belogurowdev.lab3.task.GetWeatherTask

class MainActivity : AppCompatActivity() {

    private val KEY = "3cb09e739fef1e349def2c8634be954a"

    private var mRecyclerView: RecyclerView? = null
    private var mButtonUpdate: Button? = null
    private var mProgress: ProgressBar? = null

    private var adapter: WeatherAdapter? = null
    private var context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        mButtonUpdate?.setOnClickListener {
            getRequest()
        }
    }

    private fun initViews() {
        mRecyclerView = findViewById(R.id.recycler_weather)
        mButtonUpdate = findViewById(R.id.button_update)
        mProgress = findViewById(R.id.progress)

        mRecyclerView?.layoutManager = LinearLayoutManager(this)


    }

    private fun getRequest() {
        val url = "http://api.openweathermap.org/data/2.5/group?id=524901,703448,2643743&APPID=$KEY"
        GetWeatherTask(object : GetWeatherTask.DownloadResponse {
            override fun processFinish(output: ArrayList<Weather>) {
                adapter = WeatherAdapter(context, output)
                mRecyclerView?.adapter = adapter

            }


        }, mProgress).execute(url)

    }
}
