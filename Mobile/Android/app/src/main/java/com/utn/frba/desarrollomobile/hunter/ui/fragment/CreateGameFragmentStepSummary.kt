package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.service.APIAdapter
import com.utn.frba.desarrollomobile.hunter.service.models.Game
import com.utn.frba.desarrollomobile.hunter.viewmodel.CreateGameViewModel
import kotlinx.android.synthetic.main.fragment_create_game_step_add_clue.*
import kotlinx.android.synthetic.main.fragment_create_game_step_summary.*
import kotlinx.android.synthetic.main.fragment_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class CreateGameFragmentStepSummary : Fragment(R.layout.fragment_create_game_step_summary) {

    private lateinit var gameViewModel: CreateGameViewModel
    private lateinit var storage: FirebaseStorage
    private val PICK_IMAGE_REQUEST = 100
    private val TAKE_PICTURE__REQUEST = 101
    private val REQUEST_READ_EXTERNAL_STORAGE = 102
    private var game_id = 0;

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
    }


    private fun uploadImage() {
        val baos = ByteArrayOutputStream()
        imagePreview.drawable.toBitmap().compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data: ByteArray = baos.toByteArray()

        val ref = storage.reference.child("dirA/clue.png")
        val uploadTask = ref.putBytes(data)

        uploadTask.addOnSuccessListener { _ ->
            Toast.makeText(
                activity,
                "Archivo listo",
                Toast.LENGTH_SHORT
            ).show()
        }

        uploadTask.addOnFailureListener { _ ->
            Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createGame() {
        uploadImage()
        var callSetGameResponse =
            APIAdapter.getAPI().setGame(
                20,
                0.toFloat(),
                0.toFloat(),
                arrayOf<String>(game_clue_edit_text.text.toString()),
                emptyArray<Int>(),
                "")

        callSetGameResponse.enqueue(object : Callback<Game> {
            override fun onFailure(call: Call<Game>, t: Throwable) {
                print("throw Message" + t.message)
                register_password_confirmation.error = "Error reading JSON"
            }

            override fun onResponse(
                call: Call<Game>,
                response: Response<Game>
            ) {
                val body = response?.body()
                if (body != null) {
                    game_id = body.id
                    //do your work
                }
            }
        })
    }
}


