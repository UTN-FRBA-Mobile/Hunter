package com.utn.frba.desarrollomobile.hunter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setTextWatchers()

        register_button.setOnClickListener { register() }
    }

    private fun setTextWatchers() {
        register_email.addTextChangedListener(object : TextWatcher {
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

        register_password.addTextChangedListener(object : TextWatcher {
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
        val email = register_email.text.toString()
        val pass = register_password.text.toString()
        val pass_confirm = register_password_confirmation.text.toString()
        val alias = register_alias.text.toString()

        if (pass != pass_confirm)
        {
            register_password_confirmation.error = getString(R.string.register_password_not_match)
        }

        register_button.isEnabled = email.isNotEmpty() && pass.isNotEmpty()
                && (pass == pass_confirm) && alias.isNotEmpty()
    }

    private fun register() {
        val email = register_email.text.toString()
        val pass = register_password.text.toString()

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if(!task.isSuccessful) {
                    Toast.makeText(activity, R.string.regiter_error, Toast.LENGTH_SHORT).show()

                }
            }

        //aca hay que mandar al backend los datos
    }
}
