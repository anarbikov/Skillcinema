package com.skillcinema.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.R
import com.skillcinema.databinding.HomeFilmViewBinding
import com.skillcinema.entity.FilmDto
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
    inner class DataViewHolder(val binding:HomeFilmViewBinding) : RecyclerView.ViewHolder(binding.root) {
        private val bundle = bundleOf()
        init {
            itemView.setOnClickListener {
                bundle.putInt("kinopoiskId",filmList[bindingAdapterPosition].kinopoiskId!!)
                itemView.findNavController().navigate(R.id.action_navigation_home_to_filmFragment,bundle)
            }
        }

        fun bind(result: FilmDto, holder: DataViewHolder) {
            holder.binding.apply {
                filmNameTextView.text = result.nameRu ?: (result.nameEn ?: "")
                if (result.ratingKinopoisk != null) {
                    ratingFrame.visibility = View.VISIBLE
                    ratingTextView.text = result.ratingKinopoisk.toString()
                }
                val posterUrlPreview = result.posterUrlPreview
                Glide.with(binding.root.context).load(posterUrlPreview).into(filmImageView)
                var genres = ""
                if (result.genres.size == 1) genres += result.genres[0].genre
                else {
                    for (i in result.genres) {
                        genres += i.genre + ", "
                    }
                }
                filmGenreTextView.text =
                    if (result.genres.size != 1) genres.dropLast(2) else genres
                alreadyWatched.visibility =
                    if (!result.isWatched) View.GONE else View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        HomeFilmViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(filmList[position], holder = holder)
    }

    override fun getItemCount(): Int = filmList.size
}