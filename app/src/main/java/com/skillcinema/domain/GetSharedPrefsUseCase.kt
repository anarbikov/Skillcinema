package com.skillcinema.domain

import com.skillcinema.data.Repository
import javax.inject.Inject

class GetSharedPrefsUseCase @Inject constructor(
private val repository: Repository
) {
    fun execute():Int{
        return repository.getOnBoardingFlag()
    }
}