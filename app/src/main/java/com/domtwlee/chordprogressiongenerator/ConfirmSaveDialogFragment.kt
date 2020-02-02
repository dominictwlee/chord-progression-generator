package com.domtwlee.chordprogressiongenerator

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.domtwlee.chordprogressiongenerator.database.ChordProgression

private val TAG = "ConfirmSaveDialogFragment"

class ConfirmSaveDialogFragment : DialogFragment() {
    private val chordGenViewModel: ChordGenViewModel by activityViewModels()
    private val chordProgViewModel: ChordProgressionViewModel by activityViewModels()
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText

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
                            nameEditText.text.toString(),
                            descriptionEditText.text.toString(),
                            progression.toList(), progression.size))
                    progression.clear()
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.cancel_button) { dialog, _ -> dialog.dismiss()  }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onStart() {
        super.onStart()
        nameEditText = dialog!!.findViewById(R.id.nameEditText)
        descriptionEditText = dialog!!.findViewById(R.id.descriptionEditText)
    }
}