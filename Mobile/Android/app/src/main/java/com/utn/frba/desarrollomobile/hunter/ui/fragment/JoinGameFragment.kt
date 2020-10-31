package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import kotlinx.android.synthetic.main.fragment_choose_game.*
import kotlinx.android.synthetic.main.fragment_choose_game.join_game_button
import kotlinx.android.synthetic.main.fragment_join_game.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.w3c.dom.Text

class JoinGameFragment: Fragment(R.layout.fragment_join_game) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTextWatchers()
        refreshStatus()
        join_game_button.setOnClickListener { searchGame() }
    }

    private fun setTextWatchers() {
        join_code_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { /* not implemented */  }
            override fun afterTextChanged(p0: Editable?) { /* not implemented */  }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {  refreshStatus()  }
        })
    }

    private fun refreshStatus() {
        join_game_button.isEnabled = join_code_edit_text.text.length > 4
    }

    private fun searchGame() {
        val gameCode = join_code_edit_text.text
        TODO("Missing join game service")
    }

    private fun routeToGame() {
        TODO("Missing routing")
        showFragment(DummyFragment(), addToBackStack = false)
    }
}