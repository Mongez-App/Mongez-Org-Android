package com.iti.mongez.org.presentation.subscription.checkout

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

class PaymobWebViewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra(EXTRA_URL) ?: run {
            finishWithResult(null)
            return
        }

        setContent {
            WebViewScreen(url = url, onResultUrl = { resultUrl ->
                handleResultUrl(resultUrl)
            })
        }
    }

    private fun handleResultUrl(url: String) {
        val uri = Uri.parse(url)
        val rawFields = HashMap<String, String?>()
        for (key in uri.queryParameterNames) {
            rawFields[key] = uri.getQueryParameter(key)
        }
        finishWithResult(rawFields)
    }

    private fun finishWithResult(rawFields: HashMap<String, String?>?) {
        if (rawFields != null) {
            val data = Intent().apply {
                putExtra(EXTRA_RAW_FIELDS, rawFields)
            }
            setResult(RESULT_OK, data)
        } else {
            setResult(RESULT_CANCELED)
        }
        finish()
    }

    companion object {
        const val EXTRA_URL = "extra_url"
        const val EXTRA_RAW_FIELDS = "extra_raw_fields"
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun WebViewScreen(url: String, onResultUrl: (String) -> Unit) {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        val currentUrl = request?.url?.toString() ?: return false
                        
                        // Paymob appends 'hmac' and 'success' to the return URL when a transaction is processed.
                        // We intercept the redirect to capture these parameters.
                        if (currentUrl.contains("hmac=") && currentUrl.contains("success=")) {
                            onResultUrl(currentUrl)
                            return true
                        }
                        
                        return false
                    }
                }
                loadUrl(url)
            }
        }
    )
}
