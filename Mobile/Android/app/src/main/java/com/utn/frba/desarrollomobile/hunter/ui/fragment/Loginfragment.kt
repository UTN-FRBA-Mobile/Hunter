package com.utn.frba.desarrollomobile.hunter

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import kotlin.reflect.typeOf


class Loginfragment : Fragment(R.layout.fragment_login) {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        login_button.setOnClickListener { _ -> this.login() }
    }

    private fun login() {
        var email = login_email.text.toString()
        var pass = login_password.text.toString()

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(activity, R.string.login_mail_missing, Toast.LENGTH_SHORT).show()
            return
        }

        if(TextUtils.isEmpty(pass)) {
            Toast.makeText(activity, R.string.login_pass_missing, Toast.LENGTH_SHORT).show()
            return
        }
        
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener() { task ->
                if(!task.isSuccessful) {
                    Toast.makeText(activity, R.string.login_bad_credentials, Toast.LENGTH_SHORT).show()
                }
            }
    }
}
