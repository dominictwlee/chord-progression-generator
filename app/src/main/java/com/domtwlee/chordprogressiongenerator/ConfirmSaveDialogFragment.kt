package com.domtwlee.chordprogressiongenerator

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.domtwlee.chordprogressiongenerator.database.ChordProgression

class ConfirmSaveDialogFragment : DialogFragment() {
    private val chordGenViewModel: ChordGenViewModel by activityViewModels()
    private val chordProgViewModel: ChordProgressionViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(R.string.save_dialog_title)
                .setView(R.layout.confirm_save_content)
                .setPositiveButton(R.string.confirm_button) { dialog, _ ->
                    val progression = chordGenViewModel.chordProgression
                    chordProgViewModel.insert(
                        ChordProgression(
                            "some cool chords",
                            "Some description",
                            progression.toList(), progression.size))
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.cancel_button) { dialog, _ -> dialog.dismiss()  }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}