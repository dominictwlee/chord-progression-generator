package com.domtwlee.chordprogressiongenerator.chordProg

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.domtwlee.chordprogressiongenerator.database.ChordProgression
import com.domtwlee.chordprogressiongenerator.database.ChordProgressionDatabase
import com.domtwlee.chordprogressiongenerator.database.ChordProgressionRepository
import kotlinx.coroutines.launch

class ChordProgressionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ChordProgressionRepository
    val chordProgressionList: LiveData<List<ChordProgression>>

    init {
        val chordProgressionDao = ChordProgressionDatabase.getDatabase(application, viewModelScope).chordProgressionDao()
        repository = ChordProgressionRepository(chordProgressionDao)
        chordProgressionList = repository.chordProgressionList
    }

    fun insert(chordProgression: ChordProgression) = viewModelScope.launch {
        repository.insert(chordProgression)
    }

    fun remove(chordProgression: ChordProgression) = viewModelScope.launch {
        repository.delete(chordProgression)
    }
}