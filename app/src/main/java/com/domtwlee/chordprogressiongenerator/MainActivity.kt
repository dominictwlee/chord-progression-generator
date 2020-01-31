package com.domtwlee.chordprogressiongenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private lateinit var model: ChordGenViewModel
    private lateinit var chordGenParams: ChordGenParams
    private lateinit var chordProgression: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.main_toolbar))
        model = ViewModelProvider(this)[ChordGenViewModel::class.java]
        chordGenParams = model.getChordProgParams()
        chordProgression = model.chordProgression

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            val fragment = ChordGenFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.i("CHORD_PROG", chordProgression.isEmpty().toString())

        menuInflater.inflate(R.menu.menu_main, menu)
        val saveButton = menu.getItem(0)
        saveButton.setOnMenuItemClickListener {
            if (chordProgression.isEmpty()) {
                Toast.makeText(this, "Nothing to save", Toast.LENGTH_SHORT).show()
            } else {
                confirmSave()
            }
            true
        }
        return true
    }

    private fun confirmSave() {
        val fragment = ConfirmSaveDialogFragment()
        fragment.show(supportFragmentManager, "confirmSave")
    }
}
