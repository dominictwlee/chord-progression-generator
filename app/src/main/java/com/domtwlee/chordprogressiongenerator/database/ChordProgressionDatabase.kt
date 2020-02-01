package com.domtwlee.chordprogressiongenerator.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [ ChordProgression::class ], version = 1)
@TypeConverters(ChordProgressionTypeConverters::class)
abstract class ChordProgressionDatabase : RoomDatabase() {

    abstract fun chordProgressionDao(): ChordProgressionDao

    private class ChordProgressionDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.chordProgressionDao())
                }
            }
        }

        suspend fun populateDatabase(chordProgressionDao: ChordProgressionDao) {
            // Clear and populate database
            chordProgressionDao.deleteAll()
            var chordList = listOf("I", "I", "I", "v")
            var chordProgression = ChordProgression("Blues chords", "bluesy feel", chordList, chordList.size)
            chordProgressionDao.insert(chordProgression)
            chordList = listOf("I7", "IV7", "viii7", "IV7")
            chordProgression = ChordProgression("Jazzy chords", "jazzy feel", chordList, chordList.size)
            chordProgressionDao.insert(chordProgression)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ChordProgressionDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ChordProgressionDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChordProgressionDatabase::class.java,
                    "chord_progression_database"
                )
                    .addCallback(ChordProgressionDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}