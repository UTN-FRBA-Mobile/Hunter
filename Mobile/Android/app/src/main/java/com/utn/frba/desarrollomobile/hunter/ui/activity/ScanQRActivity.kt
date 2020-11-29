package com.utn.frba.desarrollomobile.hunter.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import com.utn.frba.desarrollomobile.hunter.service.APIAdapter
import com.utn.frba.desarrollomobile.hunter.service.models.Game
import com.utn.frba.desarrollomobile.hunter.ui.fragment.GamesAdapter
import com.utn.frba.desarrollomobile.hunter.ui.fragment.WinFragment
import com.utn.frba.desarrollomobile.hunter.utils.LoginHandler
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanQRActivity : AppCompatActivity() {

    var GAMEID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GAMEID = savedInstanceState?.get("game_id").toString().toInt()
        setContentView(R.layout.activity_scan_qr)
        val intentIntegrator = IntentIntegrator(this@ScanQRActivity)
        intentIntegrator.setBeepEnabled(false)
        intentIntegrator.setCameraId(0)
        intentIntegrator.setPrompt("SCAN")
        intentIntegrator.setBarcodeImageEnabled(false)
        intentIntegrator.initiateScan()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val qr = result.contents

                var callWinGameResponse =
                    APIAdapter.getAPI().winGame(GAMEID, qr)

                callWinGameResponse.enqueue(object : Callback<Game> {
                    override fun onFailure(call: Call<Game>, t: Throwable) {
                        print("throw Message" + t.message)
                        register_password_confirmation.error = "Error reading JSON"
                    }

                    override fun onResponse(
                        call: Call<Game>,
                        response: Response<Game>
                    ) {
                        val body = response.body()
                        body?.let {
                            val ended: Boolean = it.ended ?: false
                            val winID: Int = it.winId ?: 0
                            if (ended && winID == LoginHandler.USERID)
                                showFragment(WinFragment(), false)
                            //else
                            //    showFragment()
                        }
                    }
                })
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}