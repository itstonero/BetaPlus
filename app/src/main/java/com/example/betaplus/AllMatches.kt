package com.example.betaplus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception

private const val TAG = "AllMatches"

class AllMatches : AppCompatActivity() {
    private var repo = Repository(this)
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: MatchesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_matches)

        linearLayoutManager = LinearLayoutManager(this)
        var recyclerView = findViewById<RecyclerView>(R.id.matchesRecyclerView)
        recyclerView.layoutManager = linearLayoutManager

        adapter = MatchesAdapter(this, repo.allMatches)
        recyclerView.adapter = adapter


        try {
            Log.i(TAG, "Total Matches Size :: ${repo.allMatches.size}")
        }catch (ex:Exception){
            Toast.makeText(this, ex.message.toString(), Toast.LENGTH_LONG).show()
        }
    }
}