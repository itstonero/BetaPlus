package com.example.betaplus

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request


private const val TAG = "MatchActivity"

class MatchActivity : AppCompatActivity() {
    private var repo = Repository(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        findViewById<Button>(R.id.newMatchButton)
            .setOnClickListener{
                //startActivity(Intent(this, CreateNewMatchActivity::class.java))
                RetrieveMatches().execute("")
            }

        findViewById<Button>(R.id.allMatchesButton)
            .setOnClickListener{ startActivity(Intent(this, AllMatches::class.java))}

        findViewById<Button>(R.id.clearMatchButton)
            .setOnClickListener{ repo.deleteMatches(null)}
    }

    internal class RetrieveMatches :
        AsyncTask<String?, Void?, String?>() {
        private var exception: Exception? = null
        override fun onPostExecute(feed: String?) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }

        override fun doInBackground(vararg p0: String?): String? {
            var result:String = ""

            try {
                val client = OkHttpClient()

                val request = Request.Builder()
                    .url("https://api-football-beta.p.rapidapi.com/odds?date=2020-05-15")
                    .get()
                    .addHeader("x-rapidapi-key", "944b51b49dmsh339c99f0345fdd2p13db62jsned6a3c13eab9")
                    .addHeader("x-rapidapi-host", "api-football-beta.p.rapidapi.com")
                    .build()

                val response = client.newCall(request).execute()

                Log.i(TAG, response.body().bytes().toString())

            } catch (e: Exception) {
                exception = e
                Log.e(TAG, e.message.toString())
                return null
            } finally {
                return result
            }
        }
    }
}