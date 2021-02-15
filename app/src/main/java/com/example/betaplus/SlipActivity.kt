package com.example.betaplus

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.betaplus.models.Slip
import java.lang.Exception
import kotlin.math.pow

private const val TAG = "SlipActivity"
const val SLIP_ID = "slipID"

class SlipActivity : AppCompatActivity() {
    private var repo = Repository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slip)

        retrieveSlip(intent.getStringExtra(SLIP_ID))
        findViewById<Button>(R.id.retrySlipButton).setOnClickListener{ retrySlip() }
        findViewById<Button>(R.id.growSlipButton).setOnClickListener{ confirmOdd() }
        findViewById<TextView>(R.id.bonusAmountTextView).setOnClickListener{ clearBonus() }
    }

    private fun retrieveSlip(slipID: String?) {
        Log.i(TAG, "Retrieving Slip for $slipID")
        var allSlip = repo.allSlips

        Log.i(TAG, "All Slips count :: ${allSlip.size}")

        if(allSlip.size == 0)
        {
            startActivity(Intent(this, CreateNewSlip::class.java))
            return
        }
        setValues(allSlip[0])
    }

    private fun retrySlip(){
        Log.i(TAG, "Retrying Slip")

        try {
            var slip = repo.allSlips[0]
            if(slip.retryCount >= slip.retryLimit)
            {
                Toast.makeText(this, "Limit Cannot Be Exceeded", Toast.LENGTH_LONG).show()
                return
            }

            slip.retryCount += 1
            slip.oddCount = 1.0

            var context = this
            val builder = AlertDialog.Builder(context)

            with(builder)
            {
                setTitle("Slip")
                setMessage("Confirm Slip Retry")
                setPositiveButton("Confirm", DialogInterface.OnClickListener{ dialog: DialogInterface, which: Int ->
                    if(!repo.updateSlip(slip))
                    {
                        Toast.makeText(context, "Failed To Update Slip", Toast.LENGTH_SHORT).show()
                    }
                    setValues(slip)
                })
                show()
            }
        }catch (ex:Exception) {
            Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
            return;
        }
    }

    private fun clearBonus() {
        Log.i(TAG, "Clearing Bonus")


        try {
            var slip = repo.allSlips[0]
            slip.bonus = 0.0

            var context = this
            val builder = AlertDialog.Builder(context)

            with(builder)
            {
                setTitle("Slip")
                setMessage("Clear Bonus Balance")
                setPositiveButton("Confirm", DialogInterface.OnClickListener{ dialog: DialogInterface, which: Int ->
                    if(!repo.updateSlip(slip))
                    {
                        Toast.makeText(context, "Failed To Update Slip", Toast.LENGTH_SHORT).show()
                    }
                    setValues(slip)
                })
                show()
            }
        }catch (ex:Exception) {
            Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
            return;
        }

    }

    private fun setValues(slip: Slip) {
        Log.i(TAG, "Setting Values")
        var stageStartAmount = slip.amount * slip.initOdd;
        var currentAmount = stageStartAmount * slip.retryOdd.pow(slip.retryCount.toDouble());
        var receivedAmount = (currentAmount * slip.oddCount) - currentAmount;
        //var totalSum = ( stageStartAmount * (1 - slip.retryOdd.pow(slip.retryCount.toDouble()))) / (1 - slip.retryOdd);

        var adviceOdd = slip.oddLimit / slip.oddCount;
        adviceOdd *= 100

        findViewById<TextView>(R.id.retryTextView).text = "${slip.retryCount}/${slip.retryLimit}"
        findViewById<TextView>(R.id.growTextView).text = "${slip.progressCount}/${slip.progressLimit}"
        findViewById<TextView>(R.id.adviceAmountTextView).text = "${(currentAmount + receivedAmount).toInt()}"
        findViewById<TextView>(R.id.adviceOddTextView).text = (adviceOdd.toInt() / 100.00).toString()
        findViewById<TextView>(R.id.stageAmountTextView).text = "${slip.amount.toInt()}"
        findViewById<TextView>(R.id.bonusAmountTextView).text = "${slip.bonus.toInt()}"
    }

    private fun confirmOdd() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("Confirm Odd")
        val dialogLayout = inflater.inflate(R.layout.confirm_grow_odd, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.confirmedOdd)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Confirm") { _, _ ->
            var usedOdd = editText.text.toString()

            try {
                var slip = repo.allSlips[0]

                if(usedOdd.isNotEmpty() && usedOdd.toDouble() != 0.0){
                    slip.oddCount *= usedOdd.toDouble()
                }

                if (slip.oddCount >= slip.oddLimit)
                {
                    val currentAmount = slip.amount * slip.initOdd * slip.retryOdd.pow(slip.retryCount.toDouble())
                    slip.bonus += currentAmount * (slip.oddCount - slip.oddLimit)
                    slip.oddCount = 1.0
                    slip.retryCount = 0
                    slip.progressCount += 1
                    slip.amount *= slip.growOdd
                }

                if(!repo.updateSlip(slip))
                {
                    Toast.makeText(this, "Failed To Update Slip", Toast.LENGTH_SHORT).show()
                }else
                {
                    setValues(slip)
                }
            }catch (ex:Exception) {
                Toast.makeText(this, ex.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        builder.show()

    }
}