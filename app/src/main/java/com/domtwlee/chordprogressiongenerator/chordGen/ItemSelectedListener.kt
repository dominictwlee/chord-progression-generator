package com.domtwlee.chordprogressiongenerator.chordGen

import android.view.View
import android.widget.AdapterView
import com.domtwlee.chordprogressiongenerator.R
import java.util.*

object ItemSelectedListener : AdapterView.OnItemSelectedListener {
    lateinit var model: ChordGenViewModel
    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val spinnerId = parent.id
        val selectedItem = parent.getItemAtPosition(position)
        val chordProgParams = model.getChordProgParams()

        when (spinnerId) {
            R.id.startValueDropdown -> chordProgParams.start = selectedItem.toString().toInt()
            R.id.endValueDropdown -> chordProgParams.end = selectedItem.toString().toInt()
            R.id.typeValueDropdown -> chordProgParams.type = selectedItem.toString().toLowerCase(
                Locale.getDefault())
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}