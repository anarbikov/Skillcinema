package com.skillcinema.ui.film

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.R
import com.skillcinema.entity.FilmSimilarsItemDto
import kotlinx.android.synthetic.main.film_similar_film_view.view.alreadyWatched
import kotlinx.android.synthetic.main.home_film_view.view.filmImageView
import kotlinx.android.synthetic.main.home_film_view.view.filmNameTextView
import javax.inject.Inject

class FilmSimilarChildAdapter @Inject constructor(
    filmData: List<FilmSimilarsItemDto>,
    val context: Context
) :
    RecyclerView.Adapter<FilmSimilarChildAdapter.DataViewHolder>() {
    private var filmList: List<FilmSimilarsItemDto> = ArrayList()
    init {
        this.filmList = filmData
    }
    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bundle = bundleOf()

        init {
            itemView.setOnClickListener {
                    bundle.putInt("kinopoiskId",filmList[bindingAdapterPosition].filmId!!)
                itemView.findNavController().navigate(R.id.action_filmFragment_self,bundle)
            }
        }

        fun bind(result: FilmSimilarsItemDto) {
            itemView.filmNameTextView.text = result.nameRu ?: (result.nameEn ?: "")
            val url = result.posterUrlPreview ?: result.posterUrl
            Glide.with(itemView.context)
                .load(url)
                .into(itemView.filmImageView)
            itemView.alreadyWatched.setImageResource(if (result.isWatched) R.drawable.watched else R.drawable.not_watched)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.film_similar_film_view, parent,
            false
        )
    )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(filmList[position])
    }

    override fun getItemCount(): Int = filmList.size
}