package com.android_project.movie_recommender

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RecommenderViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recommender_activity)

        val data : Movie = intent.getParcelableExtra("data")!!
        val btnReturn : Button = findViewById(R.id.btnRcmReturn)
        val movie_title: TextView = findViewById<TextView>(R.id.rcm_movie_title)

        movie_title.text = data.title

        btnReturn.setOnClickListener {
            finish()
        }

    }
}