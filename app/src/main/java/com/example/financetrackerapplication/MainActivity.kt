package com.example.financetrackerapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.financetrackerapplication.databinding.ActivityMainBinding
import com.example.financetrackerapplication.domain.model.UserStatus
import com.example.financetrackerapplication.ui.settings.SettingsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        binding.navView.setupWithNavController(navController)

        observer()


    }

    private fun observer(){
        viewModel.userStatus.observe(this){ state ->


            when (state) {
                UserStatus.LoggedOut -> {
                    Log.d("MainActivity", "UI update: Belum login")
                }

                UserStatus.Anonymous -> {
                    Log.d("MainActivity", "UI update: Guest")
                    viewModel.loginGuest()
                }

                UserStatus.LoggedIn -> {
                    Log.d("MainActivity", "UI update: Logged-in")

                }
            }
        }
    }
}