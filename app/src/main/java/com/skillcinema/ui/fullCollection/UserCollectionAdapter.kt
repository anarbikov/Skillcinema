package com.skillcinema.ui.fullCollection

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.databinding.FragmentFullCollectionFilmViewBinding
import com.skillcinema.room.CollectionWIthFilms
import com.skillcinema.room.Film
import kotlinx.android.synthetic.main.fragment_full_collection_film_view.view.filmGenreTextView
import kotlinx.android.synthetic.main.fragment_full_collection_film_view.view.filmImageView
import kotlinx.android.synthetic.main.fragment_full_collection_film_view.view.filmNameTextView
import javax.inject.Inject

class UserCollectionAdapter @Inject constructor(
val context: Context,
var onClickFilm: (Film) -> Unit

):
RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var collection: List<CollectionWIthFilms> = listOf()
    var filmList: MutableList<Film> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder {
        val view = FragmentFullCollectionFilmViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        view.root.layoutParams.width = screenWidth / 2-20

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val result = filmList[position]
        holder.itemView.apply {
            filmNameTextView.text = result.nameRu ?: (result.nameEn ?: "")
            val url = result.posterUrlPreview ?: result.posterUrl
            filmGenreTextView.text = result.genres
            Glide.with(context)
                .load(url)
                .into(filmImageView)
            setOnClickListener {
                onClickFilm(result)
            }
        }
    }

    override fun getItemCount(): Int = filmList.size

    @SuppressLint("NotifyDataSetChanged")
    fun addData(list: List<CollectionWIthFilms>) {
        collection = list
        filmList = collection[0].films.toMutableList()
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun removeData (){
        filmList.clear()
        notifyDataSetChanged()
    }
}
class ViewHolder(binding: FragmentFullCollectionFilmViewBinding) :
    RecyclerView.ViewHolder(binding.root)