package com.utn.frba.desarrollomobile.hunter.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import com.utn.frba.desarrollomobile.hunter.R
import kotlinx.android.synthetic.main.fragment_create_game.*

class CreateGameFragment : Fragment(R.layout.fragment_create_game) {

    private var photo: ContactsContract.Contacts.Photo? = null
    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTextWatchers()
        refreshStatus()
        upload_photo_button.setOnClickListener { takePhoto() }
        create_game_button.setOnClickListener { createGame() }
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
        create_game_button.isEnabled = (photo != null)
    }

    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            context?.packageManager?.let {
                takePictureIntent.resolveActivity(it)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun createGame() {
        val clue = game_clue_edit_text.text
        if (photo != null) {
            TODO("Missing create game service")
            routeToGameCreated()
        }
    }

    private fun routeToGameCreated() {
        TODO("Missing routing")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            clue_Image.setImageBitmap(imageBitmap)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}