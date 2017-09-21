package ru.belogurowdev.lab3.task

import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import org.json.JSONException
import org.json.JSONObject
import ru.belogurowdev.lab3.model.Weather
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by alexbelogurow on 20.09.17.
 */
class GetWeatherTask(var delegate: DownloadResponse?,
                     val mProgressBar: ProgressBar?) : AsyncTask<String, Void, String>() {

    val TAG = "GetWeatherTask"

    interface DownloadResponse {
        fun processFinish(output: ArrayList<Weather>)

    }

    override fun onPreExecute() {
        super.onPreExecute()

        mProgressBar?.visibility = View.VISIBLE
    }

    override fun doInBackground(vararg urls: String?): String {
        var result = ""
        val url: URL
        val urlConnection: HttpURLConnection

        try {
            url = URL(urls[0])
            urlConnection = url.openConnection() as HttpURLConnection

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

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        mProgressBar?.visibility = View.GONE

        try {
            val jsonObject = JSONObject(result)
            val weatherList = jsonObject.getJSONArray("list")

            var i = 0
            val resultList = arrayListOf<Weather>()
            while (i < weatherList?.length()!!) {
                val item = weatherList.get(i) as JSONObject
                Log.d(TAG, item.toString())
                val mainData = item.getJSONObject("main")
                Log.d(TAG, mainData.toString())
                val temp = mainData.getString("temp")
                Log.d(TAG, temp.toString())
                val city = item.getString("name")

                resultList.add(Weather(city, temp, ""))
                //resultList.plusElement(Weather(city, temp, ""))
                i++
            }
            delegate?.processFinish(resultList)
        } catch (e: JSONException) {
            e.printStackTrace();
        }


    }
}