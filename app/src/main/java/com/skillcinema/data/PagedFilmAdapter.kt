package com.skillcinema.data

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.databinding.FilmViewPagedBinding
import javax.inject.Inject


class PagedFilmAdapter @Inject constructor(
    private val onClick: (FilmDto) -> Unit,
    val context: Context
) : PagingDataAdapter<FilmDto, FilmPagedViewHolder>(DiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmPagedViewHolder {
        val view = FilmViewPagedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        view.root.layoutParams.width = screenWidth / 2-20

        return FilmPagedViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: FilmPagedViewHolder, position: Int) {

        val item = getItem(position)
        val posterPreviewUrl = item?.posterUrlPreview
        Glide.with(holder.itemView.context).load(posterPreviewUrl)
            .into(holder.binding.filmImageView)
        holder.binding.filmNameTextView.text = item?.nameRu
        var genres = ""
        if (item?.genres?.size!! == 1) genres += item.genres[0].genre
        else {
            for (i in item.genres) {
                genres += i.genre + ", "
            }
        }
        holder.binding.filmGenreTextView.text =
            if (item.genres.size != 1) genres.dropLast(2) else genres
        holder.binding.root.setOnClickListener {
            onClick(item)
        }
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<FilmDto>() {
    override fun areItemsTheSame(oldItem: FilmDto, newItem: FilmDto): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: FilmDto, newItem: FilmDto): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId
}

class FilmPagedViewHolder(val binding: FilmViewPagedBinding) : RecyclerView.ViewHolder(binding.root)