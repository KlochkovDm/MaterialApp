package com.example.materialapp.viewmodel

import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.materialapp.BuildConfig
import com.example.materialapp.R
import com.example.materialapp.repository.PictureOfTheDayResponseData
import com.example.materialapp.repository.PictureOfTheDayRetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PictureOfTheDayViewModel(private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayState> = MutableLiveData(),
                               private val retrofitImpl: PictureOfTheDayRetrofitImpl = PictureOfTheDayRetrofitImpl()
) : ViewModel() {
    fun getData(): LiveData<PictureOfTheDayState> {
        return liveDataForViewToObserve
    }

    fun sendServerRequest(date: Date) {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = formatter.format(date).toString()
        liveDataForViewToObserve.value = PictureOfTheDayState.Loading(0)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            liveDataForViewToObserve.value = PictureOfTheDayState.Error(Throwable("wrong key"))
        } else {
            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey, formattedDate).enqueue(callback)
        }
    }

    private val callback = object : Callback<PictureOfTheDayResponseData> {
        override fun onResponse(
            call: Call<PictureOfTheDayResponseData>,
            response: Response<PictureOfTheDayResponseData>
        ) {
            if(response.isSuccessful&&response.body()!=null){
                liveDataForViewToObserve.value = PictureOfTheDayState.Success(response.body()!!)
            }else{
                liveDataForViewToObserve.value = PictureOfTheDayState.Error(IllegalStateException(R.string.illegal_state_exception.toString()))
            }
        }

        override fun onFailure(call: Call<PictureOfTheDayResponseData>, t: Throwable) {
            liveDataForViewToObserve.value = PictureOfTheDayState.Error(throw t)
            t.printStackTrace()
        }

    }
}