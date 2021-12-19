package com.example.materialapp.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PictureOfTheDayAPI {

    @GET("planetary/apod")

    fun getPictureOfTheDay(@Query("api_key") apikey:String): Call<PictureOfTheDayResponseData>
}