package com.example.tip_calculator

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val numbers: Array<String> = resources.getStringArray(R.array.number)
        //<-------seekBar--------->
        seekBar1.progress = 15
        tipDiscription(15)
        val seek = findViewById<SeekBar>(R.id.seekBar1)
        seek?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                textView2.text = "$progress%"
                tipDiscription(progress)
                tipCaluclator()
                calcSplitAmt(numbers)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {}

            override fun onStopTrackingTouch(seek: SeekBar) {
                Toast.makeText(this@MainActivity, "Tip % is: " + seek.progress + "%", Toast.LENGTH_SHORT).show()
            }
        })

        //<-------editText--------->
        editText1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                tipCaluclator()
                calcSplitAmt(numbers)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        //<-------spinner--------->
        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, numbers
            )
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                textView6.text = "Select no. of people"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                calcSplitAmt(numbers)
            }
        }
    }

    private fun calcSplitAmt(numbers:Array<String>) {
        val s = numbers[spinner.selectedItemPosition].toInt()
        var totalTip:Float = 0.0F
        if(!Tip_Amt.text.isEmpty()) {
            totalTip = Tip_Amt.text.toString().toFloat()
        }
        val perTip = totalTip / s
        textView6.text = "%.2f".format(perTip)

    }

    private fun tipDiscription(progress: Int) {
        var disc :String = when(progress){
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            in 25..30 -> "Amazing"
            else -> "Ok"
        }
        textView5.text = disc
        val color = ArgbEvaluator().evaluate(progress.toFloat()/seekBar1.max,
            ContextCompat.getColor(this,R.color.colorWorstTip),
            ContextCompat.getColor(this,R.color.colorBestTip)) as Int
        textView5.setTextColor(color)
    }

    private fun tipCaluclator(){
        if(editText1.text.isEmpty()){
            Tip_Amt.text=""
            Total_Amt.text=""
            return
        }
        val tipP = seekBar1.progress
        val baseAmt = editText1.text.toString().toDouble()
        val tip = baseAmt * tipP / 100
        val total = tip + baseAmt
        Tip_Amt.text = "%.2f".format(tip)
        Total_Amt.text = "%.2f".format(total)
    }
}

