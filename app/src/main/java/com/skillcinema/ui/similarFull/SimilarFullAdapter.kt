package com.skillcinema.ui.similarFull

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.R
import com.skillcinema.databinding.SimilarFullFilmViewBinding
import com.skillcinema.entity.FilmSimilarsDto
import com.skillcinema.entity.FilmSimilarsItemDto
import javax.inject.Inject

class SimilarFullAdapter @Inject constructor(
    val context: Context,

) : RecyclerView.Adapter<SimilarFullAdapter.SimilarViewHolder>() {
    private var images: List <FilmSimilarsItemDto> = listOf()
    private var kinopoiskId: Int = 0

    inner class SimilarViewHolder( val binding: SimilarFullFilmViewBinding) :
        RecyclerView.ViewHolder(binding.root){
        private val bundle = bundleOf()
        init {
            binding.root.setOnClickListener{
                bundle.putInt("kinopoiskId",kinopoiskId)
                binding.root.findNavController().navigate(R.id.action_similarFullFragment_to_filmFragment,bundle)
            }
        }
fun bind(result: FilmSimilarsItemDto) {
    binding.filmNameTextView.text = result.nameRu ?: (result.nameEn ?: "")
    val url = result.posterUrlPreview ?: result.posterUrl
    Glide.with(binding.root.context)
        .load(url)
        .into(binding.filmImageView)
}
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): SimilarViewHolder {
        val view = SimilarFullFilmViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        view.root.layoutParams.width = screenWidth / 2-20

        return SimilarViewHolder(view)


    }
    override fun onBindViewHolder(holder: SimilarViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addData(info: FilmSimilarsDto, filmId:Int){
        this.images = info.items!!
        this.kinopoiskId = filmId
        notifyDataSetChanged()
    }
}