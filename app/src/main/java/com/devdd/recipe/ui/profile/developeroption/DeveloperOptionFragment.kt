package com.devdd.recipe.ui.profile.developeroption

import android.os.Bundle
import com.devdd.recipe.R
import com.devdd.recipe.base.DevFragment
import com.devdd.recipe.databinding.FragmentDeveloperOptionBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * Created by @author Deepak Dawade on 4/14/2021 at 8:36 PM.
 * Copyright (c) 2021 deepak.dawade.dd1@gmail.com All rights reserved.
 *
 */
@AndroidEntryPoint
class DeveloperOptionFragment :
    DevFragment<FragmentDeveloperOptionBinding>(R.layout.fragment_developer_option) {
    override fun onViewCreated(
        binding: FragmentDeveloperOptionBinding,
        savedInstanceState: Bundle?
    ) {

    }
}