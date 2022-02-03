package com.anatame.pickaflix.model.scrollstate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class HomeScrollStates (
    val scrollState1 : MutableLiveData<Int>,
    val scrollState2 : MutableLiveData<Int>,
    val scrollState3 : MutableLiveData<Int>,
    val scrollState4 : MutableLiveData<Int>,
    val scrollState5 : MutableLiveData<Int>,
)