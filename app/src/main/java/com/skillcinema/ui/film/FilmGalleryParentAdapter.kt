package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skillcinema.R
import com.skillcinema.databinding.FilmGalleryChildRvBinding
import com.skillcinema.entity.FilmGalleryDto
import javax.inject.Inject

class FilmGalleryParentAdapter @Inject constructor(
    val context: Context,
) : RecyclerView.Adapter<FilmGalleryParentAdapter.ViewHolderGallery>() {
    private lateinit var images: FilmGalleryDto
    private val bundle = bundleOf()
    private var kinopoiskId = 0
    inner class ViewHolderGallery(val binding:FilmGalleryChildRvBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(result: FilmGalleryDto) {
            binding.apply {
                val takenImages = result.items?.take(20)
                childRecyclerView.adapter =
                    FilmGalleryChildAdapter(root.context, takenImages!!)
                childRecyclerView.layoutManager =
                    GridLayoutManager(root.context, 1, GridLayoutManager.HORIZONTAL, false)
                galleryAll.text = if (result.total!! > 4) "${result.total} >" else ""
                galleryAll.visibility =
                    if (result.items.isNotEmpty()) View.VISIBLE else View.GONE
                galleryAll.setOnClickListener {
                    bundle.putInt("kinopoiskId", kinopoiskId)
                    root.findNavController()
                        .navigate(R.id.action_filmFragment_to_galleryFullFragment, bundle)
                }
                val header = context.getString(R.string.gallery_header)
                galleryHeader.text = if (result.items.isNotEmpty()) header else ""
                galleryHeader.visibility =
                    if (result.items.isNotEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolderGallery (
        FilmGalleryChildRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    override fun onBindViewHolder(holder: ViewHolderGallery, position: Int) {
        holder.bind(images)
    }

    override fun getItemCount(): Int = 1

    @SuppressLint("NotifyDataSetChanged")
    fun addData(gallery: FilmGalleryDto,filmId:Int) {
        images = gallery
        kinopoiskId = filmId
        notifyDataSetChanged()
    }
}