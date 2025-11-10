package com.example.financetrackerapplication.features.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.financetrackerapplication.R
import com.example.financetrackerapplication.databinding.FragmentSettingsBinding
import com.example.financetrackerapplication.databinding.SheetAuthLayoutBinding
import com.example.financetrackerapplication.domain.model.UserStatus
import com.example.financetrackerapplication.features.auth.SignInLinkEmailActivity
import com.example.financetrackerapplication.features.settings.categorymanage.list.CategoryActivity
import com.example.financetrackerapplication.utils.Extention.setupStyle
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth

        initView()
        setupListener()
        observer()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun initView() {
//        viewModel.loginGuest()
    }

    private fun setupListener() {
        binding.btnSignin.setOnClickListener { showRegisterOptions() }

        binding.btnLogout.setOnClickListener { viewModel.logout() }

        binding.btnGuest.setOnClickListener {
            val intent = Intent(requireActivity(), CategoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signInWithGoogle() {
        // UI dialog untuk pilih akun Google, passkey, atau password.
        val credentialManager = CredentialManager.create(requireContext())

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(requireContext().getString(R.string.web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    request = request,
                    context = requireContext()
                )
                handleSignInWithGoogle(result)
            } catch (e: GetCredentialException) { //import from androidx.CredentialManager
                Log.d("Error", e.message.toString())
            }
        }
    }

    private fun handleSignInWithGoogle(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    // Process Login dengan Firebase Auth
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                        viewModel.signInWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    private fun observer() {
        // perubahan status user
        viewModel.userStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                UserStatus.LoggedOut -> {
                    Log.d("SettingsActivity", "UI update: Belum login")
                }

                UserStatus.Anonymous -> {
                    Log.d("SettingsActivity", "UI update: Guest")
                }

                UserStatus.LoggedIn -> {
                    Log.d("SettingsActivity", "UI update: Logged-in")
                }
            }
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showRegisterOptions() {
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        val bindingBottomSheet = SheetAuthLayoutBinding.inflate(layoutInflater)
        bottomSheetDialog.apply {
            setContentView(bindingBottomSheet.root)
            setupStyle(16, 16, 16)// setup radius
            show()
        }
        Log.d("Auth", "Show register opstions")

        bindingBottomSheet.apply {
            bottsheetWithGoogle.setOnClickListener {
                Log.d("Auth", "Login dengan Google dipilih")
                signInWithGoogle()
                bottomSheetDialog.dismiss()
            }
            bottsheetWithEmailPass.setOnClickListener {
                Log.d("Auth", "Login dengan Email dipilih")
                bottomSheetDialog.dismiss()
            }
            bottsheetWithPasskey.setOnClickListener {
                Log.d("Auth", "Login dengan Email dipilih")
                val intent = Intent(requireActivity(), SignInLinkEmailActivity::class.java)
                startActivity(intent)
                bottomSheetDialog.dismiss()
            }
        }
    }

    companion object {
        private val TAG = SettingsFragment::class.java.simpleName
    }
}