package comtest.ct.cd.bima.githubusers.domain.repository.remote

import comtest.ct.cd.bima.githubusers.domain.SortType
import comtest.ct.cd.bima.githubusers.domain.User
import comtest.ct.cd.bima.githubusers.domain.repository.UserRepository
import comtest.ct.cd.bima.githubusers.domain.repository.remote.source.ApiService

class RestUserRepository(
    private val apiService: ApiService
) : UserRepository {
    override suspend fun find(
        query: String,
        pageNumber: Int,
        rowCount: Int,
        sortBy: String,
        sorted: SortType
    ): List<User> =
        runCatching {
            apiService.findUsers(
                query,
                pageNumber,
                rowCount,
                sortBy,
                sorted.value
            ).items?.map { it.asUser() }
                ?: listOf()
        }.getOrThrow()
}