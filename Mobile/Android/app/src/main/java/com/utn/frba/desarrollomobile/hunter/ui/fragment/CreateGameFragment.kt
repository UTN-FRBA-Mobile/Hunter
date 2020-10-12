package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import kotlinx.android.synthetic.main.fragment_create_game.*
import java.io.ByteArrayOutputStream
import java.io.IOException

class CreateGameFragment : Fragment(R.layout.fragment_create_game) {

    private lateinit var storage: FirebaseStorage
    private val PICK_IMAGE_REQUEST = 100
    private val TAKE_PICTURE__REQUEST = 101

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTextWatchers()
        refreshStatus()
        upload_image_button.setOnClickListener { selectImage() }
        take_photo_button.setOnClickListener { takePhoto() }
        create_game_button.setOnClickListener { createGame() }
        storage = Firebase.storage("gs://desarrollo-mobile---hunter.appspot.com")
    }

    private fun setTextWatchers() {
        game_clue_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) { /* not implemented */
            }

            override fun afterTextChanged(p0: Editable?) { /* not implemented */
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                refreshStatus()
            }
        })
    }

    private fun refreshStatus() {
        create_game_button.isEnabled = (imagePreview.tag != "0")
    }

    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            startActivityForResult(takePictureIntent, TAKE_PICTURE__REQUEST)
        }
    }

    private fun selectImage() {
        val intent = Intent()
            .setType("image/*")
            .setAction(Intent.ACTION_GET_CONTENT)

        startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_IMAGE_REQUEST)
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
    }

    private fun routeToGameCreated() {
        TODO("Missing routing")
        showFragment(DummyFragment(), addToBackStack = false)
    }

    private fun loadImage(image: Bitmap) {
        try {
            imagePreview.setImageBitmap(image)
            imagePreview.tag = "1"
            refreshStatus()
        } catch (e: IOException) {
            Toast.makeText(activity, R.string.upload_image_error, Toast.LENGTH_SHORT)
                .show()
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
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(activity?.contentResolver, filePath)
                    loadImage(bitmap)
                }
                TAKE_PICTURE__REQUEST -> {
                    if (data == null || data.extras == null) {
                        return
                    }

                    val bitmap = data.extras?.get("data") as Bitmap
                    loadImage(bitmap)
                }
            }
        }
    }

}


