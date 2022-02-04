package com.anatame.pickaflix.common.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Toast
import com.anatame.pickaflix.MainActivity
import java.io.ByteArrayInputStream
import java.io.InputStream



@SuppressLint("SetJavaScriptEnabled")
class HeadlessWebViewHelper(
    private val epsPlayer: WebView,
    val context: Context
) {

    private var onLoaded:((String) -> Unit)? = null
    private var map: HashMap<String, String> = HashMap<String, String>()

    init {
        map["referer"] = "https://hdtoday.tv/"
        Log.d("webviewInit", "initialized")
    }

    fun initView() : Instance{
        webViewLayerType()
        epsPlayer.settings.userAgentString =
            "Mozilla/5.0 (Linux; Android 7.0; SM-G930V Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.36"
        epsPlayer.settings.javaScriptEnabled = true
        epsPlayer.settings.domStorageEnabled = true
        epsPlayer.settings.cacheMode = WebSettings.LOAD_DEFAULT
        epsPlayer.settings.databaseEnabled = true
        epsPlayer.settings.mediaPlaybackRequiresUserGesture = false

        epsPlayer.webViewClient = object : WebViewClient() {

            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                if (BlockHosts().hosts.contains(request!!.url.host)) {
                    val textStream: InputStream = ByteArrayInputStream("".toByteArray())
                    return getTextWebResource(textStream)
                }
                return super.shouldInterceptRequest(view, request)
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                if (url != null) {
                    if(url.endsWith("playlist.m3u8")){
                        Log.d("streamUrlgg", url)
                        onLoaded?.let { it(url) }
                    }
                }
            }
        }

        return Instance()

    }

    inner class Instance(){

        fun loadUrl(link: String, javascriptEnabled: Boolean = true){
            Log.d("webviewcalled", link)
            epsPlayer.settings.javaScriptEnabled = javascriptEnabled
            epsPlayer.loadUrl(link, map)
        }

        fun setOnStreamUrlLoadedListener(listener: (String) -> Unit){
            onLoaded = listener
        }
    }

    private fun getTextWebResource(data: InputStream): WebResourceResponse {
        return WebResourceResponse("text/plain", "UTF-8", data);
    }

    private fun webViewLayerType() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            epsPlayer.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            epsPlayer.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }
}

