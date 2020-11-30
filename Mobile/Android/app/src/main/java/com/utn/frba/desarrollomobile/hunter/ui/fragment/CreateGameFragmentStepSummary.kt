package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import com.utn.frba.desarrollomobile.hunter.service.models.Game
import com.utn.frba.desarrollomobile.hunter.utils.PermissionHandler
import com.utn.frba.desarrollomobile.hunter.viewmodel.CreateGameViewModel
import kotlinx.android.synthetic.main.fragment_create_game_step_summary.*

class CreateGameFragmentStepSummary : Fragment(R.layout.fragment_create_game_step_summary) {

    private val REQUEST_WRITE_EXTERNAL_STORAGE = 220
    private lateinit var gameViewModel: CreateGameViewModel
    private var game: Game? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        gameViewModel = ViewModelProvider(requireActivity()).get(CreateGameViewModel::class.java)

        gameViewModel.getGameCreated().observe(viewLifecycleOwner, Observer { game ->
                this.game = game
                updateUI()
        })


        ok_button.setOnClickListener {
            showFragment(ChooseGameFragment(), false, true)
        }

    }

    private fun updateUI() {
        if(this.game != null) {
            try {
                val barcodeEncoder = BarcodeEncoder()
                val bitmap: Bitmap = barcodeEncoder.encodeBitmap(this.game!!.winCode, BarcodeFormat.QR_CODE, 512, 512)
                qr.setImageBitmap(bitmap)

                download_qr.setOnClickListener {
                    if (PermissionHandler.arePermissionsGranted(
                            requireContext(),
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        )
                    ) {
                        downloadQR(game!!)
                    } else {
                        PermissionHandler.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                    }
                }

                join_code.text = resources.getString(R.string.access_code, game?.id)
            } catch (e: Exception) {
                Log.d("Game Summary", e.message)
            }
        } else {
            qr.setBackgroundResource(0)
            download_qr.setOnClickListener(null)
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

    private fun downloadQR(game: Game) {
        val yourBitmap = qr.drawable.toBitmap()
        MediaStore.Images.Media.insertImage(
            requireContext().contentResolver,
            yourBitmap,
            "Hunter_win_qr_${game.id}.jpg",
            "QR del Tesoro ${game.id}"
        )

        Toast.makeText(context, "QR Descargado. Revisá tu carpeta de imágenes.", Toast.LENGTH_LONG)
            .show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            downloadQR(game!!)
        }
    }
}


