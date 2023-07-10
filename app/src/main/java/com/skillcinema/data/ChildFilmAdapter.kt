package com.skillcinema.data

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.R

import kotlinx.android.synthetic.main.film_view.view.filmGenreTextView
import kotlinx.android.synthetic.main.film_view.view.filmImageView
import kotlinx.android.synthetic.main.film_view.view.filmNameTextView
import kotlinx.android.synthetic.main.film_view.view.ratingFrame
import kotlinx.android.synthetic.main.film_view.view.ratingTextView
import javax.inject.Inject

class ChildFilmAdapter @Inject constructor(
    filmData: List<FilmDto>,
) :
    RecyclerView.Adapter<ChildFilmAdapter.DataViewHolder>() {

    private var filmList: List<FilmDto> = ArrayList()

    init {
        this.filmList = filmData
    }

    var onItemClick: ((FilmDto) -> Unit)? = null

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                Log.d("mytag", "clicked2")
                Log.d("mytag", itemView.toString())
                onItemClick?.invoke(filmList[bindingAdapterPosition])
            }
        }

        fun bind(result: FilmDto) {
            itemView.filmNameTextView.text = result.nameRu
            if (result.ratingKinopoisk != null) {
                itemView.ratingFrame.visibility = View.VISIBLE
                itemView.ratingTextView.text = result.ratingKinopoisk.toString()
            }
            val posterUrlPreview = result.posterUrlPreview
            Glide.with(itemView.context).load(posterUrlPreview).into(itemView.filmImageView)
            var genres = ""
            if (result.genres.size == 1) genres += result.genres[0].genre
            else {
                for (i in result.genres) {
                    genres += i.genre + ", "
                }
            }
            itemView.filmGenreTextView.text =
                if (result.genres.size != 1) genres.dropLast(2) else genres
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.film_view, parent,
            false
        )
    )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(filmList[position])
    }

    override fun getItemCount(): Int = filmList.size
}