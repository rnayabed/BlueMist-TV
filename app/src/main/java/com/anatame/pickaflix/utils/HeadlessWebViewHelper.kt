package com.anatame.pickaflix.common.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.lifecycle.MutableLiveData
import com.anatame.pickaflix.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.InputStream



@SuppressLint("SetJavaScriptEnabled")
class HeadlessWebViewHelper(
    private val epsPlayer: WebView,
    private val vidEmbedURl: String,
    val context: Context
) {

    private var onLoaded:((String) -> Unit)? = null

    init {
        webViewLayerType()
        epsPlayer.settings.userAgentString =
            "Mozilla/5.0 (Linux; Android 7.0; SM-G930V Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.125 Mobile Safari/537.36"
        epsPlayer.settings.javaScriptEnabled = true
        epsPlayer.settings.domStorageEnabled = true
        epsPlayer.settings.cacheMode = WebSettings.LOAD_DEFAULT
        epsPlayer.settings.databaseEnabled = true
        epsPlayer.settings.mediaPlaybackRequiresUserGesture = false
        epsPlayer.settings.loadsImagesAutomatically = false
        epsPlayer.settings.blockNetworkImage = true

        val map = HashMap<String, String>()
        map["referer"] = "https://fmovies.to"

        epsPlayer.loadUrl(vidEmbedURl, map)

        epsPlayer.addJavascriptInterface(
            WebAppInterface(context), "Android")

        epsPlayer.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                epsPlayer.loadUrl(
                    """javascript:(function f() {
                            let myInterval = setInterval(() => {
                            let server = document.querySelector('.server');
                            if(server != null || server != 'undefined'){
                        
                                  document.querySelector('.server').addEventListener('click', function() {
                                     Android.finish();
                                 });
                                   clearInterval(myInterval);
                            }
                        }, 200);
                        
         
          
                      })()""".trimIndent().trimMargin()
                );
            }

            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                Log.d("intercepted", request!!.url.host.toString())
                if (BlockHosts().hosts.contains(request!!.url.host)) {
                    Log.d("interceptedAndblocked", request!!.url.host.toString())
                    val textStream: InputStream = ByteArrayInputStream("".toByteArray())
                    return getTextWebResource(textStream)
                }
                return super.shouldInterceptRequest(view, request)
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                if (url != null) {
                    Log.d("headlessWeb", url)
                    if(url.endsWith("playlist.m3u8")){
                        onLoaded?.let { it(url) }
                    } else if(url.endsWith("list.m3u8")){
                        onLoaded?.let { it(url) }
                    }
                }
            }
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

    fun setOnStreamUrlLoadedListener(listener: (String) -> Unit){
        onLoaded = listener
    }
}

class WebAppInterface(
    private val mContext: Context,
) {

    /** Show a toast from the web page  */
    @JavascriptInterface
    fun finish() {

        (mContext as MainActivity).runOnUiThread {
            Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show()
        }
    }
}