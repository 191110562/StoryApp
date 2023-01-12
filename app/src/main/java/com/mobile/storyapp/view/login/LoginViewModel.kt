package com.mobile.storyapp.view.login

import android.util.Log
import androidx.lifecycle.*
import com.mobile.storyapp.api.ApiConfig
import com.mobile.storyapp.model.LoginResponse
import com.mobile.storyapp.model.UserModel
import com.mobile.storyapp.model.UserPreference
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference): ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun saveLogin(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    private val _check = MutableLiveData<String>()
    fun check(): LiveData<String> {
        return _check
    }

    fun onLogin(email: String, password: String) {
        val service = ApiConfig().getApiService(pref).loginUser(email, password)
        _check.value = "Loading"
        service.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>,
            ) {
                if (response.isSuccessful) {
                    saveLogin(
                        UserModel(
                            response.body()?.userModel?.userId.toString(),
                            response.body()?.userModel?.name.toString(),
                            response.body()?.userModel?.token.toString(),
                        )
                    )
                    _check.postValue("Success")
                } else {
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                    _check.postValue(jsonObj.getString("message"))
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("TEST", "onFailure: ${t.message.toString()}")
                _check.postValue(t.message as String)
            }
        })
    }
}