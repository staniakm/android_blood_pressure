package com.example.onescreenapp

import android.os.Bundle
import android.text.format.DateFormat
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.dice_roller.*
import java.util.*
import kotlin.random.Random


class DiceRoller : AppCompatActivity() {
    private val rollList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dice_roller)

        val adapter: ArrayAdapter<*> = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            rollList
        )
        listView.adapter = adapter;

        rollBtn.setOnClickListener {
            val time = Date().time
            val format = DateFormat.format("hh:mm:ss", time)
            val roll = getDiceRollSummary()
            rollList.add(0, "roll $roll - $format")
            rollSummaryTxt.text = "You have rolled $roll"
            adapter.notifyDataSetChanged()
        }

        dice12.setOnClickListener {
            rollBtn.text = "Roll 12 side dice"
            rollList.add(0, "Switch to 12")
            adapter.notifyDataSetChanged()
        }
        dice20.setOnClickListener {
            rollBtn.text = "Roll 20 side dice"
            rollList.add(0, "Switch to 20")
            adapter.notifyDataSetChanged()
        }
        dice6.setOnClickListener {
            rollBtn.text = "Roll 6 side dice"
            rollList.add(0, "Switch to 6")
            adapter.notifyDataSetChanged()
        }

    }

    private fun getDiceRollSummary(): Int {
        val dice = getDice()
        return Random.nextInt(dice) + 1
    }

    private fun getDice(): Int {
        if (dice12.isChecked) return 12
        if (dice20.isChecked) return 20
        return 6
    }
}