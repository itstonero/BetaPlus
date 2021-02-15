package com.example.betaplus

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.betaplus.models.Match
import com.example.betaplus.models.Slip

private const val TAG = "Repository"

class Repository(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(dB: SQLiteDatabase?) {
        dB?.execSQL("CREATE TABLE IF NOT EXISTS ${DB_SLIP_TABLE}(" +
                "$DB_SLIP_ID INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "$DB_SLIP_RETRY_COUNT INTEGER NOT NULL, " +
                "$DB_SLIP_RETRY_LIMIT INTEGER NOT NULL, " +
                "$DB_SLIP_PROGRESS_COUNT INTEGER NOT NULL, " +
                "$DB_SLIP_PROGRESS_LIMIT INTEGER NOT NULL, " +
                "$DB_SLIP_ODD_COUNT DOUBLE NOT NULL, " +
                "$DB_SLIP_ODD_LIMIT DOUBLE NOT NULL, " +
                "$DB_SLIP_AMOUNT DOUBLE NOT NULL, " +
                "$DB_SLIP_BONUS DOUBLE NOT NULL, " +
                "$DB_SLIP_INIT_ODD DOUBLE NOT NULL, " +
                "$DB_SLIP_GROW_ODD DOUBLE NOT NULL, " +
                "$DB_SLIP_RETRY_ODD DOUBLE NOT NULL" +
                ")")
        dB?.execSQL("CREATE TABLE IF NOT EXISTS ${DB_MATCH_TABLE}(" +
                "$DB_MATCH_ID INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "$DB_MATCH_HOME TEXT NOT NULL, " +
                "$DB_MATCH_AWAY TEXT NOT NULL, " +
                "$DB_MATCH_ADVICE TEXT NOT NULL, " +
                "$DB_MATCH_TIME TEXT NOT NULL, " +
                "$DB_MATCH_ODD DOUBLE NOT NULL)")
    }

    override fun onUpgrade(dB: SQLiteDatabase?, p1: Int, p2: Int) {
        dB?.execSQL("DROP TABLE IF EXISTS $DB_MATCH_TABLE")
        onCreate(dB)
    }

    fun insertSlip(slip: Slip)
    {
        val dB = this.writableDatabase
        val content = ContentValues()
        content.put(DB_SLIP_AMOUNT, slip.amount)
        content.put(DB_SLIP_BONUS, slip.bonus)
        content.put(DB_SLIP_RETRY_COUNT, slip.retryCount)
        content.put(DB_SLIP_RETRY_LIMIT, slip.retryLimit)
        content.put(DB_SLIP_PROGRESS_COUNT, slip.progressCount)
        content.put(DB_SLIP_PROGRESS_LIMIT, slip.progressLimit)
        content.put(DB_SLIP_ODD_COUNT, slip.oddCount)
        content.put(DB_SLIP_ODD_LIMIT, slip.oddLimit)
        content.put(DB_SLIP_INIT_ODD, slip.initOdd)
        content.put(DB_SLIP_RETRY_ODD, slip.retryOdd)
        content.put(DB_SLIP_GROW_ODD, slip.growOdd)
        dB.insert(DB_SLIP_TABLE, null, content)
    }

    fun insertMatch(match: Match)
    {
        val dB = this.writableDatabase
        val content = ContentValues()
        content.put(DB_MATCH_HOME, match.home)
        content.put(DB_MATCH_AWAY, match.away)
        content.put(DB_MATCH_ADVICE, match.advice)
        content.put(DB_MATCH_TIME, match.timestamp)
        content.put(DB_MATCH_ODD, match.odd)
        dB.insert(DB_MATCH_TABLE, null, content)
    }

    fun updateSlip(slip: Slip) : Boolean {
        val dB = this.writableDatabase
        val content = ContentValues()
        content.put(DB_SLIP_ID, slip.id)
        content.put(DB_SLIP_AMOUNT, slip.amount)
        content.put(DB_SLIP_BONUS, slip.bonus)
        content.put(DB_SLIP_RETRY_COUNT, slip.retryCount)
        content.put(DB_SLIP_RETRY_LIMIT, slip.retryLimit)
        content.put(DB_SLIP_PROGRESS_COUNT, slip.progressCount)
        content.put(DB_SLIP_PROGRESS_LIMIT, slip.progressLimit)
        content.put(DB_SLIP_ODD_COUNT, slip.oddCount)
        content.put(DB_SLIP_ODD_LIMIT, slip.oddLimit)
        content.put(DB_SLIP_INIT_ODD, slip.initOdd)
        content.put(DB_SLIP_RETRY_ODD, slip.retryOdd)
        content.put(DB_SLIP_GROW_ODD, slip.growOdd)

        var affected = dB.update(DB_SLIP_TABLE, content, "$DB_SLIP_ID = ?", arrayOf(slip.id.toString()))
        return (affected > 0)
    }

    fun updateMatch(match: Match) : Boolean {
        val dB = this.writableDatabase
        val content = ContentValues()
        content.put(DB_MATCH_ID, match.id)
        content.put(DB_MATCH_HOME, match.home)
        content.put(DB_MATCH_AWAY, match.away)
        content.put(DB_MATCH_ADVICE, match.advice)
        content.put(DB_MATCH_TIME, match.timestamp)
        content.put(DB_MATCH_ODD, match.odd)
        var affected = dB.update(DB_MATCH_TABLE, content, "$DB_SLIP_ID = ?", arrayOf(match.id.toString()))
        return (affected > 0)
    }

    val allSlips : ArrayList<Slip>
    get(){
        var allSlips:ArrayList<Slip> = ArrayList();

        val dB = this.writableDatabase
        var result = dB.rawQuery("SELECT * FROM $DB_SLIP_TABLE", null)
        while(result.moveToNext())
        {
            var newSlip:Slip = Slip()
            newSlip.id = result.getInt(0)
            newSlip.retryCount = result.getInt(1)
            newSlip.retryLimit = result.getInt(2)
            newSlip.progressCount = result.getInt(3)
            newSlip.progressLimit = result.getInt(4)
            newSlip.oddCount = result.getDouble(5)
            newSlip.oddLimit = result.getDouble(6)
            newSlip.amount = result.getDouble(7)
            newSlip.bonus = result.getDouble(8)
            newSlip.initOdd = result.getDouble(9)
            newSlip.growOdd = result.getDouble(10)
            newSlip.retryOdd = result.getDouble(11)
            allSlips.add(newSlip)
        }
        return allSlips
    }

    val allMatches : ArrayList<Match>
        get(){
            var allMatches:ArrayList<Match> = ArrayList();

            val dB = this.writableDatabase
            var result = dB.rawQuery("SELECT * FROM $DB_MATCH_TABLE", null)
            while(result.moveToNext()) {
                var newMatch = Match()
                newMatch.id = result.getInt(0)
                newMatch.home = result.getString(1)
                newMatch.away = result.getString(2)
                newMatch.advice = result.getString(3)
                newMatch.timestamp = result.getString(4)
                newMatch.odd = result.getDouble(5)
                allMatches.add(newMatch)
            }

            return allMatches
        }

    fun retrieveSlip(newSlip : Slip) : Slip {
        val dB = this.writableDatabase
        var result = dB.rawQuery("SELECT * FROM $DB_SLIP_TABLE WHERE $DB_SLIP_ID = '${newSlip.id}'", null)

        while(result.moveToFirst())
        {
            newSlip.id = result.getInt(0)
            newSlip.retryCount = result.getInt(1)
            newSlip.retryLimit = result.getInt(2)
            newSlip.progressCount = result.getInt(3)
            newSlip.progressLimit = result.getInt(4)
            newSlip.oddCount = result.getDouble(5)
            newSlip.oddLimit = result.getDouble(6)
            newSlip.amount = result.getDouble(7)
            newSlip.bonus = result.getDouble(8)
            newSlip.initOdd = result.getDouble(9)
            newSlip.growOdd = result.getDouble(10)
            newSlip.retryOdd = result.getDouble(11)
        }

        return newSlip
    }

    fun retrieveMatch(newMatch: Match) : Match{
        val dB = this.writableDatabase
        var result = dB.rawQuery("SELECT * FROM $DB_MATCH_TABLE WHERE $DB_MATCH_ID = '${newMatch.id}'", null)
        while(result.moveToFirst()) {
            newMatch.id = result.getInt(0)
            newMatch.home = result.getString(1)
            newMatch.away = result.getString(2)
            newMatch.advice = result.getString(3)
            newMatch.timestamp = result.getString(4)
            newMatch.odd = result.getDouble(5)
        }
        return newMatch
    }

    fun deleteMatches(id : String?) {
        onUpgrade(this.writableDatabase, 0, 0)
    }

    companion object{
        const val DATABASE_NAME:String = "myGames"
        const val DB_SLIP_TABLE = "slips"
        const val DB_SLIP_ID = "id"
        const val DB_SLIP_RETRY_COUNT = "retryCount"
        const val DB_SLIP_RETRY_LIMIT = "retryLimit"
        const val DB_SLIP_PROGRESS_COUNT = "progressCount"
        const val DB_SLIP_PROGRESS_LIMIT = "progressLimit"
        const val DB_SLIP_ODD_COUNT = "oddCount"
        const val DB_SLIP_ODD_LIMIT = "oddLimit"
        const val DB_SLIP_AMOUNT = "amount"
        const val DB_SLIP_BONUS = "bonus"
        const val DB_SLIP_INIT_ODD = "initOdd"
        const val DB_SLIP_GROW_ODD = "growOdd"
        const val DB_SLIP_RETRY_ODD = "retryOdd"

        const val DB_MATCH_TABLE = "matches"
        const val DB_MATCH_ID = "id"
        const val DB_MATCH_HOME = "homeTeam"
        const val DB_MATCH_AWAY = "awayTeam"
        const val DB_MATCH_ADVICE = "gameAdvice"
        const val DB_MATCH_TIME = "gameTime"
        const val DB_MATCH_ODD = "gameOdd"
    }
}