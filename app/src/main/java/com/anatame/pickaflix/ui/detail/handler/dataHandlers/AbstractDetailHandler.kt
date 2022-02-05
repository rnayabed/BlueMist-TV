package com.anatame.pickaflix.ui.detail.handler.dataHandlers

import android.util.Log
import android.view.View
import com.anatame.pickaflix.MainActivity
import com.anatame.pickaflix.common.utils.HeadlessWebViewHelper
import com.anatame.pickaflix.ui.detail.models.EpisodeItem
import com.anatame.pickaflix.ui.detail.models.MovieDetails
import com.anatame.pickaflix.ui.detail.models.SeasonItem

abstract class AbstractDetailHandler {

    lateinit var detailHandlerListener: DetailHandlerListener

    abstract fun getStreamUrl(url: String)

    fun addListener(mDetailHandlerListener: DetailHandlerListener){
        detailHandlerListener = mDetailHandlerListener
    }
}