package com.utn.frba.desarrollomobile.hunter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.utn.frba.desarrollomobile.hunter.service.APIAdapter
import com.utn.frba.desarrollomobile.hunter.service.models.User
import kotlinx.android.synthetic.main.fragment_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

        register_password_confirmation.addTextChangedListener(object : TextWatcher {
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

        if (pass != pass_confirm) {
            register_password_confirmation.error = getString(R.string.register_password_not_match)
        }

        register_button.isEnabled = email.isNotEmpty() && pass.isNotEmpty()
                && (pass == pass_confirm) && alias.isNotEmpty()
    }

    private fun SaveUserData(user: User) {
        var callSetUserResponse =
            APIAdapter.createConection()?.setUser(user)

        callSetUserResponse?.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                print("throw Message" + t.message)
                register_password_confirmation.error = "Error reading JSON"
            }

            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                val body = response?.body()
                if (body != null) {
                    //do your work
                }
            }
        })
    }

    private fun register() {
        val pass = register_password.text.toString()

        val user = User()
        user.mail = register_email.text.toString()
        user.alias = register_alias.text.toString()
        user.firstName = register_first_name.text.toString()
        user.lastName = register_last_name.text.toString()

        auth.createUserWithEmailAndPassword(user.mail.toString(), pass)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Toast.makeText(activity, R.string.regiter_error, Toast.LENGTH_SHORT).show()

                } else {
                    auth.currentUser?.getIdToken(true)?.addOnCompleteListener { token ->
                        APIAdapter.Token = token?.result?.token.toString()
                        SaveUserData(user)
                    }
                }
            }
    }
}
