package com.mobile.storyapp.view.story

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.storyapp.R
import com.mobile.storyapp.ViewModelFactory
import com.mobile.storyapp.adapter.ListAdapter
import com.mobile.storyapp.databinding.ActivityStoryBinding
import com.mobile.storyapp.model.UserPreference
import com.mobile.storyapp.response.ListStory
import com.mobile.storyapp.view.addstory.AddStoryActivity
import com.mobile.storyapp.view.detailstory.DetailStoryActivity
import com.mobile.storyapp.view.maps.MapsActivity
import kotlinx.coroutines.launch


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class StoryActivity : AppCompatActivity() {
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var binding: ActivityStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Story"

        setupViewModel()
    }

    private fun setupViewModel(){

        storyViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[StoryViewModel::class.java]

        storyViewModel.showStories()
        val check = storyViewModel.check()
        check.observe(this) { status ->
            when (status) {
                "Success" -> {
                    showLoading(false)
                    storyViewModel.list.observe(this){ data ->
                        if (data != null ){
                            setupData(data)
                        }
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

     private fun setupData(story: PagingData<ListStory>) {

         val listAdapter = ListAdapter()
         this.lifecycleScope.launch {
             listAdapter.submitData(lifecycle, story)
         }
         listAdapter.notifyDataSetChanged()
         with(binding.rvListStory){
             adapter = listAdapter
             layoutManager = LinearLayoutManager(context)
         }

         listAdapter.setOnItemClickCallback(object :
             ListAdapter.OnItemClickCallback {
             override fun onItemClicked(data: ListStory) {
                 val intent =
                     Intent(this@StoryActivity, DetailStoryActivity::class.java)
                 intent.putExtra(DetailStoryActivity.EXTRA_NAME, data.name)
                 intent.putExtra(DetailStoryActivity.EXTRA_PHOTO, data.photoUrl)
                 intent.putExtra(DetailStoryActivity.EXTRA_DESCRIPTION, data.description)
                 startActivity(intent)
             }
         })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu1 -> {
                val i = Intent(this, AddStoryActivity::class.java)
                startActivity(i)
            }
            R.id.menu2 -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.menu3 -> {
                startActivity(Intent(this, MapsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        storyViewModel.list.observe(this){data ->
            setupData(data)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.bringToFront()
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}