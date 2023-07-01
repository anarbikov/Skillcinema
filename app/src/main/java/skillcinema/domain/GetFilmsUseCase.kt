package skillcinema.domain

import skillcinema.data.FilmsDto

interface GetFilmsUseCase {

    suspend fun execute():FilmsDto
}