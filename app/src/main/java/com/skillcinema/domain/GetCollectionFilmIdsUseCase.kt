package com.skillcinema.domain

import com.skillcinema.data.Repository
import javax.inject.Inject

class GetCollectionFilmIdsUseCase @Inject constructor(
    private val repository: Repository
) {
     fun execute(collection:String):List<Int> {
        return repository.getFilmIdsFromCollection(collection)
    }
}