package com.devdd.recipe.ui.profile.devoption.datastoresetting

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.devdd.recipe.R
import com.devdd.recipe.ui.base.DevFragment
import com.devdd.recipe.databinding.FragmentDataStoreSettingBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by @author Deepak Dawade on 4/15/2021 at 12:04 AM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
@AndroidEntryPoint
class DataStoreSettingFragment :
    DevFragment<FragmentDataStoreSettingBinding>(R.layout.fragment_data_store_setting) {
    companion object {
        private val TAG: String = this::class.java.simpleName
    }

    private val viewModel: DataStoreViewModel by viewModels()
    private var adapter: ArrayAdapter<String>? = null
    override fun onViewCreated(
        binding: FragmentDataStoreSettingBinding,
        savedInstanceState: Bundle?
    ) {
        binding.viewModel = viewModel
        setSpinnerAdapter(binding)
        setListeners()
        setObservers()
    }

    private fun setSpinnerAdapter(binding: FragmentDataStoreSettingBinding) {
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        binding.dataStoreSettingFragmentSelectKeySpinner.adapter = adapter
    }

    private fun setObservers() {
        viewModel.prefNames.observe(viewLifecycleOwner) {
            adapter?.clear()
            adapter?.addAll(it)
        }
    }

    private fun setListeners() {
        binding?.dataStoreSettingFragmentSelectKeySpinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.getKeyAtPosition(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
    }
}