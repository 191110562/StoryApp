package com.mobile.storyapp.di

import com.mobile.storyapp.api.ApiConfig
import com.mobile.storyapp.data.ListRepository
import com.mobile.storyapp.model.UserPreference

object Injection {
    fun provideRepository(dataStore: UserPreference): ListRepository{
        val apiService = ApiConfig().getApiService(dataStore)
        return ListRepository(apiService)
    }
}