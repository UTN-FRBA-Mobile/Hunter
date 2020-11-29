package com.utn.frba.desarrollomobile.hunter.ui.activity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.utn.frba.desarrollomobile.hunter.LoginFragment
import com.utn.frba.desarrollomobile.hunter.R
import com.utn.frba.desarrollomobile.hunter.extensions.showFragment
import com.utn.frba.desarrollomobile.hunter.service.APIAdapter
import com.utn.frba.desarrollomobile.hunter.ui.customviews.LoadingView
import com.utn.frba.desarrollomobile.hunter.ui.fragment.ChooseGameFragment
import com.utn.frba.desarrollomobile.hunter.utils.LoginHandler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var loading: LoadingView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolbar()

        loading = LoadingView.create(fragment_container.rootView as ViewGroup, this)

        val auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener { firebaseAuth ->
            hideLoading()
            if(firebaseAuth.currentUser != null) {
                LoginHandler.loginComplete()
                Log.d("DEBUG", APIAdapter.Token)
            // ----
                goToApp()
            } else {
                goToLogin()
            }
        }


        NotificationManagerCompat.from(this).cancelAll()
    }

    private fun setToolbar() {

        hunterToolbar.setNavigationIcon(R.drawable.ic_arrow_back) //todo poner el asset correspondiente

        setSupportActionBar(hunterToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        hunterToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun setToolbarTitle(title: String) {
        hunterToolbarText.text = title
    }

    private fun goToLogin() {
        showFragment(LoginFragment(), false)
    }

    private fun goToApp() {
        showFragment(ChooseGameFragment(), false)
    }

    fun hideLoading() {
        loading.dismiss()
    }

    fun showLoading(message: String) {
        loading.show(message)
    }
}
