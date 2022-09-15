package com.example.smartcontainersapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class SecondBinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_bin)

        val url = intent.getStringExtra("url")

        val mainURL = "http://$url:5000/"
        val distURL = "http://$url:5000/bin2/distance"
        val percURL = "http://$url:5000/bin2/percent"
        val okHttpClient: OkHttpClient = OkHttpClient()

        val dateText: TextView = findViewById(R.id.date2)
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
                    dateText.text = txt
                }
            }
        })

        val distance: TextView = findViewById(R.id.second_dist)

        val requestDist: Request = Request.Builder()
            .url(distURL)
            .build()
        okHttpClient.newCall(requestDist).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("mytag", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val txt = response.body()?.string()
                runOnUiThread {
                    val distanceText = "Расстояние от крышки: " + txt.toString() + " см"
                    distance.text = distanceText
                }
            }
        })

        val percent: TextView = findViewById(R.id.second_perc)
        val fullnessText: TextView = findViewById(R.id.fullness2)

        val requestPerc: Request = Request.Builder()
            .url(percURL)
            .build()
        okHttpClient.newCall(requestPerc).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("mytag", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                val txt = response.body()?.string()
                runOnUiThread {
                    val percentText = "Свободное место: " + txt.toString() + "%"
                    percent.text = percentText
                    val fullness = txt.toString().toDouble()
                    Log.d("mytag", fullness.toString())
                    if (fullness <= 5) {
                        fullnessText.text = getString(R.string.empty)
                    } else if (fullness <= 33){
                        fullnessText.text = getString(R.string.one_third)
                    } else if (fullness <= 50){
                        fullnessText.text = getString(R.string.half)
                    } else if (fullness <= 66){
                        fullnessText.text = getString(R.string.two_thirds)
                    } else if (fullness <= 95){
                        fullnessText.text = getString(R.string.almost_empty)
                    } else{
                        fullnessText.text = getString(R.string.full)
                    }
                }
            }
        })

        val backFrom2: Button = findViewById(R.id.back_from_2)
        val second2Third: Button = findViewById(R.id.second_to_third)

        backFrom2.setOnClickListener(){
            val intent1 = Intent(this, MainActivity::class.java)
            intent1.putExtra("url", url)
            startActivity(intent1)
        }

        second2Third.setOnClickListener(){
            val intent2 = Intent(this, ThirdBinActivity::class.java)
            intent2.putExtra("url", url)
            startActivity(intent2)
        }

    }
}