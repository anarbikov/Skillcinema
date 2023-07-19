package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.databinding.FilmGalleryImageViewBinding
import com.skillcinema.entity.ItemDto
import javax.inject.Inject

class FilmGalleryChildAdapter @Inject constructor(
    val context: Context,
    images: List<ItemDto>
) : RecyclerView.Adapter<FilmGalleryChildAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: FilmGalleryImageViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var imagesList: List<ItemDto>
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
        holder.binding.apply {
            Glide.with(holder.itemView.context)
                .load(imagesList[position].previewUrl)
                .centerCrop()
                .into(this.galleryImageView)
        }
    }
    override fun getItemCount(): Int {
        return imagesList.size
    }
}