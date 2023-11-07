package com.belajar.submissionintermediate.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.belajar.submissionintermediate.R
import com.belajar.submissionintermediate.databinding.ActivityMainBinding
import com.belajar.submissionintermediate.view.ViewModelFactory
import com.belajar.submissionintermediate.view.addstory.AddStoryActivity
import com.belajar.submissionintermediate.view.maps.MapsActivity
import com.belajar.submissionintermediate.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val storyViewModel by viewModels<StoryViewModel> {
        StoryModelFactory(this)
    }

    private lateinit var binding: ActivityMainBinding
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            fab.setOnClickListener {
                val intentStory = Intent(this@MainActivity, AddStoryActivity::class.java)
                startActivity(intentStory)
            }
        }

        binding.progressBar.visibility = View.VISIBLE

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                token = user.token

                val layoutManager = LinearLayoutManager(this)
                binding.recycleView.layoutManager = layoutManager
                val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
                binding.recycleView.addItemDecoration(itemDecoration)

                storyViewModel.getStories(token).observe(this) {
                    val adapter = MainAdapter()
                    adapter.submitData(lifecycle, it)
                    binding.recycleView.adapter = adapter
                }

                storyViewModel.isLoading.observe(this) {
                    if (it) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

        setupView()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
            }
            R.id.setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.logout -> {
                viewModel.logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}