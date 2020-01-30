package com.domtwlee.chordprogressiongenerator.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chord_progression_table")
data class ChordProgression(@PrimaryKey val name: String, val description: String, val chords: List<String>, val length: Int)
