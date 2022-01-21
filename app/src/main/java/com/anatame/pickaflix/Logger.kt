package com.anatame.pickaflix

object Logger{
    fun Log(data: Any){
        android.util.Log.d("DataLog", data.toString())
    }
}