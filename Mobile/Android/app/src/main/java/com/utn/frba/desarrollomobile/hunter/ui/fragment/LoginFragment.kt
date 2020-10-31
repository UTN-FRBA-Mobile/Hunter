package com.utn.frba.desarrollomobile.hunter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import com.utn.frba.desarrollomobile.hunter.ui.fragment.RegisterFragment
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setTextWatchers()

        login_button.setOnClickListener { login() }
        login_register_button.setOnClickListener { goToRegister() }
    }

    private fun goToRegister() {
        showFragment(RegisterFragment(), false)
    }

    private fun setTextWatchers() {
        login_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // not implemented
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // not implemented
                validateInputs()
            }

            override fun afterTextChanged(s: Editable?) {
                //not implemented
            }
        })

        login_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // not implemented
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // not implemented
                validateInputs()
            }

            override fun afterTextChanged(s: Editable?) {
                //not implemented
            }
        })
    }

    private fun validateInputs() {
        val email = login_email.text.toString()
        val pass = login_password.text.toString()

        login_button.isEnabled = email.isNotEmpty() && pass.isNotEmpty()
    }

    private fun login() {
        val email = login_email.text.toString()
        val pass = login_password.text.toString()

        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if(!task.isSuccessful) {
                    Toast.makeText(activity, R.string.login_bad_credentials, Toast.LENGTH_SHORT).show()
                }
            }
    }
}
