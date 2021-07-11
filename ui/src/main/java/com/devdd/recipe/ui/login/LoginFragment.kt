package com.devdd.recipe.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.base.utils.Logger
import com.devdd.recipe.base_android.utils.extensions.observeEvent
import com.devdd.recipe.ui.R
import com.devdd.recipe.ui.base.DevFragment
import com.devdd.recipe.ui.databinding.FragmentLoginBinding
import com.devdd.recipe.utils.extensions.navigateOnce
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : DevFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var googleSignClient: GoogleSignInClient
    private val viewModel: LoginViewModel by viewModels()

    var resultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                        account.idToken?.let { viewModel.loggedIn(it) }
                    } catch (e: ApiException) {
                        logger.e(e)
                    }
                }
            }
    }

    override fun onViewCreated(binding: FragmentLoginBinding, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        setListeners(binding)
        setObservers()
    }

    private fun setObservers() {
        viewModel.loginSuccess.observeEvent(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
    }

    private fun setListeners(binding: FragmentLoginBinding) {
        binding.loginFragmentSigninGoogleButton.setOnClickListener {
            val signInIntent = googleSignClient.signInIntent
            resultLauncher?.launch(signInIntent)
        }

        binding.loginFragmentSigninLater.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        resultLauncher = null
    }
}