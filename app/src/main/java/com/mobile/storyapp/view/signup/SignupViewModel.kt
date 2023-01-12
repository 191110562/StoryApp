package com.mobile.storyapp.view.signup

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobile.storyapp.api.ApiConfig
import com.mobile.storyapp.model.LoginResponse
import com.mobile.storyapp.model.UserPreference
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel(private val pref: UserPreference): ViewModel() {

    private val _check = MutableLiveData<String>()
    fun check(): LiveData<String> {
        return _check
    }

    fun onRegister(name: String, email: String, password: String) {
        val service = ApiConfig().getApiService(pref).registerUser(name, email, password)
        _check.value = "Loading"
        service.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d(ContentValues.TAG, "Successful")
                    _check.postValue("Success")
                } else {
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    _check.postValue(jsonObj.getString("message"))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                _check.postValue(t.message as String)
            }
        })
    }
}