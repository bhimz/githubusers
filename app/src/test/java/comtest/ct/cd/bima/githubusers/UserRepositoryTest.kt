package comtest.ct.cd.bima.githubusers

import comtest.ct.cd.bima.githubusers.domain.SortType
import comtest.ct.cd.bima.githubusers.domain.User
import comtest.ct.cd.bima.githubusers.domain.repository.UserRepository
import comtest.ct.cd.bima.githubusers.domain.repository.remote.RestUserRepository
import comtest.ct.cd.bima.githubusers.domain.repository.remote.Result
import comtest.ct.cd.bima.githubusers.domain.repository.remote.UserItem
import comtest.ct.cd.bima.githubusers.domain.repository.remote.source.ApiService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class UserRepositoryTest {
    private lateinit var apiService: ApiService
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        apiService = mock(ApiService::class.java)
        userRepository = RestUserRepository(apiService)
    }

    @Test
    fun `find user successful`() = runBlocking {
        val query = "user"
        val page = 1
        val rowCount = 10
        val sortBy = "followers"
        val order = SortType.ASC

        val response = Result(
            items = listOf(
                UserItem("bpupadhyaya", "https://avatars0.githubusercontent.com/u/2559131?v=4"),
                UserItem("bHimes", "https://avatars3.githubusercontent.com/u/3077528?v=4")
            )
        )
        val expected = listOf(
            User("bpupadhyaya", "https://avatars0.githubusercontent.com/u/2559131?v=4"),
            User("bHimes", "https://avatars3.githubusercontent.com/u/3077528?v=4")
        )
        `when`(
            apiService.findUsers(
                query,
                page,
                rowCount,
                sortBy,
                order.value
            )
        ).thenReturn(response)

        runCatching {
            val result = userRepository.find(
                query,
                page,
                rowCount,
                sortBy,
                order
            )
            assertEquals(expected, result)
        }.getOrElse { fail("Should not fail request") }
    }
}