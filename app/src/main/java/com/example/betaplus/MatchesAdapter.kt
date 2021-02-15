package com.example.betaplus

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.betaplus.models.Match

private const val TAG = "MatchesAdapter"

class MatchesAdapter(var context : Context, var allMatches: ArrayList<Match>) : RecyclerView.Adapter<MatchesAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindMatch(match: Match) {
            Log.i(TAG, "Binding for ${match.home} vs ${match.away}")
            itemView.findViewById<TextView>(R.id.homeTeamTextView).text = match.home
            itemView.findViewById<TextView>(R.id.awayTeamTextView).text = match.away
            itemView.findViewById<TextView>(R.id.gameHintTextView).text = match.advice
            itemView.findViewById<TextView>(R.id.gameTimeTextView).text = match.timestamp
            itemView.findViewById<TextView>(R.id.gameOddTextView).text = match.odd.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "Creating View Holder")
        var layout = LayoutInflater.from(parent.context).inflate(R.layout.layout_all_matches, parent, false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        Log.i(TAG, "Total List Count :: ${allMatches.size}")
        return allMatches.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "Binding For Position :: $position")
        holder.itemView.setOnClickListener{
            Toast.makeText(context, "Bound to $position Position", Toast.LENGTH_LONG).show()
        }
        holder.bindMatch(allMatches[position])
    }
}