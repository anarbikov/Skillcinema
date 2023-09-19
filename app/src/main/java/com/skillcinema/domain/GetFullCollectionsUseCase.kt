package com.skillcinema.domain

import com.skillcinema.data.Repository
import com.skillcinema.room.CollectionWIthFilms
import javax.inject.Inject

class GetFullCollectionsUseCase @Inject constructor(
    private val repository: Repository
) {
     fun execute():List<CollectionWIthFilms> {
        return repository.getFullCollections()
    }
}