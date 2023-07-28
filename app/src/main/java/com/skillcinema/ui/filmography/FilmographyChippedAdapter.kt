package com.skillcinema.ui.filmography

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.R
import com.skillcinema.entity.FilmInfo
import kotlinx.android.synthetic.main.actor_best_film_view.view.filmGenreTextView
import kotlinx.android.synthetic.main.actor_best_film_view.view.filmImageView
import kotlinx.android.synthetic.main.actor_best_film_view.view.filmNameTextView
import kotlinx.android.synthetic.main.actor_best_film_view.view.ratingFrame
import kotlinx.android.synthetic.main.actor_best_film_view.view.ratingTextView
import javax.inject.Inject

class FilmographyChippedAdapter @Inject constructor(
    val context: Context,
) :
    RecyclerView.Adapter<FilmographyChippedAdapter.DataViewHolder>() {
    private var filmList: MutableList<FilmInfo> = ArrayList()

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bundle = bundleOf()
        init {
            itemView.setOnClickListener {
                bundle.putInt("kinopoiskId",filmList[bindingAdapterPosition].kinopoiskId!!)
                itemView.findNavController().navigate(R.id.action_filmographyFragment_to_filmFragment ,bundle)
            }
        }

        fun bind(result: FilmInfo) {
            itemView.filmNameTextView.text = when{
                result.nameRu != null -> result.nameRu
                result.nameEn != null -> result.nameEn.toString()
                result.nameOriginal != null -> result.nameOriginal.toString()
                else -> ""
            }

            var rating = ""
            if (result.ratingKinopoisk != null) {rating = result.ratingKinopoisk.toString()}

            itemView.ratingFrame.visibility = if (rating !="") View.VISIBLE else View.GONE
            itemView.ratingTextView.text = rating
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
            R.layout.filmography_film_view, parent,
            false
        )
    )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(filmList[position])
    }

    override fun getItemCount(): Int = filmList.size
    @SuppressLint("NotifyDataSetChanged")
    fun removeData (){
        filmList.clear()
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addData (filmData: List<FilmInfo>) {
        this.filmList = filmData.toMutableList()
        notifyDataSetChanged()
    }
}