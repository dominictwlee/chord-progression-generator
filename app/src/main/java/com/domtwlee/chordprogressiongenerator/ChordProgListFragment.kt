package com.domtwlee.chordprogressiongenerator

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.domtwlee.chordprogressiongenerator.database.ChordProgression
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val TAG = "ChordProgListFragment"

class ChordProgListFragment: Fragment() {

    interface Callbacks {
        fun onAddButtonPress()
    }

    private var callbacks: Callbacks? = null
    private lateinit var chordProgRecyclerView: RecyclerView
    private lateinit var adapter: ChordProgAdapter
    private lateinit var addButton: FloatingActionButton
    private val chordProgressionViewModel: ChordProgressionViewModel by lazy {
        ViewModelProvider(this).get(ChordProgressionViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ChordProgAdapter()

        chordProgressionViewModel.chordProgressionList.observe(this, Observer { chords ->
            chords?.let { adapter.setChordProgs(it) }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chord_prog_list, container, false)

        chordProgRecyclerView = view.findViewById(R.id.chordProgRecyclerView)
        addButton = view.findViewById(R.id.floatingActionAddButton)

        chordProgRecyclerView.layoutManager = LinearLayoutManager(context)
        chordProgRecyclerView.adapter = adapter

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onStart() {
        super.onStart()
        addButton.setOnClickListener {
            callbacks?.onAddButtonPress()
        }
    }

    private inner class ChordProgAdapter : RecyclerView.Adapter<ChordProgHolder>() {

        private var chordProgs = emptyList<ChordProgression>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChordProgHolder {
            val view = layoutInflater.inflate(R.layout.list_item_chord_prog, parent, false)
            return ChordProgHolder(view)
        }

        override fun onBindViewHolder(holder: ChordProgHolder, position: Int) {
            val currentChordProg = chordProgs[position]
            holder.apply {
                nameTextView.text = currentChordProg.name
                descriptionTextView.text = currentChordProg.description
                chords.text = currentChordProg.chords.joinToString(" ")
            }
        }

        internal fun setChordProgs(chordProgs: List<ChordProgression>) {
            this.chordProgs = chordProgs
            notifyDataSetChanged()
        }

        override fun getItemCount() = chordProgs.size
    }

    private inner class ChordProgHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.chordProgName)
        val descriptionTextView: TextView = view.findViewById(R.id.chordProgDescription)
        val chords: TextView = view.findViewById(R.id.chordProg)
    }

    companion object {
        fun newInstance(): ChordProgListFragment {
            return ChordProgListFragment()
        }
    }




}