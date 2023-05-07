package com.rinoindraw.storybismillah.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.lifecycle.lifecycleScope
import com.rinoindraw.storybismillah.R
import com.rinoindraw.storybismillah.ui.main.MainActivity
import com.rinoindraw.storybismillah.databinding.ActivitySplashBinding
import com.rinoindraw.storybismillah.ui.auth.AuthActivity
import com.rinoindraw.storybismillah.utils.SessionManager
import com.rinoindraw.storybismillah.utils.UiConstValue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!

    private lateinit var pref: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)

        initUserDirection()

    }

    private fun initUserDirection() {
        pref = SessionManager(this)
        val isLogin = pref.isLogin
        Handler(Looper.getMainLooper()).postDelayed({
            when {
                isLogin -> {
                    val imageLogo = findViewById<ImageView>(R.id.imgLogo)
                    imageLogo.alpha = 1f
                    imageLogo.animate().setDuration(1000).alpha(0f).withEndAction {
                        val intentSplash = Intent(this, MainActivity::class.java)
                        startActivity(intentSplash)
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                        finish()
                    }
                }
                else -> {
                    val imageLogo = findViewById<ImageView>(R.id.imgLogo)
                    imageLogo.alpha = 1f
                    imageLogo.animate().setDuration(1000).alpha(0f).withEndAction {
                        val intentSplash = Intent(this, AuthActivity::class.java)
                        startActivity(intentSplash)
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                        finish()
                    }
                }
            }
        }, UiConstValue.LOADING_TIME)
    }
}