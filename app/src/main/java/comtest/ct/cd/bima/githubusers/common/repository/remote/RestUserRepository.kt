package comtest.ct.cd.bima.githubusers.common.repository.remote

import comtest.ct.cd.bima.githubusers.common.SortType
import comtest.ct.cd.bima.githubusers.common.User
import comtest.ct.cd.bima.githubusers.common.repository.UserRepository
import comtest.ct.cd.bima.githubusers.common.repository.remote.source.ApiService

class RestUserRepository(
    private val apiService: ApiService
) : UserRepository {
    override suspend fun find(
        query: String,
        pageNumber: Int,
        rowCount: Int,
        sorted: SortType
    ): List<User> =
        runCatching {
            apiService.findUsers(
                query,
                pageNumber,
                rowCount,
                sorted.value
            ).items?.map { it.asUser() }
                ?: listOf()
        }.getOrThrow()
}