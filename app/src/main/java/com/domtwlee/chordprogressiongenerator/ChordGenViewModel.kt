package com.domtwlee.chordprogressiongenerator

import androidx.lifecycle.ViewModel
import kotlin.random.Random

class ChordGenViewModel : ViewModel() {

    private val chordProgParams = ChordGenParams()
    private val majorDegrees = listOf("I", "ii", "iii", "IV", "V", "vi", "viio")
    private val minorDegrees = listOf("i", "iio", "III", "iv", "V", "VI", "VII")
    private val scaleTypes = mapOf("major" to majorDegrees, "minor" to minorDegrees)
    private val chordProgression = mutableListOf<String>()

    fun getChordProgParams() = chordProgParams

    fun getChordProg(): String = chordProgression.joinToString(" ")

    private fun randomize(): Pair<Int, Int> {
        val randomType = Random.nextInt(2)
        val randomDegree = Random.nextInt(0, 6)
        return Pair(randomType, randomDegree)
    }

    private fun addRandomChord(degree: Int? = null) {
        val (t, d) = randomize()
        val actualDegree = degree ?: d
        chordProgression.add(if (t == 0) minorDegrees[actualDegree] else majorDegrees[actualDegree])
    }

    fun createChordProgression() {
        if (chordProgression.isNotEmpty()) {
            chordProgression.clear()
        }

        val (length, start, end, type) = chordProgParams
        val degrees: List<String>? = scaleTypes.getOrElse(type) { null }
        val bodyLength = length - 2

        if (degrees == null) {
            addRandomChord(start - 1)

            for (i in 1..bodyLength) {
                addRandomChord()
            }

            addRandomChord(end - 1)
        } else {
            chordProgression.add(degrees[start - 1])

            for (i in 1..bodyLength) {
                val (t, d) = randomize()
                chordProgression.add(degrees[d])
            }

            chordProgression.add(degrees[end - 1])
        }
    }
}