package com.example.onescreenapp.util

import kotlin.random.Random

class Dice() {

    fun rollDice(diceSides: Int = 6): Int {
        return Random.nextInt(diceSides) + 1
    }
}
