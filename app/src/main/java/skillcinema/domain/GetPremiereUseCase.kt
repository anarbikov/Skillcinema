package skillcinema.domain

import skillcinema.data.Repository
import skillcinema.entity.Films
import javax.inject.Inject

class GetPremiereUseCase @Inject constructor(
    private val repository: Repository
) {
     suspend fun execute(page:Int): Films {
        return repository.getPremieres(page)
    }
}