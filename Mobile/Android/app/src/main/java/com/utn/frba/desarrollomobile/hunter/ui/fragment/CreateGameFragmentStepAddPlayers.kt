package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import com.utn.frba.desarrollomobile.hunter.service.APIAdapter
import com.utn.frba.desarrollomobile.hunter.service.models.Game
import com.utn.frba.desarrollomobile.hunter.service.models.User
import com.utn.frba.desarrollomobile.hunter.viewmodel.CreateGameViewModel
import kotlinx.android.synthetic.main.fragment_create_game_step_add_clue.*
import kotlinx.android.synthetic.main.fragment_create_game_step_add_image.*
import kotlinx.android.synthetic.main.fragment_create_game_step_add_image.next_button
import kotlinx.android.synthetic.main.fragment_create_game_step_add_players.*
import kotlinx.android.synthetic.main.fragment_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class CreateGameFragmentStepAddPlayers : Fragment(R.layout.fragment_create_game_step_add_players) {

    private lateinit var gameViewModel: CreateGameViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameViewModel = ViewModelProvider(requireActivity()).get(CreateGameViewModel::class.java)

        next_button.setOnClickListener {
            showFragment(CreateGameFragmentStepSummary(), true)
        }

        search_button.setOnClickListener {
            searchPlayer(search_box.text.toString().orEmpty())
        }
    }


    fun searchPlayer(name: String) {
        val user = User()
        user.mail = name
        var callFindUserResponse = APIAdapter.createConection()?.findUser(user)

        callFindUserResponse?.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                //print("throw Message" + t.message)
                //register_password_confirmation.error = "Error reading JSON"
            }

            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                val body = response?.body()
                if (body != null) {
                    Log.d("Debug", body.toString())
                }
            }
        })
    }
}


