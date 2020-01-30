package com.domtwlee.chordprogressiongenerator.database

import androidx.room.TypeConverter

class ChordProgressionTypeConverters {

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString(separator = ",")
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return value.split(Regex(","))
    }
}