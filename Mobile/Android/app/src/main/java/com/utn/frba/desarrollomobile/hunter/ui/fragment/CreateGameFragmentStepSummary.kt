package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.google.android.gms.maps.GoogleMap
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import com.utn.frba.desarrollomobile.hunter.service.models.Game
import com.utn.frba.desarrollomobile.hunter.viewmodel.CreateGameViewModel
import kotlinx.android.synthetic.main.fragment_create_game_step_summary.*
import java.io.File
import java.io.FileOutputStream

class CreateGameFragmentStepSummary : Fragment(R.layout.fragment_create_game_step_summary) {

    private  val REQUEST_WRITE_EXTERNAL_STORAGE = 100
    private lateinit var gameViewModel: CreateGameViewModel
    private var game: Game? = null

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
                download_qr.setOnClickListener {
                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_WRITE_EXTERNAL_STORAGE)
                    }

                    downloadQR(this.game!!)
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
        val barcodeEncoder = BarcodeEncoder()
        val bitmap: Bitmap = barcodeEncoder.encodeBitmap(game.winCode, BarcodeFormat.QR_CODE, 512, 512)
        qr.setImageBitmap(bitmap)

        val storageDir: File? = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File.createTempFile("win_qr_${game.id}_", ".jpg", storageDir)

        val fOut = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
        fOut.flush()
        fOut.close()

        Toast.makeText(context, "QR Descagado. Revisá tu directorio de descargas.", Toast.LENGTH_LONG).show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_WRITE_EXTERNAL_STORAGE -> {
                downloadQR(this.game!!)
            }
        }
    }
}


