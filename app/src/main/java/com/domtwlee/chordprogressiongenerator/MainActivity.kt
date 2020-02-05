package com.domtwlee.chordprogressiongenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), ChordProgListFragment.Callbacks, ConfirmSaveDialogFragment.Callbacks {
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
            val fragment = ChordProgListFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onSubmitSave() {
        isSaveButtonVisible = false
        invalidateOptionsMenu()
        val fragment = ChordProgListFragment()
        Log.d(TAG, "ON BUTTON PRESS")
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onAddButtonPress() {
        val fragment = ChordGenFragment()
        Log.d(TAG, "ON BUTTON PRESS")
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        val saveButton = menu.getItem(0)
//        saveButton.isVisible = isSaveButtonVisible
//        saveButton.setOnMenuItemClickListener {
//            confirmSave()
//            true
//        }
//        return true
//    }

}
