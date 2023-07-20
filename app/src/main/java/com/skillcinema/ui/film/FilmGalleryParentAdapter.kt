package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.R
import com.skillcinema.entity.FilmGalleryDto
import kotlinx.android.synthetic.main.film_actors_child_rv.view.childRecyclerView
import kotlinx.android.synthetic.main.film_gallery_child_rv.view.galleryAll
import kotlinx.android.synthetic.main.film_gallery_child_rv.view.galleryHeader
import javax.inject.Inject

class FilmGalleryParentAdapter @Inject constructor(
    val context: Context,
) : RecyclerView.Adapter<FilmGalleryParentAdapter.ViewHolderGallery>() {
    private lateinit var images: FilmGalleryDto
    inner class ViewHolderGallery(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(result: FilmGalleryDto) {
            val takenImages = result.items?.take(20)
            itemView.childRecyclerView.adapter =
                FilmGalleryChildAdapter(context, takenImages!!)
            itemView.childRecyclerView.layoutManager =
                GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
            itemView.galleryAll.text = if(takenImages.size < result.total!!) "${result.total} >" else ""
            itemView.galleryAll.visibility = if (result.items.isNotEmpty())View.VISIBLE else View.GONE
            val header = context.getString(R.string.gallery_header)
            itemView.galleryHeader.text = if (result.items.isNotEmpty()) header else ""
            itemView.galleryHeader.visibility = if (result.items.isNotEmpty()) View.VISIBLE else View.GONE

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGallery = ViewHolderGallery (
        LayoutInflater.from(parent.context).inflate(
            R.layout.film_gallery_child_rv, parent,
            false
        )
    )
    override fun onBindViewHolder(holder: ViewHolderGallery, position: Int) {
        holder.bind(images)
    }

    override fun getItemCount(): Int = 1

    @SuppressLint("NotifyDataSetChanged")
    fun addData(gallery: FilmGalleryDto) {
        images = gallery
        notifyDataSetChanged()
    }
}