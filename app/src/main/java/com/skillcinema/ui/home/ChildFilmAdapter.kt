package com.skillcinema.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.R
import com.skillcinema.entity.FilmDto
import kotlinx.android.synthetic.main.film_view.view.filmGenreTextView
import kotlinx.android.synthetic.main.film_view.view.filmImageView
import kotlinx.android.synthetic.main.film_view.view.filmNameTextView
import kotlinx.android.synthetic.main.film_view.view.ratingFrame
import kotlinx.android.synthetic.main.film_view.view.ratingTextView
import javax.inject.Inject

class ChildFilmAdapter @Inject constructor(
    val category:String,
    filmData: List<FilmDto>,
    val context: Context
) :
    RecyclerView.Adapter<ChildFilmAdapter.DataViewHolder>() {

    private var filmList: List<FilmDto> = ArrayList()

    init {
        this.filmList = filmData
    }

//    var onItemClick: ((FilmDto) -> Unit)? = null

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bundle = bundleOf()
        init {
            itemView.setOnClickListener {
                if (category != context.getString(R.string.Series)){
                    Log.d("mytag","${context.getString(R.string.Series)}, $category")
                    bundle.putInt("kinopoiskId",filmList[bindingAdapterPosition].kinopoiskId!!)
                    bundle.putString("posterUrlPreview", filmList[bindingAdapterPosition].posterUrlPreview)
                itemView.findNavController().navigate(R.id.action_navigation_home_to_filmFragment,bundle)
                }
                else Log.d("mytag","сериалы попались!!!!")
//                onItemClick?.invoke(filmList[bindingAdapterPosition])
            }
        }

        fun bind(result: FilmDto) {
            itemView.filmNameTextView.text = result.nameRu?:(result.nameEn?:"")
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