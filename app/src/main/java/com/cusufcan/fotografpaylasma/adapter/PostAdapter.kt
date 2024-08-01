package com.cusufcan.fotografpaylasma.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cusufcan.fotografpaylasma.databinding.PostItemBinding
import com.cusufcan.fotografpaylasma.model.Post
import com.squareup.picasso.Picasso

class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.PostHolder>() {
    inner class PostHolder(val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostHolder(binding)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = posts[position]

        holder.binding.postEmailText.text = post.email
        holder.binding.postCommentText.text = post.comment
        Picasso.get().load(post.downloadUrl).into(holder.binding.postImage)
    }
}