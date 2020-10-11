package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import kotlinx.android.synthetic.main.fragment_create_game.*

class CreateGameFragment: Fragment(R.layout.fragment_create_game) {

    private var photo: ContactsContract.Contacts.Photo? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTextWatchers()
        refreshStatus()
        upload_photo_button.setOnClickListener { takePhoto() }
        create_game_button.setOnClickListener { createGame() }
    }

    private fun setTextWatchers() {
        game_clue_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { /* not implemented */  }
            override fun afterTextChanged(p0: Editable?) { /* not implemented */  }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {  refreshStatus()  }
        })
    }

    private fun refreshStatus() {
        create_game_button.isEnabled = (photo != null)
    }

    private fun takePhoto() {
        TODO("Need to get the photo")
    }

    private fun createGame() {
        val clue = game_clue_edit_text.text
        if (photo != null) {
            TODO("Missing create game service")
            routeToGameCreated()
        }
    }

    private fun routeToGameCreated() {
        TODO("Missing routing")
        showFragment(DummyFragment(), addToBackStack = false)
    }
}