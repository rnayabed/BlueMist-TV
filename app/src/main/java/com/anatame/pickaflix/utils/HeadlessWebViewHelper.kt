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
        map["referer"] = "https://fmovies.to"
        Log.d("webviewInit", "initialized")
    }

    fun initView() : Instance{
        Log.d("webviewInit", "initialized")
        webViewLayerType()
        epsPlayer.settings.userAgentString =
            "Mozilla/5.0 (platform; rv:geckoversion) Gecko/geckotrail Firefox/firefoxversion"
        epsPlayer.settings.javaScriptEnabled = true
        epsPlayer.settings.domStorageEnabled = true
        epsPlayer.settings.cacheMode = WebSettings.LOAD_DEFAULT
        epsPlayer.settings.databaseEnabled = true
        epsPlayer.settings.mediaPlaybackRequiresUserGesture = false
        epsPlayer.settings.loadsImagesAutomatically = false
        epsPlayer.settings.blockNetworkImage = true


        epsPlayer.addJavascriptInterface(
            WebAppInterface(context), "Android")

        epsPlayer.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                epsPlayer.loadUrl(
                    """javascript:(function f() {
                            let myInterval = setInterval(() => {
                            let server = document.querySelectorAll('.server');
                            if(server.length != 0){
                    
                            server.forEach((item)=>{
                                item.addEventListener('click', function() {
                                 Android.finish();
                                });
                            });
                          
                               clearInterval(myInterval);
                            }
                        }, 200);
                        
                         let playInterval = setInterval(() => {
                            let play = document.querySelector("iframe");
                            if(play != null || play != 'undefined'){
                               Android.finish();
                              play.click();
                                play.click();
                                  play.click();
                                   Android.finish();
                               clearInterval(playInterval);
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
                } else{
                    Log.d("AndNotblocked", request!!.url.host.toString())
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
                        epsPlayer.settings.javaScriptEnabled = false
                        epsPlayer.reload()
                    }
                }
            }
        }


        return Instance()

    }

    inner class Instance(){

        fun loadUrl(link: String, javascriptEnabled: Boolean = true){
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

class WebAppInterface(
    private val mContext: Context,
) {

    @JavascriptInterface
    fun iframe(frame: String) {
        Log.d("frameData", frame)
        (mContext as MainActivity).runOnUiThread {

        }
    }

    /** Show a toast from the web page  */
    @JavascriptInterface
    fun finish() {

        (mContext as MainActivity).runOnUiThread {
            Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show()
        }
    }
}