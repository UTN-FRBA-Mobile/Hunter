package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.utn.frba.desarrollomobile.hunter.BuildConfig
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import com.utn.frba.desarrollomobile.hunter.viewmodel.CreateGameViewModel
import kotlinx.android.synthetic.main.fragment_create_game_step_add_image.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CreateGameFragmentStepAddImage : Fragment(R.layout.fragment_create_game_step_add_image) {

    private lateinit var gameViewModel: CreateGameViewModel
    private val PICK_IMAGE_REQUEST = 100
    private val TAKE_PICTURE__REQUEST = 101
    private val REQUEST_READ_EXTERNAL_STORAGE = 102
    private var photoFile: File? = null

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
        if (image != null) {
            loadImage(image)
        }

        next_button.isEnabled = image != null
    }

    private fun takePhoto() {
        try {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                    photoFile = createImageFile()

                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            requireContext(),
                            "${BuildConfig.APPLICATION_ID}.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, TAKE_PICTURE__REQUEST)
                    }
                }
            }
        } catch (ex: Exception) {
            Log.d("Take Image", ex.message ?: "Error al capturar la foto")
            showFragment(ChooseGameFragment(), false, true)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    private fun selectImage() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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

    //@RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    if (data == null || data.data == null) {
                        return
                    }
                    val filePath = data.data

                    if (filePath != null) {
                        BitmapFactory.decodeFile(filePath.path, BitmapFactory.Options())?.also { bitmap ->
                            gameViewModel.setImage(bitmap)
                        }
                    }
                }
                TAKE_PICTURE__REQUEST -> {
                    Log.d("Take Image", data?.extras.toString() ?: "Nada de data")

                    if (photoFile != null) {
                        BitmapFactory.decodeFile(photoFile?.absolutePath, BitmapFactory.Options())?.also { bitmap ->
                            gameViewModel.setImage(bitmap)
                            photoFile?.delete()
                        }
                    }
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


