package com.skillcinema.domain

import com.skillcinema.entity.FilmsDto
import com.skillcinema.data.Repository
import javax.inject.Inject

class GetCartoonsUseCase @Inject constructor(
    private val repository: Repository
) {
     suspend fun execute(): FilmsDto {
        return repository.getCartoons()
    }
}