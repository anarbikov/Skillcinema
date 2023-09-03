package com.skillcinema.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CollectionWIthFilms(
    @Embedded
    val collection: Collection,
    @Relation(
        parentColumn = "name",
        entityColumn = "kinopoisk_id",
        associateBy = Junction(
            FilmCollection::class,
            parentColumn = "collection_name",
            entityColumn = "film_id"
        )
    )
    val films: List<Film>,
)
