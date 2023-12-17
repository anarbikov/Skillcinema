package com.skillcinema.ui.film

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.R
import com.skillcinema.databinding.FilmSimilarFilmViewBinding
import com.skillcinema.entity.FilmSimilarsItemDto

class FilmSimilarChildAdapter (
    filmData: List<FilmSimilarsItemDto>,
) :
    RecyclerView.Adapter<FilmSimilarChildAdapter.DataViewHolder>() {
    private var filmList: List<FilmSimilarsItemDto> = ArrayList()
    init {
        this.filmList = filmData
    }
    inner class DataViewHolder(val binding:FilmSimilarFilmViewBinding) : RecyclerView.ViewHolder(binding.root) {
        private val bundle = bundleOf()

        init {
            binding.root.setOnClickListener {
                    bundle.putInt("kinopoiskId",filmList[bindingAdapterPosition].filmId!!)
                binding.root.findNavController().navigate(R.id.action_filmFragment_self,bundle)
            }
        }

        fun bind(result: FilmSimilarsItemDto) {
            binding.apply {
                filmNameTextView.text = result.nameRu ?: (result.nameEn ?: "")
                val url = result.posterUrlPreview ?: result.posterUrl
                Glide.with(root.context)
                    .load(url)
                    .into(filmImageView)
                alreadyWatched.setImageResource(if (result.isWatched) R.drawable.watched else R.drawable.not_watched)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        FilmSimilarFilmViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(filmList[position])
    }

    override fun getItemCount(): Int = filmList.size
}