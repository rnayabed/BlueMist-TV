package com.anatame.pickaflix.ui.detail.handler.dataHandlers

import android.util.Log
import android.view.View
import com.anatame.pickaflix.MainActivity
import com.anatame.pickaflix.common.utils.HeadlessWebViewHelper
import com.anatame.pickaflix.ui.detail.models.EpisodeItem
import com.anatame.pickaflix.ui.detail.models.MovieDetails
import com.anatame.pickaflix.ui.detail.models.SeasonItem

class DetailHandlerImpl(
    private val webHelper: HeadlessWebViewHelper.Instance
): AbstractDetailHandler() {

    override fun getStreamUrl(url: String) {
        webHelper.loadUrl(url)
        webHelper.setOnStreamUrlLoadedListener{
            detailHandlerListener.onVideoUrlLoaded(it)
        }
    }

}