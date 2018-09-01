package com.nagisa.orenagi

import android.accounts.AccountAuthenticatorResponse
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.image_list.*
import android.R.array
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.image_list.view.*


class MainActivity : AppCompatActivity() {

    val images = mutableListOf(
            R.drawable.beacon_2,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
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

    val comics = List(4) { i -> ComicData(images[i], titles[i], authers[i])}

//    val ComicList:ArrayList<String?> =arrayListOf(image,title,auther)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

