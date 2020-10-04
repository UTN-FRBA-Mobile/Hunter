package com.utn.frba.desarrollomobile.hunter.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.utn.frba.desarrollomobile.hunter.Loginfragment
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import com.utn.frba.desarrollomobile.hunter.ui.fragment.AppFragment
import com.utn.frba.desarrollomobile.hunter.ui.fragment.DummyFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        var auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener { firebaseAuth ->
            if(firebaseAuth.currentUser != null) {
                goToApp()
            } else {
                goToLogin()
            }
        }
    }

    fun goToLogin() {
        supportActionBar?.hide()
        actionBar?.hide()
        showFragment(Loginfragment(), false)
    }

    fun goToApp() {
        supportActionBar?.show()
        actionBar?.show()
        showFragment(AppFragment(), false)
    }
}
