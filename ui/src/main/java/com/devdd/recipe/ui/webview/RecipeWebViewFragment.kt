package com.devdd.recipe.ui.webview

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.devdd.recipe.ui.R
import com.devdd.recipe.ui.base.DevFragment
import com.devdd.recipe.ui.databinding.FragmentRecipeWebViewBinding
import com.devdd.recipe.ui.utils.extensions.launchDefaultBrowser

class RecipeWebViewFragment :
    DevFragment<FragmentRecipeWebViewBinding>(R.layout.fragment_recipe_web_view) {
    private val args: RecipeWebViewFragmentArgs by navArgs()

    override fun onViewCreated(binding: FragmentRecipeWebViewBinding, savedInstanceState: Bundle?) {
        binding.recipeWebViewFragmentWebView.apply {
            settings.javaScriptEnabled = true
            webViewClient = CustomWebClient(this)
            webChromeClient = CustomChromeClient()
            settings.domStorageEnabled = true
            settings.setAppCacheEnabled(true)
            addJavascriptInterface(RecipeWebViewInterface(), "RecipeAndroid")
            settings.setAppCachePath(requireContext().applicationContext.filesDir.absolutePath + "/cache")
            overScrollMode = WebView.OVER_SCROLL_NEVER
            loadUrl(args.url)
        }
    }

    inner class RecipeWebViewInterface {
        @JavascriptInterface
        fun back() {
            binding?.recipeWebViewFragmentWebView?.post {
                requireActivity().onBackPressed()
            }
        }
    }

    inner class CustomChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            binding?.recipeWebViewFragmentProgress?.progress = newProgress
        }
    }

    inner class CustomWebClient(private val webView: WebView) :
        WebViewClient() {

        init {
            binding?.recipeWebViewFragmentProgress?.isVisible = true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            binding?.recipeWebViewFragmentProgress?.isVisible = true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding?.recipeWebViewFragmentProgress?.isVisible = false
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (url == null) return false
            val uriFromUrl = Uri.parse(url)
            val scheme = uriFromUrl.scheme
            if (scheme == "https") {
                webView.loadUrl(url)
            } else requireContext().launchDefaultBrowser(url)
            return true
        }

    }
}
