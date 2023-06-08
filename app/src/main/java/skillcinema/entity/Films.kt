package skillcinema.entity


interface Films {
    val items: List<Film>
    val total: Int
}

interface Film {
    val countries: List<Country>?
    val duration: Int?
    val genres: List<Genre>
    val kinopoiskId: Int?
    val nameEn: String?
    val nameRu: String?
    val posterUrl: String?
    val posterUrlPreview: String?
    val premiereRu: String?
    val year: Int?
}
interface Country {
    val country: String
}

interface Genre {
    val genre: String
}
