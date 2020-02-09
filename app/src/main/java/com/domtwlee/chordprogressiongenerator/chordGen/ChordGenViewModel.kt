package com.domtwlee.chordprogressiongenerator.chordGen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.domtwlee.chordprogressiongenerator.R
import java.util.*
import kotlin.random.Random

private const val TAG = "ChordGenViewModel"

class ChordGenViewModel : ViewModel() {

    private val chordProgParams =
        ChordGenParams()
    private val romanNumerals = listOf("I", "II", "III", "IV", "V", "VI", "VII")
    private val majorDegrees = listOf("I", "ii", "iii", "IV", "V", "vi", "viio")
    private val minorDegrees = listOf("i", "iio", "III", "iv", "V", "VI", "VII")
    private val scaleTypes = mapOf("major" to majorDegrees, "minor" to minorDegrees)
    private val modifiers = listOf("min", "aug", "dim", "inv1st", "inv2nd", "7th", "9th")

    val sounds = mapOf(
        "I" to R.raw.c7_chord,
        "i" to R.raw.cm7_chord,
        "II" to R.raw.d7_chord,
        "ii" to R.raw.dm7_chord,
        "iio" to R.raw.dm7_chord,
        "III" to R.raw.e7_chord,
        "iii" to R.raw.em7_chord,
        "IV" to R.raw.f7_chord,
        "iv" to R.raw.fm7_chord,
        "V" to R.raw.g7_chord,
        "VI" to R.raw.a7_chord,
        "vi" to R.raw.am7_chord,
        "VII" to R.raw.b7_chord,
        "viio" to R.raw.bm7_chord)

    val chordProgression = mutableListOf<String>()

    fun getChordProgParams() = chordProgParams

    fun getChordProg(): String = chordProgression.joinToString(" ")

    private fun randomize(): Pair<Int, Int> {
        val randomModifier = Random.nextInt(7)
        val randomDegree = Random.nextInt(0, 6)
        return Pair(randomModifier, randomDegree)
    }

    private fun addRandomChord(degree: Int? = null) {
        val (t, d) = randomize()
        val numDegree = degree ?: d
        val romanDegree = romanNumerals[numDegree]

        val chord = when (modifiers[t]) {
            "min" -> romanDegree.toLowerCase(Locale.getDefault())
            "aug" -> "$romanDegree+"
            "dim" -> romanDegree + "o"
            "inv1st" -> "$romanDegree/1"
            "inv2nd" -> "$romanDegree/2"
            "7th" -> romanDegree + "7"
            "9th" -> romanDegree + "9"
            else -> romanDegree
        }

        chordProgression.add(chord)
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
                val (_, d) = randomize()
                chordProgression.add(degrees[d])
            }

            chordProgression.add(degrees[end - 1])
        }
    }
}