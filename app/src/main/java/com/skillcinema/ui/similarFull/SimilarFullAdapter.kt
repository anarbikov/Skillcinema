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
import kotlinx.android.synthetic.main.similar_full_film_view.view.filmImageView
import kotlinx.android.synthetic.main.similar_full_film_view.view.filmNameTextView
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
            itemView.setOnClickListener{
                bundle.putInt("kinopoiskId",kinopoiskId)
                itemView.findNavController().navigate(R.id.action_similarFullFragment_to_filmFragment,bundle)
            }
        }
fun bind(result: FilmSimilarsItemDto) {
    itemView.filmNameTextView.text = result.nameRu ?: (result.nameEn ?: "")
    val url = result.posterUrlPreview ?: result.posterUrl
    Glide.with(itemView.context)
        .load(url)
        .into(itemView.filmImageView)
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