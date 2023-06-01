package skillcinema.domain

import skillcinema.data.Repository
import javax.inject.Inject

class GetSharedPrefsUseCase @Inject constructor(
private val repository: Repository
) {
    fun execute():Int{
        return repository.getOnBoardingFlag()
    }
}