package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import com.utn.frba.desarrollomobile.hunter.viewmodel.CreateGameViewModel
import kotlinx.android.synthetic.main.fragment_create_game_step_add_image.*
import java.io.IOException

class CreateGameFragmentStepAddImage : Fragment(R.layout.fragment_create_game_step_add_image) {

    private lateinit var gameViewModel: CreateGameViewModel
    private val PICK_IMAGE_REQUEST = 100
    private val TAKE_PICTURE__REQUEST = 101
    private val REQUEST_READ_EXTERNAL_STORAGE = 102


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameViewModel = ViewModelProvider(requireActivity()).get(CreateGameViewModel::class.java)

        upload_image_button.setOnClickListener { selectImage() }
        take_photo_button.setOnClickListener { takePhoto() }
        next_button.setOnClickListener {
            showFragment(CreateGameFragmentStepReview(), true)
        }


        gameViewModel.getImage().observe(viewLifecycleOwner, Observer { image ->
            refreshStatus(image)
        })

        refreshStatus(gameViewModel.getImage().value)
    }


    private fun refreshStatus(image: Bitmap?) {
        if(image != null) {
            loadImage(image)
        }

        next_button.isEnabled = image != null
    }

    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            startActivityForResult(takePictureIntent, TAKE_PICTURE__REQUEST)
        }
    }

    private fun selectImage() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_READ_EXTERNAL_STORAGE)
        }

        Intent(Intent.ACTION_GET_CONTENT).also { getContentIntent ->
            getContentIntent.type = "image/*"
            startActivityForResult(Intent.createChooser(getContentIntent, "Select a file"), PICK_IMAGE_REQUEST)
        }
    }

    private fun loadImage(image: Bitmap) {
        try {
            imagePreview.setImageBitmap(image)
        } catch (e: IOException) {
            Toast.makeText(activity, R.string.upload_image_error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    if (data == null || data.data == null) {
                        return
                    }

                    val filePath = data.data
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, filePath)
                    gameViewModel.setImage(bitmap)
                }
                TAKE_PICTURE__REQUEST -> {
                    if (data == null || data.extras == null) {
                        return
                    }

                    val bitmap = data.extras?.get("data") as Bitmap
                    gameViewModel.setImage(bitmap)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_READ_EXTERNAL_STORAGE -> {
                Toast.makeText(activity, "Permiso concedido", Toast.LENGTH_LONG).show()
            }
        }
    }

}


