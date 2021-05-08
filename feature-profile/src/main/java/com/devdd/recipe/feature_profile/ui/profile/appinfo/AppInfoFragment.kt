package com.devdd.recipe.feature_profile.ui.profile.appinfo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.base.utils.extensions.debugElseRelease
import com.devdd.recipe.base_android.utils.extensions.buildType
import com.devdd.recipe.feature_profile.R
import com.devdd.recipe.feature_profile.databinding.FragmentAppInfoBinding
import com.devdd.recipe.ui.base.DevFragment
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author Deepak Dawade
 */
@AndroidEntryPoint
class AppInfoFragment : DevFragment<FragmentAppInfoBinding>(R.layout.fragment_app_info) {
    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    private val viewModel: AppInfoViewModel by viewModels()
    override fun onViewCreated(binding: FragmentAppInfoBinding, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        setViewListener(binding)
    }

    private fun setViewListener(binding: FragmentAppInfoBinding) {
        binding.appInfoFragmentToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        buildType.debugElseRelease(
            whenDebugging = {
                binding.appInfoFragmentGuest.setOnClickListener {
                    val cm =
                        context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cm.setPrimaryClip(
                        ClipData.newPlainText(
                            "GuestToken",
                            viewModel.guestToken.value
                        )
                    )
                    Toast.makeText(requireContext(), "GuestToken copied", Toast.LENGTH_SHORT).show()
                }
                binding.appInfoFragmentDeviceid.setOnClickListener {
                    val cm =
                        context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cm.setPrimaryClip(ClipData.newPlainText("FcmToken", viewModel.deviceId.value))
                    Toast.makeText(
                        requireContext(),
                        "Device Installation Id copied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            onProduction = {
                binding.appInfoFragmentUserInfo.isVisible = false
                binding.appInfoFragmentGuest.isVisible = false
                binding.appInfoFragmentDeviceid.isVisible = false
            })
    }
}