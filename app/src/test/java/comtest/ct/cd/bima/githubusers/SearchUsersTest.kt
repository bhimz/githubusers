package comtest.ct.cd.bima.githubusers

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import comtest.ct.cd.bima.githubusers.domain.SortType
import comtest.ct.cd.bima.githubusers.domain.User
import comtest.ct.cd.bima.githubusers.domain.repository.UserRepository
import comtest.ct.cd.bima.githubusers.domain.usecase.SearchUsers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SearchUsersTest {
    private lateinit var userRepository: UserRepository
    private lateinit var useCase: SearchUsers

    @Before
    fun setUp() {
        userRepository = mock()
        useCase = SearchUsers(userRepository)
    }

    @Test
    fun `search user`() = runBlocking {
        val query = "user"
        val page = 1
        val rowCount = 10
        val order = SortType.ASC

        val expected = listOf(
            User("bpupadhyaya", "https://avatars0.githubusercontent.com/u/2559131?v=4"),
            User("bHimes", "https://avatars3.githubusercontent.com/u/3077528?v=4")
        )

        whenever(
            userRepository.find(
                eq(query),
                eq(page),
                eq(rowCount),
                any(),
                eq(order)
            )
        ).thenReturn(expected)

        useCase.invoke(SearchUsers.Params(query, page)).collect {
            Assert.assertEquals(expected, it)
        }
    }

    @Test
    fun `search user with row count and order`() = runBlocking {
        val query = "user"
        val page = 1
        val rowCount = 10
        val order = SortType.DESC

        val expected = listOf(
            User("bpupadhyaya", "https://avatars0.githubusercontent.com/u/2559131?v=4"),
            User("bHimes", "https://avatars3.githubusercontent.com/u/3077528?v=4")
        )

        whenever(
            userRepository.find(
                eq(query),
                eq(page),
                eq(rowCount),
                any(),
                eq(order)
            )
        ).thenReturn(expected)

        useCase.invoke(SearchUsers.Params(query, page, rowCount, order)).collect {
            Assert.assertEquals(expected, it)
        }
    }
}