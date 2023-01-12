package com.mobile.storyapp.view.signup

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.mobile.storyapp.R
import com.mobile.storyapp.databinding.ActivitySignupBinding
import com.mobile.storyapp.model.UserPreference
import com.mobile.storyapp.ViewModelFactory
import com.mobile.storyapp.view.login.LoginActivity
import com.mobile.storyapp.view.main.MainActivity
import com.mobile.storyapp.welcome.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupView(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun setupViewModel(){
        signupViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SignupViewModel::class.java]
    }

    private fun setupAction(){
        binding.signupButton.setOnClickListener{
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            when {
                name.isEmpty() -> {
                    binding.nameEditTextLayout.error = getString(R.string.input_name)
                }
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = getString(R.string.input_email)
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = getString(R.string.input_password)
                }
                password.length < 6 -> {
                }
                else ->{
                    signupViewModel.onRegister(name, email, password)
                    val check = signupViewModel.check()
                    check.observe(this){ status ->
                        when (status) {
                            "Success" -> {
                                showLoading(false)
                                AlertDialog.Builder(this).apply {
                                    setTitle(getString(R.string.welcome))
                                    setMessage(getString(R.string.account_register))
                                    setPositiveButton(getString(R.string.next)) { _, _ ->
                                        val intent = Intent(context, WelcomeActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()
                                    }
                                    create()
                                    show()
                                }
                            }
                            "Loading" -> {
                                showLoading(true)
                            }
                            else -> {
                                showLoading(false)
                                Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.bringToFront()
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}