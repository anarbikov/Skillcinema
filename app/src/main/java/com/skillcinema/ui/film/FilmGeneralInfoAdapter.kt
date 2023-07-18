package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.R
import com.skillcinema.databinding.FilmGeneralInfoViewBinding
import com.skillcinema.entity.FilmInfo
import javax.inject.Inject

class FilmGeneralInfoAdapter @Inject constructor(
    val context: Context
) : RecyclerView.Adapter<FilmGeneralInfoAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: FilmGeneralInfoViewBinding) :
        RecyclerView.ViewHolder(binding.root) {}
    private lateinit var filmInfo: FilmInfo
    init {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FilmGeneralInfoViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            Glide.with(holder.itemView.context)
                .load( filmInfo.posterUrlPreview)
                .centerCrop()
                .into(this.posterImageView)
            val rating = filmInfo.ratingKinopoisk?:""
            val filmName = filmInfo.nameRu ?: (filmInfo.nameOriginal?:"")
            val filmYear = filmInfo.year?:""
            val filmGenre = if (filmInfo.genres.isNotEmpty() && filmInfo.genres.size == 1 )", " + filmInfo.genres[0].genre
            else (", " + filmInfo.genres[0].genre + ", " + filmInfo.genres[1].genre)
            val filmCountries = if (filmInfo.countries.isNotEmpty() && filmInfo.countries.size == 1 ) filmInfo.countries[0].country
            else filmInfo.countries[0].country+", "+filmInfo.countries[1].country
            val durationHours = if(filmInfo.filmLength != null) (filmInfo.filmLength!! /60) else 0
            val durationMinutes = if (filmInfo.filmLength != null) filmInfo.filmLength!! - durationHours * 60 else 0
            val duration = if (filmInfo.filmLength != null) ", $durationHours ${context.getString(R.string.hour)} $durationMinutes ${context.getString(
                R.string.minutes)}" else ""
            val ageRestriction =  if (filmInfo.ratingAgeLimits!=null) ", "+ filmInfo.ratingAgeLimits.toString().drop(3)+"+" else ""
            this.filmNameTextView.text = "$rating $filmName\n$filmYear$filmGenre\n$filmCountries$duration$ageRestriction"
            this.filmDescriptionHeaderTextView.text = if (filmInfo.shortDescription != null) filmInfo.shortDescription.toString() else ""
            this.filmDescriptionBodyTextView.text = if (filmInfo.description != null) filmInfo.description.toString() else ""
        }
    }

    override fun getItemCount(): Int {
        return 1
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addData(info: FilmInfo) {
        this.filmInfo = info
        notifyDataSetChanged()
    }
}