package com.example.betaplus

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.betaplus.models.Match
import org.w3c.dom.Text
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "CreateNewMatchActivity"

class CreateNewMatchActivity : AppCompatActivity() {
    private var repo = Repository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_match)

        findViewById<Button>(R.id.addMatchButton).setOnClickListener{

            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                var timeStamp = SimpleDateFormat("HH:mm").format(cal.time)

                try {
                    var match = Match()

                    match.home = findViewById<TextView>(R.id.homeTeamInput).text.toString()
                    match.away = findViewById<TextView>(R.id.awayTeamInput).text.toString()
                    match.advice = findViewById<TextView>(R.id.gameHintInput).text.toString()
                    match.odd = findViewById<TextView>(R.id.gameAdviceInput).text.toString().toDouble()
                    match.timestamp = timeStamp
                    repo.insertMatch(match)
                    startActivity(Intent(this, AllMatches::class.java))
                }catch (ex:Exception){
                    Toast.makeText(this, ex.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
    }
}