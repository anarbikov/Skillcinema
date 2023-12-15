package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.R
import com.skillcinema.databinding.FilmGalleryImageViewBinding
import com.skillcinema.entity.FilmGalleryItemDto
import javax.inject.Inject

class FilmGalleryChildAdapter @Inject constructor(
    val context: Context,
    images: List<FilmGalleryItemDto>
) : RecyclerView.Adapter<FilmGalleryChildAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: FilmGalleryImageViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var imagesList: List<FilmGalleryItemDto>
    init {
        this.imagesList = images
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmGalleryChildAdapter.ViewHolder {
        return ViewHolder(
            FilmGalleryImageViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FilmGalleryChildAdapter.ViewHolder, position: Int) {
        val url = if (imagesList[position].previewUrl != null) imagesList[position].previewUrl else imagesList[position].imageUrl
        holder.binding.apply {
            Glide.with(root.context)
                .load(url)
                .centerCrop()
                .into(galleryImageView)
        }
        holder.binding.root.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("imageUrl", imagesList[position].imageUrl)
            holder.binding.root.findNavController().navigate(R.id.action_filmFragment_to_galleryFullScreenFragment,bundle)
        }
    }
    override fun getItemCount(): Int {
        return imagesList.size
    }
}