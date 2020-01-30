package com.domtwlee.chordprogressiongenerator.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ChordProgressionDao {

    @Query("SELECT * from chord_progression_table ORDER BY name ASC")
    fun getChordProgressions(): LiveData<List<ChordProgression>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(chordProgression: ChordProgression)

    @Query("DELETE FROM chord_progression_table")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteChordProgression(chordProgression: ChordProgression)
}