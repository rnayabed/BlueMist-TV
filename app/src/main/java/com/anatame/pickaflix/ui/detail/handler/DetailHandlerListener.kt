package com.anatame.pickaflix.ui.detail.handler

import com.anatame.pickaflix.ui.detail.models.ServerItem

interface DetailHandlerListener {
    fun onVideoUrlLoaded(url: String)
    fun onServerItemSelected(pos: Int, serverData: ServerItem)
}