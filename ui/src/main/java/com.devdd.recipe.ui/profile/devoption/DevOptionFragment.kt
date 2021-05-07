package com.devdd.recipe.ui.profile.devoption

import android.os.Bundle
import com.devdd.recipe.ui.R
import com.devdd.recipe.ui.base.DevFragment
import com.devdd.recipe.ui.databinding.FragmentDevOptionBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by @author Deepak Dawade on 4/14/2021 at 8:36 PM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
@AndroidEntryPoint
class DevOptionFragment :
    DevFragment<FragmentDevOptionBinding>(R.layout.fragment_dev_option) {
    override fun onViewCreated(
        binding: FragmentDevOptionBinding,
        savedInstanceState: Bundle?
    ) {

    }
}