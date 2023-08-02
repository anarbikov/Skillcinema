package com.skillcinema.ui.galleryFull

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.databinding.GalleryFullFilmViewBinding
import com.skillcinema.entity.FilmGalleryItemDto

class GalleryChippedAdapter (
    private val onClick: (FilmGalleryItemDto) -> Unit,
    val context: Context
    ) : PagingDataAdapter<FilmGalleryItemDto, GalleryPagedViewHolder>(DiffUtilCallback()) {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryPagedViewHolder {
            val view = GalleryFullFilmViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            val displayMetrics = context.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            view.root.layoutParams.width = screenWidth / 2-20

            return GalleryPagedViewHolder(view)
        }
        @SuppressLint("ResourceAsColor")
        override fun onBindViewHolder(holder: GalleryPagedViewHolder, position: Int) {
            val item = getItem(position)
            val posterPreviewUrl = item?.previewUrl?: item?.imageUrl
            Glide.with(holder.itemView.context).load(posterPreviewUrl)
                .into(holder.binding.posterImageView)
            holder.binding.root.setOnClickListener {
                onClick(item!!)
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<FilmGalleryItemDto>() {
        override fun areItemsTheSame(oldItem: FilmGalleryItemDto, newItem: FilmGalleryItemDto): Boolean =
            oldItem.imageUrl == newItem.imageUrl

        override fun areContentsTheSame(oldItem: FilmGalleryItemDto, newItem: FilmGalleryItemDto): Boolean =
            oldItem.imageUrl == newItem.imageUrl
    }

    class GalleryPagedViewHolder(val binding: GalleryFullFilmViewBinding) : RecyclerView.ViewHolder(binding.root)