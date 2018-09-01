package com.nagisa.orenagi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SwipeRefresh.setOnRefreshListener({
            // 引っ張って離した時に呼ばれます。

            //↓これないと永遠とぐるぐるします
            if (SwipeRefresh.isRefreshing()) {
                SwipeRefresh.setRefreshing(false);
            }
        })
    }
}
