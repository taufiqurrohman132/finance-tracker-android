package com.example.financetrackerapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.financetrackerapplication.databinding.ActivityMainBinding
import com.example.financetrackerapplication.domain.model.UserStatus
import com.example.financetrackerapplication.features.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val settingViewModel: SettingsViewModel by viewModels()
    private val sharedViewModel: MainSharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        binding.navView.setupWithNavController(navController)

        observer()

        // bottom action



    }

    fun showActionMenu(show: Boolean, selectedItemId: Int) {
        val bottomNav = binding.navView
        val navIsVisible = bottomNav.menu.findItem(R.id.navigation_aset) != null

        if (show) {
            if (!navIsVisible) return
            bottomNav.apply {
                menu.clear()
                inflateMenu(R.menu.menu_bottom_nav_action)
                isItemActiveIndicatorEnabled = false
                setItemTextAppearanceActiveBoldEnabled(false)

            }

            binding.navView.menu.apply {
                findItem(R.id.action_delete_list).setOnMenuItemClickListener {
                    sharedViewModel.onBottomAction(Action.DELETE)
                    false
                }

                findItem(R.id.action_pin_list).setOnMenuItemClickListener {
                    sharedViewModel.onBottomAction(Action.PIN)
                    false
                }
            }

        } else {
            if (navIsVisible) return
            bottomNav.apply {
                menu.clear()
                inflateMenu(R.menu.bottom_nav)
                this.selectedItemId = selectedItemId

                isItemActiveIndicatorEnabled = true
                setItemTextAppearanceActiveBoldEnabled(true)

            }

        }
    }


    private fun observer() {
        settingViewModel.userStatus.observe(this) { state ->


            when (state) {
                UserStatus.LoggedOut -> {
                    Log.d("MainActivity", "UI update: Belum login")
                }

                UserStatus.Anonymous -> {
                    Log.d("MainActivity", "UI update: Guest")
                    settingViewModel.loginGuest()
                }

                UserStatus.LoggedIn -> {
                    Log.d("MainActivity", "UI update: Logged-in")

                }
            }
        }
    }
}