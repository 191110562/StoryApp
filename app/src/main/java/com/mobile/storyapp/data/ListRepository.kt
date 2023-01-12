package com.mobile.storyapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.mobile.storyapp.api.ApiService
import com.mobile.storyapp.response.ListStory


class ListRepository(private val apiService: ApiService) {
    fun getList(): LiveData<PagingData<ListStory>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                Log.d("IKAN", "this is repo")
                ListPagingSource(apiService)
            }
        ).liveData
    }
}