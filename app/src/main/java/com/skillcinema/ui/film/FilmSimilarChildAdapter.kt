package com.skillcinema.ui.film

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.R
import com.skillcinema.entity.FilmSimilarsItemDto
import kotlinx.android.synthetic.main.film_view.view.filmImageView
import kotlinx.android.synthetic.main.film_view.view.filmNameTextView
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

//    var onItemClick: ((FilmDto) -> Unit)? = null

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bundle = bundleOf()

        init {
            itemView.setOnClickListener {
//                if (category != context.getString(R.string.Series)){
//                    Log.d("mytag","${context.getString(R.string.Series)}, $category")
//                    bundle.putInt("kinopoiskId",filmList[bindingAdapterPosition].kinopoiskId!!)
//                    bundle.putString("posterUrlPreview", filmList[bindingAdapterPosition].posterUrlPreview)
////                itemView.findNavController().navigate(R.id.action_navigation_home_to_filmFragment,bundle)
//                }
//                else Log.d("mytag","сериалы попались!!!!")
////                onItemClick?.invoke(filmList[bindingAdapterPosition])
            }
        }

        fun bind(result: FilmSimilarsItemDto) {
            itemView.filmNameTextView.text = result.nameRu ?: (result.nameEn ?: "")
            val url = if(result.posterUrlPreview != null) result.posterUrlPreview else result.posterUrl
            Glide.with(itemView.context)
                .load(url)
                .into(itemView.filmImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.similar_film_view, parent,
            false
        )
    )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(filmList[position])
    }

    override fun getItemCount(): Int = filmList.size
}