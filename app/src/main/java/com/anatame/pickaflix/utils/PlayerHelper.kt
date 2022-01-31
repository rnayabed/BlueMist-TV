package com.anatame.pickaflix.utils

import android.content.Context
import android.util.Log
import android.webkit.WebView
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.text.ExoplayerCuesDecoder
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource

class PlayerHelper(
    private val context: Context,
    private val playerView: PlayerView,
) {
    private var player: ExoPlayer? = null

    fun initPlayer(): PlayerLoader{
        // playerView.useController = false


        player = ExoPlayer.Builder(context).build()
        playerView.player = player

        return PlayerLoader(context, player, playerView)
    }

    fun play(){
        player?.play()
    }

    fun pause(){
        player?.pause()
    }

    fun isPlaying(): Boolean{
        if (player != null) {
            return player!!.isPlaying
        } else {
            return false
        }
    }

    fun stopPlayer() {
        if (player == null) {
            return
        }
        player?.stop()
//        player = null
    }

    fun releasePlayer(){
        player?.stop()
        player?.release()
        player = null
    }
}

class PlayerLoader(
    private val context: Context,
    private val player: ExoPlayer?,
    private val playerView: PlayerView
){


    fun loadVideoandPlay(hls: String){
        if(player != null){
            player.setMediaSource(createMediaSource(hls))
            player.prepare()
            player.playWhenReady = true
        }
    }

    fun getPlayer(): ExoPlayer{
        return player!!
    }

    fun getPlayerView(): PlayerView{
        return playerView
    }

    private fun createMediaSource(hls: String): MediaSource {
        val dataSourceFactory = DefaultHttpDataSource.Factory()

        return HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(hls))
    }
}
