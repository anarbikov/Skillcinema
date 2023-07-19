package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.databinding.FilmActorsViewBinding
import com.skillcinema.entity.ActorDto
import javax.inject.Inject

class FilmActorsChildAdapter @Inject constructor(
    val context: Context,
    info: List<ActorDto>
) : RecyclerView.Adapter<FilmActorsChildAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: FilmActorsViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var actorsList: List<ActorDto>
    init {
        this.actorsList = info
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmActorsChildAdapter.ViewHolder {
        return ViewHolder(
            FilmActorsViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FilmActorsChildAdapter.ViewHolder, position: Int) {
        holder.binding.apply {
            Glide.with(holder.itemView.context)
                .load(actorsList[position].posterUrl)
                .centerCrop()
                .into(this.actorImageView)
            this.actorNameTextView.text = if (actorsList[position].nameRu != null)
                actorsList[position].nameRu else actorsList[position].nameEn ?: ""
            this.actorNickTextView.text = if (actorsList[position].description!=null) actorsList[position].description else actorsList[position].professionText ?: ""
        }
    }

    override fun getItemCount(): Int {
        return actorsList.size
    }
}