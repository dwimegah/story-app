package com.belajar.submissionintermediate.view.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.belajar.submissionintermediate.data.response.Story
import com.belajar.submissionintermediate.databinding.StoryItemBinding
import com.belajar.submissionintermediate.view.story.StoryDetailActivity
import com.bumptech.glide.Glide

class MainAdapter : ListAdapter<Story, MainAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, StoryDetailActivity::class.java)
            intentDetail.putExtra("dataStories", data)
            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                holder.itemView.context as Activity,
                Pair.create(holder.binding.image, "profile")
            )
            holder.itemView.context.startActivity(intentDetail, optionsCompat.toBundle())
        }
    }
    class MyViewHolder(val binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Story){
            binding.title.text = "${data.name}"
            Glide.with(this.itemView.context)
                .load("${data.photoUrl}")
                .into(binding.image)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }
        }
    }
}