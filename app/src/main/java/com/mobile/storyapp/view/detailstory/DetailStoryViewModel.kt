package com.mobile.storyapp.view.detailstory

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobile.storyapp.api.ApiConfig
import com.mobile.storyapp.model.UserPreference
import com.mobile.storyapp.response.ListStory
import com.mobile.storyapp.response.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailStoryViewModel(private val pref: UserPreference): ViewModel() {
    private val _detail = MutableLiveData<List<ListStory>>()
    fun detail(): LiveData<List<ListStory>> {
        return _detail
    }
    private fun showDetail() {
        val service = ApiConfig().getApiService(pref).fetchStory()
        service.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                if (response.isSuccessful) {
                    _detail.value = response.body()!!.listStory
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }
}