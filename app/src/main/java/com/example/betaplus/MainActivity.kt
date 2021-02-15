package com.example.betaplus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    var tempToggler: Boolean = false;
    private var repo = Repository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.OpenSlipButton).setOnClickListener{ openSlips() }
        findViewById<Button>(R.id.OpenGamesButton).setOnClickListener{ openGames() }
    }

    private fun openGames() {
        startActivity(Intent(this, MatchActivity::class.java))
    }

    private fun openSlips() {
        try {
            var allSlips = repo.allSlips
            if(allSlips.size == 0)
            {
                startActivity(Intent(this, CreateNewSlip::class.java))
            }else
            {
//                var viewer = findViewById<RecyclerView>(R.id.recyclerView)
//                viewer.adapter = SlipAdapter(this, allSlips)
//                viewer.layoutManager = LinearLayoutManager(this)
                startActivity(Intent(this, SlipActivity::class.java))
            }
        }catch (ex: Exception) {
            Toast.makeText(this, ex.message.toString(), Toast.LENGTH_LONG).show()
        }
    }
}