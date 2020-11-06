package com.utn.frba.desarrollomobile.hunter.ui.fragment

import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import kotlinx.android.synthetic.main.fragment_app.*


class AppFragment : Fragment(R.layout.fragment_app) {

    var auth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()

        email_text.text = auth.currentUser?.email.toString()

        sub_text.text = auth.currentUser?.uid

        log_out.setOnClickListener {
            auth.signOut()
        }

        compassButton.setOnClickListener {
            showFragment(MapFragment(), true)
        }

        chooseGameButoon.setOnClickListener {
            showFragment(ChooseGameFragment(), true)
        }
    }
}