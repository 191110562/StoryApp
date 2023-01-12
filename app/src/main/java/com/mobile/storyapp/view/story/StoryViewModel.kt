package com.mobile.storyapp.view.story

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mobile.storyapp.api.ApiConfig
import com.mobile.storyapp.data.ListRepository
import com.mobile.storyapp.model.UserPreference
import com.mobile.storyapp.response.ListStory
import com.mobile.storyapp.response.StoryResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel(private val pref: UserPreference, listRepository: ListRepository): ViewModel() {

    val listStory: LiveData<List<ListStory>> = list()

    val list: LiveData<PagingData<ListStory>> =
        listRepository.getList().cachedIn(viewModelScope)

/*    private val _list = MutableLiveData<PagingData<ListStory>>()
    fun list(): LiveData<PagingData<ListStory>> {
        return _list
    }*/

    private val _list = MutableLiveData<List<ListStory>>()
    fun list(): LiveData<List<ListStory>> {
        return _list
    }

    private val _check = MutableLiveData<String>()
    fun check(): LiveData<String> {
        return _check
    }

    fun showStories(){
        val service = ApiConfig().getApiService(pref).fetchStory()
        _check.value = "Loading"
        service.enqueue(object : Callback<StoryResponse>{
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d(ContentValues.TAG, "Successful")
/*                    _list.postValue(list.value)*/
                    _list.postValue(response.body()!!.listStory)
                    _check.postValue("Success")
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    _check.postValue(jsonObj.getString("message"))
                }
            }
            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                _check.postValue(t.message as String)
            }
        })
    }

    fun getLocation(){
        val service = ApiConfig().getApiService(pref).getListLocation(1)
        _check.value = "Loading"
        service.enqueue(object : Callback<StoryResponse>{
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d(ContentValues.TAG, "Successful")
                    _list.postValue(response.body()!!.listStory)
                    _check.postValue("Success")
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    _check.postValue(jsonObj.getString("message"))
                }
            }
            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                _check.postValue(t.message as String)
            }
        })
    }

}