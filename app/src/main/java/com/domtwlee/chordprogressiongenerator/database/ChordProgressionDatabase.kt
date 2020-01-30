package com.domtwlee.chordprogressiongenerator.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ ChordProgression::class ], version = 1)
@TypeConverters(ChordProgressionTypeConverters::class)
abstract class ChordProgressionDatabase : RoomDatabase() {
    abstract fun chordProgressionDao(): ChordProgressionDao

    companion object {
        @Volatile
        private var INSTANCE: ChordProgressionDatabase? = null

        fun getDatabase(context: Context): ChordProgressionDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChordProgressionDatabase::class.java,
                    "chord_progression_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}