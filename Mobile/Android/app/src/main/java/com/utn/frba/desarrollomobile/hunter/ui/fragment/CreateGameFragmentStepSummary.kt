package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.google.android.gms.maps.GoogleMap
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import com.utn.frba.desarrollomobile.hunter.viewmodel.CreateGameViewModel
import kotlinx.android.synthetic.main.fragment_create_game_step_summary.*
import kotlinx.android.synthetic.main.fragment_map.*

class CreateGameFragmentStepSummary : Fragment(R.layout.fragment_create_game_step_summary) {

    private lateinit var gameViewModel: CreateGameViewModel

    private lateinit var googleMap: GoogleMap

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //map.onCreate(savedInstanceState)

        gameViewModel = ViewModelProvider(requireActivity()).get(CreateGameViewModel::class.java)

//        gameViewModel.getImage().observe(viewLifecycleOwner, Observer { image ->
//            imagePreview.setImageBitmap(image)
//        })
//
//        gameViewModel.getClue().observe(viewLifecycleOwner, Observer { text ->
//            clue.text = text
//        })

        gameViewModel.getGameCreated().observe(viewLifecycleOwner, Observer { game ->
            try {
                val barcodeEncoder = BarcodeEncoder()
                val bitmap: Bitmap = barcodeEncoder.encodeBitmap(game.winCode, BarcodeFormat.QR_CODE, 512, 512)
                qr.setImageBitmap(bitmap)
            } catch (e: Exception) {
                Log.d("Game Summary", e.message)
            }
        })

        ok_button.setOnClickListener {
            showFragment(ChooseGameFragment(), false, true)
        }

        var invitation = resources.getText(R.string.game_invitation)
        var gameId = gameViewModel.getId().toString()
        share_button.setOnClickListener {
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "$invitation $gameId")
                type = "text/plain"
            }
            startActivity(shareIntent)
        }
    }
}


