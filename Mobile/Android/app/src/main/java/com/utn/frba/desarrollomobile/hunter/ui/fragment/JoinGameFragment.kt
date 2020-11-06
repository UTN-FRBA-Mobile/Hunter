package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import com.utn.frba.desarrollomobile.hunter.ui.fragment.BaseLocationFragment.Companion.GAME_ID
import com.utn.frba.desarrollomobile.hunter.viewmodel.GameViewModel
import kotlinx.android.synthetic.main.fragment_choose_game.join_game_button
import kotlinx.android.synthetic.main.fragment_join_game.*

class JoinGameFragment: Fragment(R.layout.fragment_join_game) {

    private lateinit var gameViewModel: GameViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTextWatchers()
        refreshStatus()
        join_game_button.setOnClickListener { searchGame() }

        gameViewModel = ViewModelProvider(requireActivity()).get(GameViewModel::class.java)
    }

    private fun setTextWatchers() {
        join_code_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { /* not implemented */  }
            override fun afterTextChanged(p0: Editable?) { /* not implemented */  }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {  refreshStatus()  }
        })
    }

    private fun refreshStatus() {
        join_game_button.isEnabled = join_code_edit_text.text.isNotEmpty()
    }

    private fun searchGame() {
        val gameCode = join_code_edit_text.text

        showFragment(MapFragment().apply {
            arguments = Bundle().apply { putInt(GAME_ID, gameCode.toString().toInt()) }
        }, true)
    }

    private fun routeToGame() {
        TODO("Missing routing")
//        showFragment(DummyFragment(), addToBackStack = false)
    }
}