package com.domtwlee.chordprogressiongenerator

import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders

class MainActivity : AppCompatActivity() {
    private lateinit var model: ChordProgViewModel
    private lateinit var startSpinner: Spinner
    private lateinit var endSpinner: Spinner
    private lateinit var typeSpinner: Spinner
    private lateinit var lengthEditText: EditText
    private lateinit var chordNumberItems: Array<String>
    private lateinit var scaleTypeItems: Array<String>
    private lateinit var generateButton: Button
    private lateinit var chordProgressionDisplay: TextView
    private var isSaveButtonVisible = false
    private lateinit var chordProgParams: ChordProgParams

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ItemSelectedListener.model = ViewModelProviders.of(this)[ChordProgViewModel::class.java]
        model = ViewModelProviders.of(this)[ChordProgViewModel::class.java]
        chordProgParams = model.getChordProgParams()
        chordProgParams.length
        getSelectItems()
        initWidgets()
        setListeners()
        setDefaultFormValues()
        val note = Note("A")
        Log.i("NOTE", note.toScale("major").joinToString(","))
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

    private fun initWidgets() {
        startSpinner = findViewById(R.id.startValueDropdown)
        endSpinner = findViewById(R.id.endValueDropdown)
        typeSpinner = findViewById(R.id.typeValueDropdown)
        lengthEditText = findViewById(R.id.lengthEditText)
        generateButton = findViewById(R.id.generateButton)
        chordProgressionDisplay = findViewById(R.id.chordProgressionDisplay)
        setSupportActionBar(findViewById(R.id.main_toolbar))
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
                    chordProgParams.length = userInputLength
                } else {
                    chordProgParams.length = 0
                    lengthEditText.background.setColorFilter(
                        ContextCompat.getColor(applicationContext, R.color.colorAccent),
                        PorterDuff.Mode.SRC_IN)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        chordProgressionDisplay.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isSaveButtonVisible = !s.isNullOrBlank()
                invalidateOptionsMenu()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        generateButton.setOnClickListener {
            onSubmit()
        }
    }

    fun confirmSave() {
        val fragment = ConfirmSaveDialogFragment()
        fragment.show(supportFragmentManager, "confirmSave")
    }

    private fun onSubmit() {
        model.createChordProgression()
        val chordProgression = model.getChordProg()
        chordProgressionDisplay.text = chordProgression
    }






}
