package com.skillcinema.domain

import com.skillcinema.entity.FilmsDto
import com.skillcinema.data.Repository
import javax.inject.Inject

class GetTop250UseCase @Inject constructor(
    private val repository: Repository
){
    suspend fun execute(): FilmsDto {
        return repository.getTop250()
    }
}