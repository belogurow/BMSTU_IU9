package ru.belogurowdev.lab3.activity

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import org.json.JSONException
import org.json.JSONObject
import ru.belogurowdev.lab3.R
import ru.belogurowdev.lab3.adapter.WeatherAdapter
import ru.belogurowdev.lab3.model.Weather
import ru.belogurowdev.lab3.task.GetWeatherTask
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<String> {

    private val TAG = javaClass.simpleName
    private val KEY = "3cb09e739fef1e349def2c8634be954a"
    private val WEATHER_LOADER_ID = 13

    private var mRecyclerView: RecyclerView? = null
    private var mButtonUpdate: Button? = null
    private var mProgress: ProgressBar? = null
    private var mTextLifeCycle: TextView? = null

    private var adapter: WeatherAdapter? = null
    private var context: Context = this

    private var resultList: ArrayList<Weather>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        mButtonUpdate?.setOnClickListener {
            //getRequest()
            initLoader()
        }

        updateTextLifeCycle("onCreate()")
        Log.d(TAG, "onCreate()")
    }

    private fun initViews() {
        mRecyclerView = findViewById(R.id.recycler_weather)
        mButtonUpdate = findViewById(R.id.button_update)
        mProgress = findViewById(R.id.progress)
        mTextLifeCycle = findViewById(R.id.text_life_cycle)

        resultList = ArrayList()
        adapter = WeatherAdapter(this, resultList!!)
        mRecyclerView?.adapter = adapter

        mRecyclerView?.layoutManager = LinearLayoutManager(this)

//        val bundle = intent.extras
//        resultList = if (bundle == null) {
//            ArrayList()
//        } else {
//            bundle.getParcelableArrayList("resultList")
//        }
    }

    private fun initLoader() {
        val asyncTaskLoaderParams = Bundle()
        asyncTaskLoaderParams.putString("CITY_ID", "524901");

        val loader = supportLoaderManager.getLoader<Objects>(WEATHER_LOADER_ID)
        if (loader == null) {
            supportLoaderManager.initLoader(WEATHER_LOADER_ID, asyncTaskLoaderParams, this)
        } else {
            supportLoaderManager.restartLoader(WEATHER_LOADER_ID, asyncTaskLoaderParams, this)
        }
    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<String> =
            @SuppressLint("StaticFieldLeak")
            object : AsyncTaskLoader<String>(this) {
                override fun onStartLoading() {
                    super.onStartLoading()
                    mProgress?.visibility = View.VISIBLE

                    forceLoad()
                }

                override fun loadInBackground(): String {
                    var result = ""
                    val urlConnection: HttpURLConnection

                    try {
                        val cityId = args?.getString("CITY_ID")
                        val builder = Uri
                                .parse("http://api.openweathermap.org/data/2.5/forecast/daily?")
                                .buildUpon()
                                .appendQueryParameter("id", cityId)
                                .appendQueryParameter("APPID", KEY)
                                .build()

                        //url = URL(args[0])
                        urlConnection = URL(builder.toString()).openConnection() as HttpURLConnection

                        val input = urlConnection.getInputStream()
                        val streamReader = InputStreamReader(input)

                        var data = streamReader.read()

                        while (data != -1) {
                            result += data.toChar()
                            data = streamReader.read()
                        }

                        Log.i(TAG, result)
                        return result

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    return ""
                }


            }



    override fun onLoadFinished(loader: Loader<String>?, data: String?) {
        mProgress?.visibility = View.GONE

        try {
            val jsonObject = JSONObject(data)
            val weatherList = jsonObject.getJSONArray("list")

            var i = 0
            //val resultList = arrayListOf<Weather>()
            while (i < weatherList?.length()!!) {
                val item = weatherList.get(i) as JSONObject
                val weather = item.getJSONArray("weather").get(0) as JSONObject
                Log.d(TAG, weather.toString())
                val main = weather.getString("description")
                val temp = item.getJSONObject("temp").getString("day")
                Log.d(TAG, temp.toString())

                resultList?.add(Weather("", temp, main))
                //resultList.plusElement(Weather(city, temp, ""))
                i++
            }
            //adapter = WeatherAdapter(context, resultList!!)
            adapter?.updateList(resultList!!)
//            mRecyclerView?.adapter = adapter
        } catch (e: JSONException) {
            e.printStackTrace();
        }
    }

    override fun onLoaderReset(loader: Loader<String>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun getRequest() {
        val url = "http://api.openweathermap.org/data/2.5/forecast/daily?id=524901&APPID=$KEY"
        GetWeatherTask(object : GetWeatherTask.DownloadResponse {
            override fun processFinish(output: ArrayList<Weather>) {
                adapter = WeatherAdapter(context, output)
                mRecyclerView?.adapter = adapter

            }


        }, mProgress).execute(url)

    }

    private fun updateTextLifeCycle(text: String) {
        mTextLifeCycle?.text = "${mTextLifeCycle?.text} \n$text"
    }

    override fun onStart() {
        super.onStart()
        updateTextLifeCycle("onStart()")
        Log.d(TAG, "onStart()")
    }

    override fun onResume() {
        super.onResume()
        updateTextLifeCycle("onResume()")
        Log.d(TAG, "onResume()")

    }

    override fun onPause() {
        super.onPause()
        updateTextLifeCycle("onPause()")
        Log.d(TAG, "onPause()")
    }

    override fun onStop() {
        super.onStop()
        updateTextLifeCycle("onStop")
        Log.d(TAG, "onStop()")
    }

    override fun onRestart() {
        super.onRestart()
        updateTextLifeCycle("onRestart()")
        Log.d(TAG, "onRestart()")
    }
    override fun onDestroy() {
        super.onDestroy()
        updateTextLifeCycle("onDestroy()")
        Log.d(TAG, "onDestroy()")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        Log.d(TAG, "onSaveInstanceState()")
        super.onSaveInstanceState(outState)
        outState?.putParcelableArrayList("resultList", resultList)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        Log.d(TAG, "onRestoreInstanceState()")
        super.onRestoreInstanceState(savedInstanceState)


        if (savedInstanceState != null) {
            resultList = savedInstanceState.getParcelableArrayList("resultList")
            adapter?.updateList(resultList!!)
//            adapter = WeatherAdapter(context, resultList!!)
        }
    }
}
