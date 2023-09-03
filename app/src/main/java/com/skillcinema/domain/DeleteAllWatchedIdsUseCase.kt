package com.skillcinema.domain

import com.skillcinema.data.Repository
import javax.inject.Inject

class DeleteAllWatchedIdsUseCase @Inject constructor(
    private val repository: Repository
) {
     suspend fun execute() {
        return repository.deleteAllWatched()
    }
}