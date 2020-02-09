package com.domtwlee.chordprogressiongenerator.chordGen

import android.content.Context
import android.graphics.PorterDuff
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.domtwlee.chordprogressiongenerator.*
import java.util.*

private const val TAG = "ChordGenFragment"

class ChordGenFragment : Fragment() {
    private val model: ChordGenViewModel by activityViewModels()
    private lateinit var startSpinner: Spinner
    private lateinit var endSpinner: Spinner
    private lateinit var typeSpinner: Spinner
    private lateinit var lengthEditText: EditText
    private lateinit var chordNumberItems: Array<String>
    private lateinit var scaleTypeItems: Array<String>
    private lateinit var generateButton: Button
    private lateinit var chordProgressionDisplay: TextView
    private lateinit var chordGenParams: ChordGenParams
    private lateinit var saveButton: ImageButton
    private lateinit var playButton: ImageButton
    private var audioAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
        .build()
    private var soundPool = SoundPool.Builder()
        .setAudioAttributes(audioAttributes)
        .setMaxStreams(5)
        .build()
    private val soundIds = mutableMapOf<String, Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ItemSelectedListener.model = model
        chordGenParams = model.getChordProgParams()
        chordGenParams.length
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
        val chordsRes = model.sounds
        for ((chord, chordRes) in chordsRes) {
            soundIds[chord] = soundPool.load(context, chordRes, 1)
        }
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
        saveButton = view.findViewById(R.id.saveButton)
        playButton = view.findViewById(R.id.playButton)
        setSaveButtonVisibility()
    }

    private fun setDefaultFormValues() {
        val chordProgParams = model.getChordProgParams()
        startSpinner.setSelection(chordNumberItems.indexOf(chordProgParams.start.toString()))
        endSpinner.setSelection(chordNumberItems.indexOf(chordProgParams.end.toString()))
        typeSpinner.setSelection(scaleTypeItems.indexOf(chordProgParams.type))
        lengthEditText.setText(chordProgParams.length.toString())
    }

    private fun getSelectItems() {
        chordNumberItems = resources.getStringArray(R.array.chord_number_select)
        scaleTypeItems = resources.getStringArray(R.array.scale_type_select)
    }

    private fun setSaveButtonVisibility() {
        saveButton.isVisible = model.chordProgression.isNotEmpty()
    }

    private fun setListeners() {
        startSpinner.onItemSelectedListener = ItemSelectedListener
        endSpinner.onItemSelectedListener = ItemSelectedListener
        typeSpinner.onItemSelectedListener = ItemSelectedListener
        chordProgressionDisplay.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setSaveButtonVisibility()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        lengthEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrBlank()) {
                    lengthEditText.background.clearColorFilter()
                    val userInputLength = s.toString().toInt()
                    chordGenParams.length = userInputLength
                } else {
                    chordGenParams.length = 0
                    lengthEditText.background.setColorFilter(
                        ContextCompat.getColor(context as Context,
                            R.color.colorAccent
                        ),
                        PorterDuff.Mode.SRC_IN)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        generateButton.setOnClickListener {
            onSubmit()
        }

        playButton.setOnClickListener {
            val chordProgression = model.chordProgression
            chordProgression.forEachIndexed { index, chord ->

                val soundId = soundIds[chord]

                when (index) {
                    0 -> {
                        playSound(soundId)
                    }

                    chordProgression.lastIndex -> {
                        val prevChord = chordProgression[index - 1]
                        val prevSoundId = soundIds[prevChord]
                        val delay = 3000 * index

                        Timer().schedule(object : TimerTask() {
                            override fun run() {
                                stopSound(prevSoundId)
                                playSound(soundId)
                            }
                        }, delay.toLong())

                        Timer().schedule(object : TimerTask() {
                            override fun run() {
                                stopSound(soundId)
                            }
                        }, (delay + 3000).toLong())
                    }

                    else -> {
                        val prevChord = chordProgression[index - 1]
                        val prevSoundId = soundIds[prevChord]
                        val delay = 3000 * index

                        Timer().schedule(object : TimerTask() {
                            override fun run() {
                                stopSound(prevSoundId)
                                playSound(soundId)
                            }
                        }, delay.toLong())
                    }
                }
            }
        }

        saveButton.setOnClickListener {
            onSave()
        }
    }

    private fun playSound(soundId: Int?) {
        if (soundId != null) {
            soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
        }
    }

    private fun stopSound(soundId: Int?) {
        if (soundId != null) {
            soundPool.stop(soundId)
        }
    }

    private fun onSave() {
        val fragment =
            ConfirmSaveDialogFragment()
        fragment.show(childFragmentManager, "startSave")
    }

    private fun onSubmit() {
        model.createChordProgression()
        val chordProgression = model.getChordProg()
        chordProgressionDisplay.text = chordProgression
    }
}