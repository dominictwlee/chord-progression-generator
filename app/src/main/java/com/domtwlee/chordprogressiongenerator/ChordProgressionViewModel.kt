package com.domtwlee.chordprogressiongenerator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.domtwlee.chordprogressiongenerator.database.ChordProgression
import com.domtwlee.chordprogressiongenerator.database.ChordProgressionDatabase
import com.domtwlee.chordprogressiongenerator.database.ChordProgressionRepository
import kotlinx.coroutines.launch

//data class MockChordProgression(val name: String, val description: String, val chords: List<String>, val length: Int)

class ChordProgressionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ChordProgressionRepository
    val chordProgressionList: LiveData<List<ChordProgression>>
//    val chordProgressionList: List<MockChordProgression> = listOf(
//        MockChordProgression("sweet chords", "some sweet chords", listOf("I", "II", "iii"), 3),
//        MockChordProgression("bad chords", "some bad chords", listOf("I", "II", "iii"), 3),
//        MockChordProgression("lame chords", "some lame chords", listOf("I", "II", "iii"), 3)
//    )

    init {
        val chordProgressionDao = ChordProgressionDatabase.getDatabase(application, viewModelScope).chordProgressionDao()
        repository = ChordProgressionRepository(chordProgressionDao)
        chordProgressionList = repository.chordProgressionList
    }

    fun insert(chordProgression: ChordProgression) = viewModelScope.launch {
        repository.insert(chordProgression)
    }
}