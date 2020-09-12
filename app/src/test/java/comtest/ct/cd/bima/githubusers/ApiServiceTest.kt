package comtest.ct.cd.bima.githubusers

import com.google.gson.GsonBuilder
import comtest.ct.cd.bima.githubusers.domain.repository.remote.Result
import comtest.ct.cd.bima.githubusers.domain.repository.remote.UserItem
import comtest.ct.cd.bima.githubusers.domain.repository.remote.source.ApiService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTest {
    private lateinit var mockServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        mockServer = MockWebServer()
        mockServer.start()

        apiService = Retrofit.Builder()
            .baseUrl(mockServer.url("/"))
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().create()
                )
            ).build()
            .create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun `find user successful`() {
        val jsonResponse = """
            {
                "total_count": 584,
                "incomplete_results": false,
                "items": [
                    {
                        "login": "bpupadhyaya",
                        "id": 2559131,
                        "node_id": "MDQ6VXNlcjI1NTkxMzE=",
                        "avatar_url": "https://avatars0.githubusercontent.com/u/2559131?v=4",
                        "gravatar_id": "",
                        "url": "https://api.github.com/users/bpupadhyaya",
                        "html_url": "https://github.com/bpupadhyaya",
                        "followers_url": "https://api.github.com/users/bpupadhyaya/followers",
                        "following_url": "https://api.github.com/users/bpupadhyaya/following{/other_user}",
                        "gists_url": "https://api.github.com/users/bpupadhyaya/gists{/gist_id}",
                        "starred_url": "https://api.github.com/users/bpupadhyaya/starred{/owner}{/repo}",
                        "subscriptions_url": "https://api.github.com/users/bpupadhyaya/subscriptions",
                        "organizations_url": "https://api.github.com/users/bpupadhyaya/orgs",
                        "repos_url": "https://api.github.com/users/bpupadhyaya/repos",
                        "events_url": "https://api.github.com/users/bpupadhyaya/events{/privacy}",
                        "received_events_url": "https://api.github.com/users/bpupadhyaya/received_events",
                        "type": "User",
                        "site_admin": false,
                        "score": 1.0
                    },
                    {
                        "login": "bHimes",
                        "id": 3077528,
                        "node_id": "MDQ6VXNlcjMwNzc1Mjg=",
                        "avatar_url": "https://avatars3.githubusercontent.com/u/3077528?v=4",
                        "gravatar_id": "",
                        "url": "https://api.github.com/users/bHimes",
                        "html_url": "https://github.com/bHimes",
                        "followers_url": "https://api.github.com/users/bHimes/followers",
                        "following_url": "https://api.github.com/users/bHimes/following{/other_user}",
                        "gists_url": "https://api.github.com/users/bHimes/gists{/gist_id}",
                        "starred_url": "https://api.github.com/users/bHimes/starred{/owner}{/repo}",
                        "subscriptions_url": "https://api.github.com/users/bHimes/subscriptions",
                        "organizations_url": "https://api.github.com/users/bHimes/orgs",
                        "repos_url": "https://api.github.com/users/bHimes/repos",
                        "events_url": "https://api.github.com/users/bHimes/events{/privacy}",
                        "received_events_url": "https://api.github.com/users/bHimes/received_events",
                        "type": "User",
                        "site_admin": false,
                        "score": 1.0
                    }
                ]
            }
        """.trimIndent()

        val response = MockResponse().setResponseCode(200).setBody(jsonResponse)
        mockServer.enqueue(response)

        val expected = Result(
            items = listOf(
                UserItem("bpupadhyaya", "https://avatars0.githubusercontent.com/u/2559131?v=4"),
                UserItem("bHimes", "https://avatars3.githubusercontent.com/u/3077528?v=4")
            )
        )

        runBlocking {
            val result = apiService.findUsers("bh", 1, 10, "followers", "asc")
            assertEquals(expected, result)
        }

    }
}