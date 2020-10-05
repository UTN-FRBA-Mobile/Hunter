package com.utn.frba.desarrollomobile.hunter.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.utn.frba.desarrollomobile.hunter.LoginFragment
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import com.utn.frba.desarrollomobile.hunter.ui.fragment.AppFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener { firebaseAuth ->
            if(firebaseAuth.currentUser != null) {
                goToApp()
            } else {
                goToLogin()
            }
        }
    }

    private fun goToLogin() {
        supportActionBar?.hide()
        actionBar?.hide()
        showFragment(LoginFragment(), false)
    }

    private fun goToApp() {
        supportActionBar?.show()
        actionBar?.show()
        showFragment(AppFragment(), false)
    }
}
