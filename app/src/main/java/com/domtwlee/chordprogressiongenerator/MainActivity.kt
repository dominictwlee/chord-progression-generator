package com.domtwlee.chordprogressiongenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu

class MainActivity : AppCompatActivity() {
    private var isSaveButtonVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.main_toolbar))
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
        super.onCreateOptionsMenu(menu)
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
        val fragment = ConfirmSaveDialogFragment()
        fragment.show(supportFragmentManager, "confirmSave")
    }
}
