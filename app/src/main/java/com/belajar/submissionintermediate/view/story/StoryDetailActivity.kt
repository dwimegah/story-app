package com.belajar.submissionintermediate.view.story

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.belajar.submissionintermediate.data.database.StoryItem
import com.belajar.submissionintermediate.databinding.ActivityStoryDetailBinding
import com.bumptech.glide.Glide


class StoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryDetailBinding
    private var data: StoryItem? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            data = intent.getParcelableExtra("dataStories", StoryItem::class.java)
        } else {
            data = intent.getParcelableExtra<StoryItem>("dataStories")
        }

        binding.title.text = "${data?.name}"
        binding.desc.text = "${data?.description}"
        Glide.with(this)
            .load("${data?.photoUrl}")
            .into(binding.image)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}