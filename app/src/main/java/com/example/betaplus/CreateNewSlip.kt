package com.example.betaplus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.betaplus.models.Slip
import java.lang.Exception
import kotlin.math.ceil
import kotlin.math.ln
import kotlin.math.pow

private const val TAG = "CreateNewSlip"

class CreateNewSlip : AppCompatActivity() {
    var repo = Repository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_slip)

        findViewById<Button>(R.id.createNewSlipButton).setOnClickListener{ validateInput()}
    }

    private fun validateInput() {
        Log.i(TAG, "Validating Input")
        val startAmount = findViewById<TextView>(R.id.startAmountInput).text.toString().toDouble()
        val givenOdd = findViewById<TextView>(R.id.givenOddInput).text.toString().toDouble()
        val retryLimit = findViewById<TextView>(R.id.retriesLimitInput).text.toString().toInt()
        val target = findViewById<TextView>(R.id.oddTargetInput).text.toString().toInt()
        Log.i(TAG, "Start Amount: $startAmount, Given Odd: $givenOdd, Retry Limit : $retryLimit, Target Odd: $target")
        initializeSlip(startAmount, givenOdd, retryLimit, target)
    }

    private fun initializeSlip(startAmount: Double, givenOdd: Double, retryLimit: Int, target: Int) {
        var zRequest = givenOdd - 1.0
        var slip = Slip()

        slip.retryOdd = (givenOdd / zRequest);
        slip.retryCount = 0
        slip.retryLimit = retryLimit
        var totalSum = (1 - slip.retryOdd.pow(slip.retryLimit.toDouble())) / (1 - slip.retryOdd);
        slip.growOdd = (totalSum + zRequest) / totalSum;
        slip.initOdd = (1 / totalSum);

        slip.oddCount = 1.0
        slip.oddLimit = givenOdd
        slip.progressLimit = ceil(ln(target.toDouble()) / ln(slip.growOdd)).toInt()
        slip.progressCount = 0
        slip.amount = startAmount
        slip.bonus = 0.0

        try {
            repo.insertSlip(slip)
        }catch(ex: Exception){
            Toast.makeText(this, ex.message.toString(), Toast.LENGTH_LONG).show()
            return;
        }


        startActivity(Intent(this, SlipActivity::class.java).apply {
            putExtra(SLIP_ID, slip.id)
        })
    }

}