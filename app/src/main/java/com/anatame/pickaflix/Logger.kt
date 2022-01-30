package com.anatame.pickaflix

object Logger{
    fun Log(tag: String,data: Any){
        android.util.Log.d("$tag", data.toString())
    }
}