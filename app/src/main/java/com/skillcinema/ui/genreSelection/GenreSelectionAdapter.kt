package com.skillcinema.ui.genreSelection

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.data.FilterGenreDto
import com.skillcinema.databinding.GenreSelectionGenreViewBinding
import kotlinx.android.synthetic.main.genre_selection_genre_view.view.genreName
import javax.inject.Inject

class GenreSelectionAdapter @Inject constructor(
var onClickGenre: (Int) -> Unit

):
RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var genres: List<FilterGenreDto> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder {
        val view = GenreSelectionGenreViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val result = genres[position]
        holder.itemView.apply {
            genreName.text = result.genre
                setOnClickListener {
                    result.id?.let { it1 -> onClickGenre(it1) }
            }
        }
    }

    override fun getItemCount(): Int = genres.size

    @SuppressLint("NotifyDataSetChanged")
    fun addData(list: List<FilterGenreDto>) {
        genres = list
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun clearData(){
        genres = listOf()
        notifyDataSetChanged()
    }
}
class ViewHolder(binding: GenreSelectionGenreViewBinding) :
    RecyclerView.ViewHolder(binding.root)