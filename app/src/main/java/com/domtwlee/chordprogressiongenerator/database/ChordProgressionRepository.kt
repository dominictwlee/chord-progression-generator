package com.domtwlee.chordprogressiongenerator.database

import androidx.lifecycle.LiveData

class ChordProgressionRepository(private val chordProgressionDao: ChordProgressionDao) {
    val chordProgressionList: LiveData<List<ChordProgression>> = chordProgressionDao.getChordProgressions()

    suspend fun insert(chordProgression: ChordProgression) {
        chordProgressionDao.insert(chordProgression)
    }
}