package com.skillcinema.domain

import com.skillcinema.data.FilmsDto
import com.skillcinema.data.Repository
import javax.inject.Inject

class GetPremiereUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun execute(): FilmsDto {
        return repository.getPremieres()
    }
}