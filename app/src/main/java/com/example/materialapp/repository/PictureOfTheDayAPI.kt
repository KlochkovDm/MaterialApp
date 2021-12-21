package com.example.materialapp.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface PictureOfTheDayAPI {

    @GET("planetary/apod")

    fun getPictureOfTheDay(@Query("api_key") apikey:String, @Query("date") date :String): Call<PictureOfTheDayResponseData>
}