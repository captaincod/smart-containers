package com.example.smartcontainersapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = intent.getStringExtra("url")
        Log.d("mytag", url.toString())
        val mainURL = "http://$url:5000/"
        val okHttpClient: OkHttpClient = OkHttpClient()

        val serverText: TextView = findViewById(R.id.server)
        val urlText = "Сервер " + url.toString()
        serverText.text = urlText

        val mainText: TextView = findViewById(R.id.main)
        val request: Request = Request.Builder()
            .url(mainURL)
            .build()
        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("mytag", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val txt = response.body()?.string()
                runOnUiThread {
                    mainText.text = txt
                }
            }
        })

        val button1: Button = findViewById(R.id.toFirstBin)
        val button2: Button = findViewById(R.id.toSecondBin)
        val button3: Button = findViewById(R.id.toThirdBin)

        button1.setOnClickListener(){
            val intent1 = Intent(this, FirstBinActivity::class.java)
            intent1.putExtra("url", url)
            startActivity(intent1)
        }

        button2.setOnClickListener(){
            val intent2 = Intent(this, SecondBinActivity::class.java)
            intent2.putExtra("url", url)
            startActivity(intent2)
        }

        button3.setOnClickListener(){
            val intent3 = Intent(this, ThirdBinActivity::class.java)
            intent3.putExtra("url", url)
            startActivity(intent3)
        }



    }
}