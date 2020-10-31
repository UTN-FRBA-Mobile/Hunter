package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import kotlinx.android.synthetic.main.fragment_choose_game.*

class ChooseGameFragment: Fragment(R.layout.fragment_choose_game) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        new_game_button.setOnClickListener { createGame() }
        join_game_button.setOnClickListener { joinAGame() }
    }

    private fun createGame() {
        showFragment(CreateGameFragment(), false)
    }

    private fun joinAGame() {
        showFragment(JoinGameFragment(), false)
    }
}