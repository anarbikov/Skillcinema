package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.R
import com.skillcinema.entity.FilmSimilarsDto
import kotlinx.android.synthetic.main.film_similar_child_rv.view.similarAll
import kotlinx.android.synthetic.main.film_similar_child_rv.view.similarChildRecyclerView
import kotlinx.android.synthetic.main.film_similar_child_rv.view.similarHeader
import javax.inject.Inject

class FilmSimilarParentAdapter @Inject constructor(
    val context: Context,
) : RecyclerView.Adapter<FilmSimilarParentAdapter.ViewHolderSimilar>() {
    private lateinit var similarFilms: FilmSimilarsDto
    inner class ViewHolderSimilar(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(result: FilmSimilarsDto) {
            val takenImages = result.items?.shuffled()?.take(20)
            itemView.similarChildRecyclerView.adapter =
                FilmSimilarChildAdapter(context= context, filmData = takenImages!!)
            itemView.similarChildRecyclerView.layoutManager =
                GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
            itemView.similarAll.text = if(takenImages.size < result.total!!) "${result.total} >" else ""
            itemView.similarAll.visibility = if (result.items.isNotEmpty())View.VISIBLE else View.GONE
            val header = context.getString(R.string.similar_header)
            itemView.similarHeader.text = if (result.items.isNotEmpty()) header else ""
            itemView.similarHeader.visibility = if (result.items.isNotEmpty()) View.VISIBLE else View.GONE

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSimilar = ViewHolderSimilar (
        LayoutInflater.from(parent.context).inflate(
            R.layout.film_similar_child_rv, parent,
            false
        )
    )
    override fun onBindViewHolder(holder: ViewHolderSimilar, position: Int) {
        holder.bind(similarFilms)
    }

    override fun getItemCount(): Int = 1

    @SuppressLint("NotifyDataSetChanged")
    fun addData(similar: FilmSimilarsDto) {
        similarFilms = similar
        notifyDataSetChanged()
    }
}