package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import com.utn.frba.desarrollomobile.hunter.service.APIAdapter
import com.utn.frba.desarrollomobile.hunter.service.models.Game
import com.utn.frba.desarrollomobile.hunter.ui.activity.MainActivity
import com.utn.frba.desarrollomobile.hunter.viewmodel.CreateGameViewModel
import kotlinx.android.synthetic.main.fragment_create_game_step_add_clue.*
import kotlinx.android.synthetic.main.fragment_create_game_step_review.*
import kotlinx.android.synthetic.main.fragment_create_game_step_summary.*
import kotlinx.android.synthetic.main.fragment_create_game_step_summary.clue
import kotlinx.android.synthetic.main.fragment_create_game_step_summary.imagePreview
import kotlinx.android.synthetic.main.fragment_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class CreateGameFragmentStepReview : Fragment(R.layout.fragment_create_game_step_review) {

    private lateinit var gameViewModel: CreateGameViewModel
    private lateinit var storage: FirebaseStorage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storage = Firebase.storage("gs://desarrollo-mobile---hunter.appspot.com")

        gameViewModel = ViewModelProvider(requireActivity()).get(CreateGameViewModel::class.java)

        gameViewModel.getImage().observe(viewLifecycleOwner, Observer { image ->
            imagePreview.setImageBitmap(image)
        })

        gameViewModel.getClue().observe(viewLifecycleOwner, Observer { text ->
            clue.text = text
        })

        create_game_button.setOnClickListener {
            createGame()
        }
    }

    private fun createGame() {
        Log.d("Create Game", "Creando Juego")
        create_game_button.isEnabled = false
        var callSetGameResponse =
            APIAdapter.getAPI().setGame(
                20,
                0.toFloat(),
                0.toFloat(),
                arrayOf<String>(clue.text.toString()),
                emptyArray<Int>(),
                ""
            )

        callSetGameResponse.enqueue(object : Callback<Game> {
            override fun onFailure(call: Call<Game>, t: Throwable) {
                onCreateGameError(t.message.orEmpty())
            }

            override fun onResponse(
                call: Call<Game>,
                response: Response<Game>
            ) {
                val body = response?.body()
                if (body != null) {
                    gameViewModel.setGameCreated(body)
                    uploadImage()
                } else {
                    onCreateGameError("Game not found")
                }
            }
        })
    }

    private fun onCreateGameError(msg: String) {
        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
        Log.d("Create Game", msg)
        showFragment(ChooseGameFragment(), false, true)
    }

    private fun uploadImage() {
        Log.d("Create Game", "Subiendo Imagen")
        val game: Game? = gameViewModel.getGameCreated()

        if (game != null) {
            val gameId = game.id.toString()

            val baos = ByteArrayOutputStream()
            imagePreview.drawable.toBitmap().compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data: ByteArray = baos.toByteArray()

            val ref = storage.reference.child("${gameId}/clue.jpeg")
            val uploadTask = ref.putBytes(data)

            uploadTask.addOnSuccessListener { _ ->
                onSuccesImageUpload(ref)
            }

            uploadTask.addOnFailureListener { err ->
                onCreateGameError(err.message.orEmpty())
            }
        } else {
            onCreateGameError("Game not found")
        }
    }

    private fun onSuccesImageUpload(ref: StorageReference) {
        val downloadUrl = ref.downloadUrl

        downloadUrl.addOnCompleteListener { task ->
            updateGameImage(task.result.toString())
        }

        downloadUrl.addOnFailureListener { err ->
            onCreateGameError(err.message.orEmpty())
        }
    }

    private fun updateGameImage(url: String) {
        Log.d("Create Game", "Actualizando Juego")
        val game: Game? = gameViewModel.getGameCreated()
        if (game != null) {
            var callSetGameResponse =
                APIAdapter.getAPI().setGame(
                    20,
                    game.latitude,
                    game.longitude,
                    game.clues,
                    game.userIds,
                    url
                )

            callSetGameResponse.enqueue(object : Callback<Game> {
                override fun onFailure(call: Call<Game>, t: Throwable) {
                    onCreateGameError(t.message.orEmpty())
                }

                override fun onResponse(call: Call<Game>, response: Response<Game>) {
                    val body = response?.body()
                    if (body != null) {
                        gameViewModel.setGameCreated(body)
                        onSuccessGameCreated()
                    } else {
                        onCreateGameError("Game not found")
                    }
                }
            })
        }
    }

    private fun onSuccessGameCreated() {
        showFragment(CreateGameFragmentStepSummary(), false, true)
    }
}


