package skillcinema.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import skillcinema.entity.Film
import skillcinema.entity.Films

@JsonClass(generateAdapter = true)
data class FilmsDto(
    @Json(name = "items") override val items: List<FilmDto>,
    @Json(name = "total") override val total: Int
): Films

@JsonClass(generateAdapter = true)
data class FilmDto (
    @Json (name = "kinopoiskId") override val kinopoiskId : Int? =null,
    @Json (name = "posterUrlPreview") override val posterUrlPreview: String? =null,
//    @Json (name = "countries")override val countries: List<CountryDto>?=null,
    @Json (name = "duration")override val duration: Int? =null,
//    @Json (name = "duration")override val genres: List<GenreDto>? =null,
    @Json (name = "nameEn")override val nameEn: String? =null,
    @Json (name = "nameRu")override val nameRu: String? =null,
    @Json (name = "posterUrl")override val posterUrl: String? =null,
    @Json (name = "premiereRu")override val premiereRu: String? =null,
    @Json (name = "year")override val year: Int? =null
):Film

//@JsonClass(generateAdapter = true)
//data class CountryDto (
//    @Json(name = "country") val country: String
//)
//
//@JsonClass(generateAdapter = true)
//data class GenreDto (
//    @Json(name = "genre") val genre: String? =null
//)