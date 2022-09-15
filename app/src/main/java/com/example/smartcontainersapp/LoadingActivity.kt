package com.example.smartcontainersapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoadingActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        val chooseText: TextView = findViewById(R.id.choose_server)
        val URLtext: EditText = findViewById(R.id.URLtext)
        val buttonSetURL: Button = findViewById(R.id.toSetURL)

        buttonSetURL.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            val inputText = URLtext.text.toString()
            if (inputText != ""){
                intent.putExtra("url", inputText)
                startActivity(intent)
            }
            else{
                chooseText.text = "Введите сервер ещё раз!"
            }
        }

    }
}