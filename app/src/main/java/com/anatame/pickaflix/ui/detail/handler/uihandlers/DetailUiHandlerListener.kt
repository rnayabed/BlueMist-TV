package com.anatame.pickaflix.ui.detail.handler.uihandlers

interface DetailUiHandlerListener {
    fun seasonItemSelected(seasonDataID: String)
    fun episodeItemSelected(episodeDataID: String)
    fun videoEmbedLinkLoaded(embedUrl: String)
}