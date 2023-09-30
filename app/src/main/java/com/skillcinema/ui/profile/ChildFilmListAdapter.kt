package com.skillcinema.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.databinding.FragmentProfileChildFilmAdapterFilmViewBinding
import com.skillcinema.databinding.FragmentProfileFooterViewBinding
import com.skillcinema.room.CollectionWIthFilms
import com.skillcinema.room.Film
import kotlinx.android.synthetic.main.fragment_profile_child_film_adapter_film_view.view.filmGenreTextView
import kotlinx.android.synthetic.main.fragment_profile_child_film_adapter_film_view.view.filmImageView
import kotlinx.android.synthetic.main.home_film_view.view.filmNameTextView
import javax.inject.Inject

class ChildFilmListAdapter @Inject constructor(
    filmData: List<CollectionWIthFilms>,
    val context: Context,
    var onClickCleanHistory: (String) -> Unit,
    var onClickFilm: (Film) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var filmList: MutableList<Film> = mutableListOf()
    private var collectionName = ""
    init {
        this.collectionName = filmData[0].collection.name
        this.filmList = if (filmData[0].films.size<20)filmData[0].films.toMutableList() else filmData[0].films.take(20).toMutableList()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_FILM -> ViewHolder1(
                FragmentProfileChildFilmAdapterFilmViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> ViewHolder2(
                FragmentProfileFooterViewBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_FILM -> {
                val result = filmList[position]
                holder.itemView.apply {
                    filmNameTextView.text = result.nameRu ?: (result.nameEn ?: "")
                    val url = result.posterUrlPreview ?: result.posterUrl
                    filmGenreTextView.text = result.genres
                    Glide.with(context)
                        .load(url)
                        .into(filmImageView)
                    setOnClickListener{
                        onClickFilm (result)
                    }
                }
            }
            TYPE_FOOTER -> {
                holder.itemView.visibility = if (filmList.isNotEmpty()) View.VISIBLE else View.GONE
                holder.itemView.setOnClickListener {
                    onClickCleanHistory(collectionName)
                    filmList.clear()
                    notifyDataSetChanged()
                }
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return when (position){
            (itemCount-1) -> TYPE_FOOTER
            else -> TYPE_FILM
        }
    }
    override fun getItemCount(): Int = filmList.size+1
    companion object{
        private const val TYPE_FILM = 1
        private const val TYPE_FOOTER = 2
    }
}
class ViewHolder1(binding: FragmentProfileChildFilmAdapterFilmViewBinding) :
    RecyclerView.ViewHolder(binding.root)
class ViewHolder2(binding: FragmentProfileFooterViewBinding) :
    RecyclerView.ViewHolder(binding.root)