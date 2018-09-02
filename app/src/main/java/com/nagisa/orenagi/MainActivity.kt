package com.nagisa.orenagi

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import java.io.BufferedReader
import org.json.JSONObject
import org.json.JSONException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fullAddress = findViewById<TextView>(R.id.fullAddress)
        var receiver = locationReceiver(fullAddress)
        receiver.execute("0")
    }

    inner class locationReceiver(var _fullAddress:TextView) : AsyncTask<String,String,String>(){
        //    TODO: paramsはいるのか？
        override fun doInBackground(vararg params: String): String {
            //        位置情報取得できなかったので暫定的に決め打ち
            //        位置座標はNagisa社を使用
            var urlStr="https://maps.googleapis.com/maps/api/geocode/json?latlng=35.653872,139.689638&sensor=false&language=ja"
            var result=""

            var con:HttpURLConnection?=null
            var Is:InputStream?=null

            try {
                var url= URL(urlStr)
                con=url.openConnection() as HttpURLConnection
                con.requestMethod="GET"
                con.connect()
                Is=con.getInputStream()
                result=is2String(Is)
            }
            catch (ex: MalformedURLException){
            }
            catch (ex:IOException){
            }
            finally {
                if(con!=null){
                    con.disconnect()
                }
                if(Is!=null){
                    try {
                        Is.close()
                    }
                    catch (ex:IOException){
                    }
                }
            }
            return result
        }
        override fun onPostExecute(result: String?) {
            var fullAddress = ""
            var prefecture = ""
            var city = ""

            try{
                var rootJSON= JSONObject(result)
                var results= rootJSON.getJSONArray("results")

                var locationJSON=results.getJSONObject(0)
                fullAddress=locationJSON.getString("formatted_address")

                var addresses=locationJSON.getJSONArray("address_components")
                var prefectureJSON=addresses.getJSONObject(4)
                var prefectureStr=prefectureJSON.getString("long_name")
                prefecture = prefectureStr.substring(0, prefectureStr.length-1);

                var cityJSON=addresses.getJSONObject(3)
                var cityStr=cityJSON.getString("long_name")
                city = cityStr.substring(0, cityStr.length-1);
            }
            catch (ex: JSONException){}

            _fullAddress.setText(fullAddress)
        }
        @Throws(IOException::class)
        fun is2String(Is:InputStream): String{
            var reader = BufferedReader(InputStreamReader(Is, "UTF-8"))
            var sb = StringBuffer()
            var b = CharArray(1024)
            var line: Int=reader.read(b)

            while (0<=line) {
                sb.append(b, 0, line)
                line=reader.read(b)
            }
            return sb.toString()
        }
    }
}
