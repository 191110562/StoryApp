package com.mobile.storyapp.view.detailstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.mobile.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_PHOTO = "extra_photo"
        const val EXTRA_DESCRIPTION = "extra_detail"
    }

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val name = intent.getStringExtra(EXTRA_NAME) as String
        val photo = intent.getStringExtra(EXTRA_PHOTO) as String
        val description = intent.getStringExtra(EXTRA_DESCRIPTION) as String
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail"

        setupDetail(name, description, photo)
    }

    private fun setupDetail(name: String, description: String, photoUrl: String){
        binding.nameTextView.text = name
        binding.descTextView.text = description
        Glide.with(binding.profileImageView)
            .load(photoUrl)
            .circleCrop()
            .into(binding.profileImageView)
    }
}