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
import com.devdd.recipe.ui.databinding.DialogLoginViaEmailBinding
import com.devdd.recipe.ui.databinding.FragmentLoginBinding
import com.devdd.recipe.ui.utils.extensions.bindWithLayout
import com.devdd.recipe.ui.utils.extensions.createMaterialAlertDialog
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

    private var resultLauncher: ActivityResultLauncher<Intent>? = null

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

        binding.loginFragmentSigninEmailButton.setOnClickListener {
            loginViaEmailAndPassWord(true)
        }

        binding.loginFragmentRegisterButton.setOnClickListener {
            loginViaEmailAndPassWord(false)
        }

        binding.loginFragmentSigninLater.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun loginViaEmailAndPassWord(login: Boolean) {
        viewModel.clearFields()
        val dialogBinding = bindWithLayout<DialogLoginViaEmailBinding>(
            R.layout.dialog_login_via_email,
            layoutInflater
        ) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@LoginFragment.viewModel
        }
        val resId =
            if (login) R.string.login_via_email_cta_login else R.string.login_via_email_cta_register
        dialogBinding.dialogLoginViaEmailLoginButton.text = getString(resId)

        val dialog = requireContext().createMaterialAlertDialog(
            viewBinding = dialogBinding,
            cancelable = false
        ).also {
            it.show()
        }

        dialogBinding.dialogLoginViaEmailLoginButton.setOnClickListener {
            viewModel.handleCtaClick(login)
            dialog.dismiss()
        }

        dialogBinding.dialogLoginViaEmailClose.setOnClickListener {
            dialog.dismiss()
        }

        viewModel.emailLoginSuccess.observeEvent(viewLifecycleOwner) {
            dialog.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        resultLauncher = null
    }
}