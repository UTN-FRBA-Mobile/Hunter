package com.utn.frba.desarrollomobile.hunter.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import com.utn.frba.desarrollomobile.hunter.ui.fragment.DummyFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showFragment(DummyFragment(), false)
    }
}
