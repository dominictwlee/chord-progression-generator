package com.domtwlee.chordprogressiongenerator

import android.view.View
import android.widget.AdapterView

object ItemSelectedListener : AdapterView.OnItemSelectedListener {
    lateinit var model: ChordProgViewModel
    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val spinnerId = parent.id
        val selectedItem = parent.getItemAtPosition(position)
        val chordProgParams = model.getChordProgParams()

        when (spinnerId) {
            R.id.startValueDropdown -> chordProgParams.start = selectedItem.toString().toInt()
            R.id.endValueDropdown -> chordProgParams.end = selectedItem.toString().toInt()
            R.id.typeValueDropdown -> chordProgParams.type = selectedItem.toString()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}