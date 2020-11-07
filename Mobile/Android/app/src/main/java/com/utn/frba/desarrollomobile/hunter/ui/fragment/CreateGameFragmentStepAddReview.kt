package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.graphics.Bitmap
import android.location.Location
import android.net.sip.SipSession
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
import kotlinx.android.synthetic.main.dialog_clue_layout.view.*
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
import java.util.*

class CreateGameFragmentStepReview : Fragment(R.layout.fragment_create_game_step_review) {

    private lateinit var gameViewModel: CreateGameViewModel
    private lateinit var storage: FirebaseStorage
    private var game = object {
        var clue: String? = null
        var image: Bitmap? = null
        var duration: Int? = null
        var location: Location? = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storage = Firebase.storage("gs://desarrollo-mobile---hunter.appspot.com")

        updateUI()

        gameViewModel = ViewModelProvider(requireActivity()).get(CreateGameViewModel::class.java)

        gameViewModel.getImage().observe(viewLifecycleOwner, Observer { image ->
            imagePreview.setImageBitmap(image)
            game.image = image
            updateUI()
        })

        gameViewModel.getClue().observe(viewLifecycleOwner, Observer { clue ->
            cluePreview.text = clue
            game.clue = clue
            updateUI()
        })

        gameViewModel.getDuration().observe(viewLifecycleOwner, Observer { duration ->
            durationPreview.text = duration.toString()
            game.duration = duration
            updateUI()
        })

        gameViewModel.getLocation().observe(viewLifecycleOwner, Observer { location ->
            latitudePreview.text = location.latitude.toString()
            longitudePreview.text = location.longitude.toString()
            game.location = location
            updateUI()
        })

        create_game_button.setOnClickListener {
            createGame()
        }
    }

    private fun dataOk(): Boolean {
        return game.clue != null &&
                game.duration != null &&
                game.location != null &&
                game.image != null
    }

    private fun updateUI() {
        create_game_button.isEnabled = dataOk()
    }

    private fun createGame() {
        Log.d("Create Game", "Creando Juego")

        if (!dataOk()) {
            return
        }

        uploadImage(object : ImageUploadListener {
            override fun onSuccess(uri: String) {
                saveNewGame(uri)
            }

            override fun onFailure() {
                TODO("Not yet implemented")
            }
        })
    }

    private fun saveNewGame(imageUrl: String) {
        var callSetGameResponse =
            APIAdapter.getAPI().setGame(
                game.duration!!,
                game.location?.latitude?.toFloat()!!,
                game.location?.longitude?.toFloat()!!,
                arrayOf(game.clue!!),
                emptyArray<Int>(),
                imageUrl
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
                } else {
                    onCreateGameError("Game not found")
                }
            }
        })
    }

    private fun uploadImage(taskListener: ImageUploadListener) {
        Log.d("Create Game", "Subiendo Imagen")
        val id: String = UUID.randomUUID().toString()

        val baos = ByteArrayOutputStream()
        imagePreview.drawable.toBitmap().compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data: ByteArray = baos.toByteArray()

        val ref = storage.reference.child("${id}/clue.jpeg")
        val uploadTask = ref.putBytes(data)


        uploadTask.addOnSuccessListener { _ ->
            val downloadUrl = ref.downloadUrl

            downloadUrl.addOnCompleteListener { task ->
                taskListener.onSuccess(task.result.toString())
            }

            downloadUrl.addOnFailureListener { _ ->
                taskListener.onFailure()
            }
        }

        uploadTask.addOnFailureListener { _ ->
            taskListener.onFailure()
        }
    }

/*    private fun updateGameImage(url: String) {
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
    }*/

    private fun onCreateGameError(msg: String) {
        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
        Log.d("Create Game", msg)
        showFragment(ChooseGameFragment(), false, true)
    }

    private fun onSuccessGameCreated() {
        showFragment(CreateGameFragmentStepSummary(), false, true)
    }
}


interface ImageUploadListener {
    fun onSuccess(uri: String)

    fun onFailure()
}