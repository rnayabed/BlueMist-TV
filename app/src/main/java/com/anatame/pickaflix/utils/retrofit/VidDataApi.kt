package com.anatame.pickaflix.utils.data.remote.retrofit

import com.anatame.pickaflix.utils.retrofit.VidData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url


interface VidDataApi {
    @GET
    suspend fun getVidData(@Url url: String): Response<VidData>

}
