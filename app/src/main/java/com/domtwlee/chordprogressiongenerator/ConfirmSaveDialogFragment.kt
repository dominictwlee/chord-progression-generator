package com.domtwlee.chordprogressiongenerator

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class ConfirmSaveDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val input = EditText(it)
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder
                .setTitle(R.string.save_dialog_title)
                .setView(input)
                .setPositiveButton(R.string.confirm_button) { dialog, _ -> dialog.dismiss() }
                .setNegativeButton(R.string.cancel_button) { dialog, _ -> dialog.dismiss()  }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}