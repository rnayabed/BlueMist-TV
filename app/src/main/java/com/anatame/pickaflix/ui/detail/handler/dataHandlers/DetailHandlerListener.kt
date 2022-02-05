package com.anatame.pickaflix.ui.detail.handler.dataHandlers

import com.anatame.pickaflix.ui.detail.models.ServerItem

interface DetailHandlerListener {
    fun onVideoUrlLoaded(url: String)
}