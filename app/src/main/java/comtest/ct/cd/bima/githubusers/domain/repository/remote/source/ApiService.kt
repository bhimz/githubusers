package comtest.ct.cd.bima.githubusers.domain.repository.remote.source

import comtest.ct.cd.bima.githubusers.domain.repository.remote.Result
import comtest.ct.cd.bima.githubusers.domain.repository.remote.UserItem
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/search/users")
    suspend fun findUsers(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order") order: String
    ): Result<UserItem>
}