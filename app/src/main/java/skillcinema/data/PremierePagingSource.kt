package skillcinema.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import skillcinema.domain.GetPremiereUseCase
import skillcinema.entity.Film
import javax.inject.Inject

class PremierePagingSource @Inject constructor(private val useCase: GetPremiereUseCase): PagingSource<Int, Film> () {
    override fun getRefreshKey(state: PagingState<Int, Film>): Int = FIRST_PAGE
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {
        val page = params.key?: FIRST_PAGE
        return kotlin.runCatching {
            useCase.execute(page)
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it.items.shuffled().take(21),
                    prevKey = null,
                    nextKey = null
                )
            }, onFailure = {LoadResult.Error(it) }
        )
    }
    private companion object {
        private const val FIRST_PAGE = 1
    }

    }



