package com.nagisa.orenagi


import android.accounts.AccountAuthenticatorResponse
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.R.array
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.os.AsyncTask
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.image_list.*
import kotlinx.android.synthetic.main.image_list.view.*
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

    val images = mutableListOf(
            R.drawable.beacon_2,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground)
    val titles = mutableListOf(
            "電波青年",
            "電波青年",
            "電波青年",
            "電波青年",
            "電波青年",
            "電波青年",
            "電波青年",
            "電波青年")
    val authers = mutableListOf(
            "〇〇",
            "〇〇",
            "〇〇",
            "〇〇",
            "〇〇",
            "〇〇",
            "〇〇",
            "〇〇")

//    var images=null as List<*>
//    var titles=null as List<*>
//    var authers=null as List<*>

    val comics = List(images.size) { i -> ComicData(images[i], titles[i], authers[i])}

//    val ComicList:ArrayList<String?> =arrayListOf(image,title,auther)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fullAddress = findViewById<TextView>(R.id.fullAddress)
        var receiver = locationReceiver(fullAddress)
        receiver.execute("0")

        SwipeRefresh.setOnRefreshListener({
            // 引っ張って離した時に呼ばれます。
            onRefresh()

            //↓これないと永遠とぐるぐるします
            if (SwipeRefresh.isRefreshing()) {
                SwipeRefresh.setRefreshing(false);
            }
        })

        val adapter=ComicListAdapter(this,comics)
        ListView.adapter=adapter
    }
    fun onRefresh(){
    //refresh処理
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

                var addresses=locationJSON.getJSONArray("address_components")
                var prefectureJSON=addresses.getJSONObject(4)
                var prefectureStr=prefectureJSON.getString("long_name")
                prefecture = prefectureStr.substring(0, prefectureStr.length-1);

                var cityJSON=addresses.getJSONObject(3)
                var cityStr=cityJSON.getString("long_name")
                city = cityStr.substring(0, cityStr.length-1);

                fullAddress = prefectureStr + "  " + cityStr
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

data class ComicData(val image:Int, val title:String, val auther:String)
data class ViewHolder(val imageImageView:ImageView, val titleTextView:TextView, val autherTextView:TextView)

class ComicListAdapter(context: Context, comics: List<ComicData>) : ArrayAdapter<ComicData>(context, 0, comics) {
    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view= convertView
        var holder: ViewHolder

        if (view == null) {
            view = layoutInflater.inflate(R.layout.image_list, parent, false)
            holder = ViewHolder(
                    view?.comic_image!!,
                    view.comic_title,
                    view.comic_auther
            )
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }


        val comic = getItem(position) as ComicData
        holder.imageImageView.setImageBitmap(BitmapFactory.decodeResource(context.resources, comic.image))
        holder.titleTextView.text = comic.title
        holder.autherTextView.text = comic.auther

        return view
    }

}



//class CustomImageView(context: Context) : ImageView(context) {
//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//
//        val width = width/2
//        val height = height/2
//        setMeasuredDimension(width, height)
//    }
//
//}

