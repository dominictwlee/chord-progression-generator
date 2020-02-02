package com.domtwlee.chordprogressiongenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity(), ChordGenFragment.Callbacks {
    private lateinit var chordGenModel: ChordGenViewModel
    private lateinit var chordProgModel: ChordProgressionViewModel
    private lateinit var chordGenParams: ChordGenParams
    private lateinit var chordProgression: MutableList<String>
    private var isSaveButtonVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.main_toolbar))
        chordGenModel = ViewModelProvider(this)[ChordGenViewModel::class.java]
        chordProgModel = ViewModelProvider(this)[ChordProgressionViewModel::class.java]
        chordGenParams = chordGenModel.getChordProgParams()
        chordProgression = chordGenModel.chordProgression

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            val fragment = ChordGenFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onChordProgChanged(s: Editable?) {
        isSaveButtonVisible = !s.isNullOrEmpty()
        invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val saveButton = menu.getItem(0)
        saveButton.isVisible = isSaveButtonVisible
        saveButton.setOnMenuItemClickListener {
            confirmSave()
            true
        }
        return true
    }

    private fun confirmSave() {
        val fragment = ChordProgListFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
//        val fragment = ConfirmSaveDialogFragment()
//        fragment.show(supportFragmentManager, "confirmSave")
    }
}
