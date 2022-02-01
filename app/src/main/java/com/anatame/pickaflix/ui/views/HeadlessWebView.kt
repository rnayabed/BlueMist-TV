package com.anatame.pickaflix.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

import android.webkit.WebView


class HeadlessWebView : WebView {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!,
        attrs,
        defStyle
    ) {
    }

//    override fun onTouchEvent(event: MotionEvent): Boolean {
//       requestDisallowInterceptTouchEvent(true)
//        return super.onTouchEvent(event)
//    }
}