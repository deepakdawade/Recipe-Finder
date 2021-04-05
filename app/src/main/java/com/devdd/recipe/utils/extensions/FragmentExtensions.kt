package com.devdd.recipe.utils.extensions

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.devdd.recipe.utils.NavigationResult
import kotlin.properties.Delegates

inline fun <reified T : AppCompatActivity> Fragment.launch(
    noinline with: (Intent.() -> Intent) = { Intent() }
) {
    with(Intent()).setClass(requireContext(), T::class.java).also {
        startActivity(it)
    }
}

inline fun <T : ViewDataBinding> Fragment.bindingWithLifecycleOwner(
    inflater: LayoutInflater,
    @LayoutRes layoutId: Int,
    container: ViewGroup?,
    bind: (T.() -> Unit) = {}
): T {
    val binding: T = DataBindingUtil.inflate(inflater, layoutId, container, false)
    binding.lifecycleOwner = viewLifecycleOwner
    binding.bind()
    return binding
}


fun Fragment.dispatchResultOnDismiss(result: Bundle) {
    val childFragmentManager: FragmentManager? = parentFragment?.childFragmentManager
    (childFragmentManager?.primaryFragment() as? NavigationResult)?.onNavigationResult(result)
}

fun Fragment.navigateBackWithResult(result: Bundle) {
    val childFragmentManager: FragmentManager? = parentFragment?.childFragmentManager

    var backStackListener: FragmentManager.OnBackStackChangedListener by Delegates.notNull()
    backStackListener = FragmentManager.OnBackStackChangedListener {
        (childFragmentManager?.primaryFragment() as? NavigationResult)?.onNavigationResult(result)
        childFragmentManager?.removeOnBackStackChangedListener(backStackListener)
    }
    childFragmentManager?.addOnBackStackChangedListener(backStackListener)
    lifecycleScope.launchWhenResumed {
        findNavController().popBackStack()
    }
}

inline fun <reified T> Fragment.callerFragment(
    callerFragmentType: CallerFragmentType,
    position: Int
): T? {
    return when (callerFragmentType) {
        CallerFragmentType.PARENT_ACTIVITY -> activity as? T
        CallerFragmentType.PARENT_FRAGMENT -> parentFragmentManager.primaryFragment() as? T
        CallerFragmentType.VIEW_PAGER_FRAGMENT -> parentFragmentManager.primaryFragment()?.childFragmentManager?.primaryFragment(
            position
        ) as? T
        CallerFragmentType.BOTTOM_NAVIGATION -> parentFragmentManager.primaryFragment() // get parent fragment which contains bottom navigation (i.e DASHBOARD FRAGMENT)
            ?.childFragmentManager?.primaryFragment() // get parent fragment navHostFragment
            ?.childFragmentManager?.primaryFragment() as? T // get navHostFragment current fragment
    }
}

fun Fragment.requireNavHostParentFragment(): Fragment =
    requireParentFragment().requireParentFragment()

fun FragmentManager.primaryFragment(position: Int = 0): Fragment? = fragments.getOrNull(position)

fun Fragment.hideSoftInput(): Unit = requireActivity().hideSoftInput()


