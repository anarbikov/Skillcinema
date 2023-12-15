package com.skillcinema.ui.fullFilmList

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.databinding.FullFilmListFilmViewBinding
import com.skillcinema.entity.FilmDto
import javax.inject.Inject


class PagedFilmAdapter @Inject constructor(
    private val onClick: (FilmDto) -> Unit,
    val context: Context
) : PagingDataAdapter<FilmDto, FilmPagedViewHolder>(DiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmPagedViewHolder {
        val view = FullFilmListFilmViewBinding.inflate(
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
        holder.binding.apply {
            Glide.with(root.context).load(posterPreviewUrl)
                .into(filmImageView)
            filmNameTextView.text = item?.nameRu
            var genres = ""
            if (item?.genres?.size!! == 1) genres += item.genres[0].genre
            else {
                for (i in item.genres) {
                    genres += i.genre + ", "
                }
            }
            filmGenreTextView.text =
                if (item.genres.size != 1) genres.dropLast(2) else genres
            alreadyWatched.visibility =
                if (item.isWatched) View.VISIBLE else View.GONE
            root.setOnClickListener {
                onClick(item)
            }
        }
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<FilmDto>() {
    override fun areItemsTheSame(oldItem: FilmDto, newItem: FilmDto): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: FilmDto, newItem: FilmDto): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId
}

class FilmPagedViewHolder(val binding: FullFilmListFilmViewBinding) : RecyclerView.ViewHolder(binding.root)