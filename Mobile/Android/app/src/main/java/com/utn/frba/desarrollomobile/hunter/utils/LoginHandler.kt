package com.utn.frba.desarrollomobile.hunter.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.service.APIAdapter
import com.utn.frba.desarrollomobile.hunter.service.MessagingService

object LoginHandler {
    private lateinit var auth: FirebaseAuth

    public var UID = ""

    fun loginComplete()
    {
        var auth = FirebaseAuth.getInstance()
        auth.currentUser?.getIdToken(true)?.addOnCompleteListener { token ->
            APIAdapter.Token = token?.result?.token.toString()
        }

        UID = auth.currentUser?.uid.toString();
        FirebaseMessaging.getInstance().subscribeToTopic(UID);
    }
}