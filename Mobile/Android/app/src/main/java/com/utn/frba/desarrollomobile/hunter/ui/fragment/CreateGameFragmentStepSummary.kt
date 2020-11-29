package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import com.utn.frba.desarrollomobile.hunter.service.APIAdapter
import com.utn.frba.desarrollomobile.hunter.service.models.Game
import com.utn.frba.desarrollomobile.hunter.ui.activity.MainActivity
import com.utn.frba.desarrollomobile.hunter.viewmodel.CreateGameViewModel
import kotlinx.android.synthetic.main.fragment_create_game_step_add_clue.*
import kotlinx.android.synthetic.main.fragment_create_game_step_summary.*
import kotlinx.android.synthetic.main.fragment_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class CreateGameFragmentStepSummary : Fragment(R.layout.fragment_create_game_step_summary) {

    private lateinit var gameViewModel: CreateGameViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        gameViewModel = ViewModelProvider(requireActivity()).get(CreateGameViewModel::class.java)

//        gameViewModel.getImage().observe(viewLifecycleOwner, Observer { image ->
//            imagePreview.setImageBitmap(image)
//        })
//
//        gameViewModel.getClue().observe(viewLifecycleOwner, Observer { text ->
//            clue.text = text
//        })

        gameViewModel.getGameCreated().observe(viewLifecycleOwner, Observer { game ->
                updateUI(game)
        })


        ok_button.setOnClickListener {
            showFragment(ChooseGameFragment(), false, true)
        }

    }

    private fun updateUI(game: Game?) {
        if(game != null) {
            try {
                val barcodeEncoder = BarcodeEncoder()
                val bitmap: Bitmap = barcodeEncoder.encodeBitmap(game.winCode, BarcodeFormat.QR_CODE, 512, 512)
                qr.setImageBitmap(bitmap)

                download_qr.setOnClickListener {
                    val storageDir: File? = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val file = File.createTempFile("win_qr_${game.id}_", ".jpg", storageDir)

                    val fOut = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                    fOut.flush()
                    fOut.close()

                }

                join_code.text = "El codigo para unirse es: ${game.id}"
            } catch (e: Exception) {
                Log.d("Game Summary", e.message)
            }
        } else {
            join_code.text = "No hay juego creado"
            qr.setBackgroundResource(0)
            download_qr.setOnClickListener(null)
        }
    }

}


