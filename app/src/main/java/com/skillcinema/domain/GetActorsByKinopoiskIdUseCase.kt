package com.skillcinema.domain

import com.skillcinema.data.Repository
import com.skillcinema.entity.ActorDto
import javax.inject.Inject

class GetActorsByKinopoiskIdUseCase @Inject constructor(
    private val repository: Repository
) {
     suspend fun execute(kinopoiskId:Int): List<ActorDto> {
        return repository.getActorsByKinopoiskId(kinopoiskId)
    }
}