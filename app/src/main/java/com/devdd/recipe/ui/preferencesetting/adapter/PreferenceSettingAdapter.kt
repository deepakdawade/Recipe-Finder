package com.devdd.recipe.ui.preferencesetting.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.devdd.recipe.ui.preferencesetting.language.LanguagePreferenceFragment
import com.devdd.recipe.ui.preferencesetting.recipepref.RecipePreferenceFragment

class PreferenceSettingAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val pages: List<Fragment> =
        listOf(LanguagePreferenceFragment(), RecipePreferenceFragment())

    override fun getItemCount(): Int = pages.size

    override fun createFragment(position: Int): Fragment {
        return pages[position]
    }
}