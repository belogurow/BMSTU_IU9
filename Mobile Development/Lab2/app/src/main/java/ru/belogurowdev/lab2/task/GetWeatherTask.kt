package ru.belogurowdev.lab2.task

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import ru.belogurowdev.lab2.GetWeatherTask.DownloadResponse
import ru.belogurowdev.lab2.model.Weather


/**
 * Created by alexbelogurow on 13.09.17.
 */
class GetWeatherTask(var delegate: DownloadResponse?) : AsyncTask<String, Void, String>() {

    val TAG = "GetWeatherTask"

    interface DownloadResponse {
        fun processFinish(output: Weather)

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

        try {
            val jsonObject = JSONObject(result)
            val mainData = jsonObject.getJSONObject("main")

            val temp = mainData.getString("temp")
            val city = jsonObject.getString("name")

            delegate?.processFinish(Weather(city, temp, ""))
        } catch (e: JSONException) {
            e.printStackTrace();
        }


    }
}