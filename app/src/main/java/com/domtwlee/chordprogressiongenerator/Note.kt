package com.domtwlee.chordprogressiongenerator

class Note (val note: String) {
    private val noteRe = Regex("([A-Ga-g])([#b]?)")
    private val noteParts = parseNote()
    val letter: String = noteParts[0]
    var modifier: String = noteParts[1]

    private val allNotes = listOf("A" , "Bb/A#" , "B" , "C" , "Db/C#" , "D" , "D#/Eb" , "E" , "F" , "Gb/F#", "G", "Ab/G#")
    private val majorScale = listOf("unison", "major2nd", "major3rd", "perfect4th", "perfect5th", "major6th", "major7th")
    private val minorScale = listOf("unison", "minor2nd", "minor3rd", "perfect4th", "perfect5th", "minor6th", "minor7th")
    private val intervals = mapOf(
        "subOctave" to -12,
        "unison" to 0,
        "minor2nd" to 1,
        "major2nd" to 2,
        "minor3rd" to 3,
        "major3rd" to 4,
        "perfect4th" to 5,
        "diminished5th" to 6,
        "perfect5th" to 7,
        "minor6th" to 8,
        "major6th" to 9,
        "minor7th" to 10,
        "major7th" to 11,
        "perfectOctave" to 12
    )

    fun toScale(scaleType: String): List<String> {
        if (scaleType != "major" && scaleType != "minor") {
            throw IllegalArgumentException("Invalid scale type")
        }

        val intervalErr = "Interval not found"

        val rootNoteIndex = allNotes.indexOfFirst {
            if (modifier.isEmpty()) {
                letter == it
            } else {
                val enharmonics = it.split("/")
                note == enharmonics[0] || note == enharmonics[1]
            }
        }

        if (rootNoteIndex == -1) {
            return listOf()
        }

        val intervals = if (scaleType == "major") majorScale.map { intervals[it] ?: error(intervalErr) }
            else minorScale.map { intervals[it] ?: error(intervalErr) }

        return intervals.map {
            val noteIndex = (rootNoteIndex + it) % allNotes.size
            allNotes[noteIndex]
        }
    }

    private fun parseNote(): List<String> {
        val match = noteRe.matchEntire(note) ?: throw IllegalArgumentException("Incompatible format")

        val last = match.groupValues.lastIndex
        return match.groupValues.slice(1..2)
    }


}