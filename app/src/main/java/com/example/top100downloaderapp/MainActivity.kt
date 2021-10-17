package com.example.top100downloaderapp

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var myRv : RecyclerView
    lateinit var button : Button
    lateinit var tv : TextView
    var Top10Apps = mutableListOf<Top10Apps>()
    var bool = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myRv = findViewById(R.id.recyclerView)
        button = findViewById(R.id.button)
        tv = findViewById(R.id.textView)
        button.setOnClickListener {
            Fetch10Apps().execute()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menus, menu)
        val item1: MenuItem = menu!!.getItem(0)
        item1.setVisible(true)
        val item2: MenuItem = menu!!.getItem(1)
        item2.setVisible(true)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.m1 -> {
                bool = false
                return true
            }
            R.id.m2 -> {
                bool=true
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private inner class Fetch10Apps: AsyncTask<Void, Void, MutableList<Top10Apps>>(){
        val parser = XMLParser()
        override fun doInBackground(vararg p0: Void?): MutableList<Top10Apps> {
            var url: URL
            if(bool == false) {
                tv.text="Top 10 Apps in App Store"
                url = URL("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
            }else{
                tv.text="Top 100 Apps in App Store"
                url =URL("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=100/xml")
            }
            val urlConnection = url.openConnection() as HttpURLConnection
            Top10Apps = urlConnection.getInputStream()?.let {
                parser.parse(it)
            } as MutableList<Top10Apps>
            return Top10Apps
        }

        override fun onPostExecute(result: MutableList<Top10Apps>?) {
            super.onPostExecute(result)
            myRv.adapter = RecyclerViewAdapter(Top10Apps)
            myRv.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

}