package com.domtwlee.chordprogressiongenerator

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class ConfirmSaveDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(R.string.save_dialog_title)
                .setView(R.layout.confirm_save_content)
                .setPositiveButton(R.string.confirm_button) { dialog, _ -> dialog.dismiss() }
                .setNegativeButton(R.string.cancel_button) { dialog, _ -> dialog.dismiss()  }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}