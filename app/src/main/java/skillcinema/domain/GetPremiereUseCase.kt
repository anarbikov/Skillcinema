package skillcinema.domain

import skillcinema.data.FilmsDto
import skillcinema.data.Repository
import javax.inject.Inject

class GetPremiereUseCase @Inject constructor(
    private val repository: Repository
) {
     suspend fun execute(): FilmsDto {
        return repository.getPremieres()
    }
}