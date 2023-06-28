package skillcinema.data

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillcinema.R
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.synthetic.main.film_view.view.filmGenreTextView
import kotlinx.android.synthetic.main.film_view.view.filmImageView
import kotlinx.android.synthetic.main.film_view.view.filmNameTextView
import skillcinema.entity.Country
import skillcinema.entity.Film
import skillcinema.entity.Films
import skillcinema.entity.Genre
import javax.inject.Inject

@JsonClass(generateAdapter = true)
data class FilmsDto(
    @Json(name = "items") override val items: List<FilmDto>,
    @Json(name = "total") override val total: Int
): Films

@JsonClass(generateAdapter = true)
data class FilmDto (
    @Json (name = "kinopoiskId") override val kinopoiskId : Int?,
    @Json (name = "posterUrlPreview") override val posterUrlPreview: String?,
    @Json (name = "countries")override val countries: List<CountryDto>?,
    @Json (name = "duration")override val duration: Int?,
    @Json (name = "genres")override val genres: List<GenreDto>,
    @Json (name = "nameEn")override val nameEn: String?,
    @Json (name = "nameRu")override val nameRu: String?,
    @Json (name = "posterUrl")override val posterUrl: String?,
    @Json (name = "premiereRu")override val premiereRu: String?,
    @Json (name = "year")override val year: Int?
):Film

@JsonClass(generateAdapter = true)
data class CountryDto (
    @Json(name = "country") override val country: String
): Country

@JsonClass(generateAdapter = true)
data class GenreDto (
    @Json(name = "genre") override val genre: String
):Genre

class ChildFilmAdapter @Inject constructor(
    filmData: List<FilmDto>,
) :
    RecyclerView.Adapter<ChildFilmAdapter.DataViewHolder>() {

    private var filmList: List<FilmDto> = ArrayList()

    init {
        this.filmList = filmData
    }

    var onItemClick: ((FilmDto) -> Unit)? = null

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                Log.d("mytag", "clicked2")
                Log.d("mytag", filmList[bindingAdapterPosition].nameRu.toString())
                onItemClick?.invoke(filmList[bindingAdapterPosition])
            }
        }

        fun bind(result: FilmDto) {
            itemView.filmNameTextView.text = result.nameRu
            val posterUrlPreview = result.posterUrlPreview
            Glide.with(itemView.context).load(posterUrlPreview).into(itemView.filmImageView)
            var genres = ""
            if (result.genres.size == 1) genres += result.genres[0].genre
            else {
                for (i in result.genres) {
                    genres += i.genre + ", "
                }
            }
            itemView.filmGenreTextView.text =
                if (result.genres.size != 1) genres.dropLast(2) else genres
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.film_view, parent,
            false
        )
    )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(filmList[position])
    }

    override fun getItemCount(): Int = filmList.size
}