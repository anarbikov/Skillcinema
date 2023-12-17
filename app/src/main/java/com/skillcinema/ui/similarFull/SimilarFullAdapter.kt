package com.skillcinema.ui.similarFull

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.databinding.SimilarFullFilmViewBinding
import com.skillcinema.entity.FilmSimilarsDto
import com.skillcinema.entity.FilmSimilarsItemDto

class SimilarFullAdapter (
    val onClickYear: (Int) -> Unit

) : RecyclerView.Adapter<SimilarFullAdapter.SimilarViewHolder>() {
    private var images: List <FilmSimilarsItemDto> = listOf()
    private var kinopoiskId: Int = 0

    inner class SimilarViewHolder( val binding: SimilarFullFilmViewBinding) :
        RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener{onClickYear(kinopoiskId)}
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
        val displayMetrics = view.root.context.resources.displayMetrics
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
        info.items?.let { this.images = it }
        this.kinopoiskId = filmId
        notifyDataSetChanged()
    }
}