package com.domtwlee.chordprogressiongenerator

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class ChordGenFragment : Fragment() {
    private lateinit var model: ChordGenViewModel
    private lateinit var startSpinner: Spinner
    private lateinit var endSpinner: Spinner
    private lateinit var typeSpinner: Spinner
    private lateinit var lengthEditText: EditText
    private lateinit var chordNumberItems: Array<String>
    private lateinit var scaleTypeItems: Array<String>
    private lateinit var generateButton: Button
    private lateinit var chordProgressionDisplay: TextView
    private lateinit var chordGenParams: ChordGenParams
    private lateinit var appContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ItemSelectedListener.model = ViewModelProvider(this)[ChordGenViewModel::class.java]
        model = ViewModelProvider(this)[ChordGenViewModel::class.java]
        chordGenParams = model.getChordProgParams()
        chordGenParams.length
        val note = Note("A")
        Log.i("NOTE", note.toScale("major").joinToString(","))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chord_gen, container, false)
        initWidgets(view)
        getSelectItems()
        setDefaultFormValues()
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appContext = context
    }

    override fun onStart() {
        super.onStart()
        setListeners()
    }

    private fun initWidgets(view: View) {
        startSpinner = view.findViewById(R.id.startValueDropdown)
        endSpinner = view.findViewById(R.id.endValueDropdown)
        typeSpinner = view.findViewById(R.id.typeValueDropdown)
        lengthEditText = view.findViewById(R.id.lengthEditText)
        generateButton = view.findViewById(R.id.generateButton)
        chordProgressionDisplay = view.findViewById(R.id.chordProgressionDisplay)
    }

    private fun setDefaultFormValues() {
        val chordProgParams = model.getChordProgParams()
        startSpinner.setSelection(chordNumberItems.indexOf(chordProgParams.start.toString()))
        endSpinner.setSelection(chordNumberItems.indexOf(chordProgParams.end.toString()))
        typeSpinner.setSelection(scaleTypeItems.indexOf(chordProgParams.type))
        lengthEditText.setText(chordProgParams.length.toString())
        model.createChordProgression()
    }

    private fun getSelectItems() {
        chordNumberItems = resources.getStringArray(R.array.chord_number_select)
        scaleTypeItems = resources.getStringArray(R.array.scale_type_select)
    }

    private fun setListeners() {
        startSpinner.onItemSelectedListener = ItemSelectedListener
        endSpinner.onItemSelectedListener = ItemSelectedListener
        typeSpinner.onItemSelectedListener = ItemSelectedListener
        lengthEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrBlank()) {
                    lengthEditText.background.clearColorFilter()
                    val userInputLength = s.toString().toInt()
                    chordGenParams.length = userInputLength
                } else {
                    chordGenParams.length = 0
                    lengthEditText.background.setColorFilter(
                        ContextCompat.getColor(appContext, R.color.colorAccent),
                        PorterDuff.Mode.SRC_IN)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
//        chordProgressionDisplay.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                isSaveButtonVisible = !s.isNullOrBlank()
//                invalidateOptionsMenu()
//            }
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })
        generateButton.setOnClickListener {
            onSubmit()
        }
    }

    private fun onSubmit() {
        model.createChordProgression()
        val chordProgression = model.getChordProg()
        chordProgressionDisplay.text = chordProgression
    }
}