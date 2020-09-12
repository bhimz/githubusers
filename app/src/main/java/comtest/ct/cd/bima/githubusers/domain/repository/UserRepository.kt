package comtest.ct.cd.bima.githubusers.domain.repository

import comtest.ct.cd.bima.githubusers.domain.SortType
import comtest.ct.cd.bima.githubusers.domain.User

interface UserRepository {
    suspend fun find(
        query: String,
        pageNumber: Int,
        rowCount: Int,
        sortBy: String,
        sorted: SortType
    ): List<User>
}