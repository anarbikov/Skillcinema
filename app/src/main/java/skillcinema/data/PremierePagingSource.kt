//package skillcinema.data
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import skillcinema.domain.GetFilmsUseCase
//import javax.inject.Inject
//
//class PremierePagingSource @Inject constructor(
//    private val useCase: GetFilmsUseCase,
//    private val category: String
//) : PagingSource<Int, FilmsDto>() {
//    override fun getRefreshKey(state: PagingState<Int, FilmsDto>): Int = FIRST_PAGE
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilmsDto> {
//        val page = params.key ?: FIRST_PAGE
//        return kotlin.runCatching {
//            useCase.execute()
//        }.fold(
//            onSuccess = {
//                it.category = category
//                LoadResult.Page(
//                    data = listOf(it),
//                    prevKey = null,
//                    nextKey = if(it.items.isEmpty())null else page+1
//                )
//            }, onFailure = { LoadResult.Error(it) }
//        )
//    }
//
//    private companion object {
//        private const val FIRST_PAGE = 1
//    }
//}
//
//

