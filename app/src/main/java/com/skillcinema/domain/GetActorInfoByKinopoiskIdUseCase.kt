package com.skillcinema.domain

import com.skillcinema.data.Repository
import com.skillcinema.entity.ActorGeneralInfoDto
import javax.inject.Inject

class GetActorInfoByKinopoiskIdUseCase @Inject constructor(
    private val repository: Repository
) {
     suspend fun execute(staffId:Int): ActorGeneralInfoDto {
        return repository.getActorInfoByKinopoiskId(staffId)
    }
}