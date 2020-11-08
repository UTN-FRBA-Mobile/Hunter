package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import com.utn.frba.desarrollomobile.hunter.viewmodel.CreateGameViewModel
import kotlinx.android.synthetic.main.fragment_create_game_step_add_clue.*
import kotlinx.android.synthetic.main.fragment_create_game_step_add_image.*
import kotlinx.android.synthetic.main.fragment_create_game_step_add_image.next_button

class CreateGameFragmentStepAddClue : Fragment(R.layout.fragment_create_game_step_add_clue) {

    private lateinit var gameViewModel: CreateGameViewModel
    private var clue: String? = null
    private var duration: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameViewModel = ViewModelProvider(requireActivity()).get(CreateGameViewModel::class.java)

        setTextWatchers()
        updateUI()

        gameViewModel.getClue().observe(viewLifecycleOwner, Observer { text ->
            clue = text
            updateUI()
        })

        gameViewModel.getDuration().observe(viewLifecycleOwner, Observer { min ->
            duration = min
            updateUI()
        })

        next_button.setOnClickListener {
            showFragment(CreateGameFragmentStepAddLocation(), true)
        }
    }

    private fun setTextWatchers() {
        game_clue_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                /* not implemented */
            }

            override fun afterTextChanged(p0: Editable?) {
                /* not implemented */
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                gameViewModel.setClue(game_clue_edit_text.text.toString())
            }
        })

        duration_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                /* not implemented */
            }

            override fun afterTextChanged(p0: Editable?) {
                /* not implemented */
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var value: String = duration_edit_text.text.toString()

                gameViewModel.setDuration(if(value.isEmpty()) null else value.toInt())
            }
        })
    }

    private fun updateUI() {
        next_button.isEnabled = duration != null && clue != null
    }
}


