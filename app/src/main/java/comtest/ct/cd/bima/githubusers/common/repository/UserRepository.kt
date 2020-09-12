package comtest.ct.cd.bima.githubusers.common.repository

import comtest.ct.cd.bima.githubusers.common.SortType
import comtest.ct.cd.bima.githubusers.common.User

interface UserRepository {
    suspend fun find(query: String, pageNumber: Int, rowCount: Int, sorted: SortType): List<User>
}