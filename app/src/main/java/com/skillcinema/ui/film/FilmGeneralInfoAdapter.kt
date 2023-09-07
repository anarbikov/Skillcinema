package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.R
import com.skillcinema.databinding.FilmGeneralInfoViewBinding
import com.skillcinema.entity.FilmInfo
import com.skillcinema.room.Film
import javax.inject.Inject

class FilmGeneralInfoAdapter @Inject constructor(
    val context: Context,
    private val onClickWatched: (Film) -> Unit,
    private val onClickLiked: (Film) -> Unit,
    private val onClickToWatch: (Film) -> Unit,
    private val addToHistory: (Film) -> Unit
) : RecyclerView.Adapter<FilmGeneralInfoAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: FilmGeneralInfoViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private lateinit var filmInfo: FilmInfo
    private var isCollapsed = INITIAL_IS_COLLAPSED

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FilmGeneralInfoViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
                Glide.with(holder.itemView.context)
                    .load(filmInfo.posterUrlPreview)
                    .centerCrop()
                    .into(this.posterImageView)
                val rating = filmInfo.ratingKinopoisk ?: ""
                val filmName = filmInfo.nameRu ?: (filmInfo.nameOriginal ?: "")
                val filmYear = filmInfo.year ?: ""
                val filmGenre =
                    if (filmInfo.genres.size == 1) ", " + filmInfo.genres[0].genre
                    else if (filmInfo.genres.size > 1) (", " + filmInfo.genres[0].genre + ", " + filmInfo.genres[1].genre)
                    else ""
                val filmCountries =
                    if (filmInfo.countries.size == 1) {filmInfo.countries[0].country}
                    else if (filmInfo.countries.size > 1) filmInfo.countries[0].country + ", " + filmInfo.countries[1].country
                    else ""
                val durationHours =
                    if (filmInfo.filmLength != null) (filmInfo.filmLength!! / 60) else 0
                val durationMinutes =
                    if (filmInfo.filmLength != null) filmInfo.filmLength!! - durationHours * 60 else 0
                val duration =
                    if (filmInfo.filmLength != null) ", $durationHours ${context.getString(R.string.hour)} $durationMinutes ${
                        context.getString(
                            R.string.minutes
                        )
                    }" else ""
                val ageRestriction =
                    if (filmInfo.ratingAgeLimits != null) ", " + filmInfo.ratingAgeLimits.toString()
                        .drop(3) + "+" else ""
                this.filmNameTextView.text =
                    "$rating $filmName\n$filmYear$filmGenre\n$filmCountries$duration$ageRestriction"
                this.filmDescriptionHeaderTextView.visibility =
                    if (filmInfo.shortDescription != null) View.VISIBLE else View.GONE
                this.filmDescriptionHeaderTextView.text =
                    if (filmInfo.shortDescription != null) filmInfo.shortDescription.toString() else ""
                this.filmDescriptionBodyTextView.visibility =
                    if (filmInfo.description != null) View.VISIBLE else View.GONE
                val description = if (filmInfo.description != null) filmInfo.description.toString() else ""
                this.filmDescriptionBodyTextView.text = description

            this.filmDescriptionBodyTextView.setOnClickListener {
                if (isCollapsed){
                    this.filmDescriptionBodyTextView.maxLines = 20
                }
                else {this.filmDescriptionBodyTextView.maxLines = MAX_LINES_COLLAPSED}
                isCollapsed = !isCollapsed
            }
            addToHistory(createFilmForRoom(parameter = "",boolean = false))
            val watchedFieldRes = if (filmInfo.isWatched) R.drawable.watched else R.drawable.not_watched
            this.notWatched.setImageResource(watchedFieldRes)
            this.notWatched.setOnClickListener{
                val res: Int
                if (filmInfo.isWatched){ //delete from watched
                    res = R.drawable.not_watched
                    filmInfo.isWatched = false
                        onClickWatched (createFilmForRoom(parameter = "isWatched",boolean = false))
                }else{
                    res = R.drawable.watched
                    filmInfo.isWatched = true
                        onClickWatched (createFilmForRoom(parameter = "isWatched",boolean = true))
                }
                this.notWatched.setImageResource(res)
            }
            this.like.setImageResource(if (filmInfo.isLiked)R.drawable.liked else R.drawable.favorite)
            this.like.setOnClickListener {
                if (filmInfo.isLiked) {
                    this.like.setImageResource(R.drawable.favorite)
                    filmInfo.isLiked = false
                    onClickLiked(createFilmForRoom(parameter = "isLiked",boolean = false))
                }else{
                    this.like.setImageResource(R.drawable.liked)
                    filmInfo.isLiked = true
                    onClickLiked(createFilmForRoom(parameter = "isLiked",boolean = true))
                }
            }
            this.toWatchList.setImageResource(if (filmInfo.toWatch)R.drawable.in_to_watch else R.drawable.to_watch)
            this.toWatchList.setOnClickListener {
                if (filmInfo.toWatch) {
                    this.toWatchList.setImageResource(R.drawable.to_watch)
                    filmInfo.toWatch = false
                    onClickToWatch(createFilmForRoom("toWatch",boolean = false))
                }else{
                    this.toWatchList.setImageResource(R.drawable.in_to_watch)
                    filmInfo.toWatch = true
                    onClickToWatch(createFilmForRoom("toWatch",boolean = true))
                }
            }
            this.share.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "https://www.kinopoisk.ru/film/${filmInfo.kinopoiskId}/")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(context,shareIntent, null)
            }
        }
    }

    private fun createFilmForRoom(parameter:String,boolean: Boolean): Film {
        return Film(
            kinopoiskId = filmInfo.kinopoiskId!!,
            duration = filmInfo.filmLength,
            nameEn = filmInfo.nameEn as String?,
            nameRu = filmInfo.nameRu,
            posterUrl =filmInfo.posterUrl,
            posterUrlPreview=filmInfo.posterUrlPreview,
            premiereRu= null,
            year= filmInfo.year,
            ratingKinopoisk= filmInfo.ratingKinopoisk,
            filmId= filmInfo.kinopoiskId,
            isWatched= if (parameter == "isWatched")boolean else false,
            countries= filmInfo.countries.joinToString(",") { it.country.toString() },
            genres= filmInfo.genres.joinToString(",") {it.genre.toString()  },
            isLiked = if (parameter == "isLiked")boolean else false,
            toWatch = if (parameter == "toWatch")boolean else false
        )
    }
    override fun getItemCount(): Int {
        return 1
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addData(info: FilmInfo) {
        this.filmInfo = info
        notifyDataSetChanged()
    }

    companion object{
        private const val MAX_LINES_COLLAPSED = 3
        private const val INITIAL_IS_COLLAPSED = true
    }
}