package com.example.ordermyt

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity

@Composable
fun FeedbackScreen(context: Activity) {
    Scaffold(topBar = {

        AppBar()
    }) {
        Box(modifier = Modifier.padding(it)) {
            AndroidView(factory = {
                WebView(context).apply {
                    getSettings().javaScriptEnabled = true
                    /*
                    mywebview.webViewClient = object : WebViewClient() {
           override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
               if (url.startsWith("intent://")) {
                   val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                   if (intent != null) {
                       val fallbackUrl = intent.getStringExtra("browser_fallback_url")
                       return if (fallbackUrl != null) {
                           webView.loadUrl(fallbackUrl)
                           true
                       } else {
                           false
                       }
                   }
               }
               return false
           }
       }
                     */
                    loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSeUCVnetsJQSxQmpXS8jo3wuLt1MxtgLq3Fv9NeAV-zyghaQA/viewform")
                }

            }, update = {}, modifier = Modifier.fillMaxSize())
        }
    }



}

@Composable
@Preview(showBackground = true)
fun FeedbackScreenPreview() {
    FeedbackScreen(context = null!!)
}