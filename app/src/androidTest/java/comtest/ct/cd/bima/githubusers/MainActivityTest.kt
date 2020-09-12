package comtest.ct.cd.bima.githubusers

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import comtest.ct.cd.bima.githubusers.domain.AppSettings
import comtest.ct.cd.bima.githubusers.domain.User
import comtest.ct.cd.bima.githubusers.domain.repository.UserRepository
import comtest.ct.cd.bima.githubusers.domain.usecase.SearchUsers
import comtest.ct.cd.bima.githubusers.util.delayedTest
import comtest.ct.cd.bima.githubusers.util.onRecyclerView
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest : KoinTest {

    private var scenario: ActivityScenario<MainActivity>? = null
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        stopKoin()
        startKoin {
            androidContext(InstrumentationRegistry.getInstrumentation().targetContext)
            modules(listOf())
        }
        userRepository = mock()
    }

    @After
    fun tearDown() {
        scenario?.close()
    }

    @Test
    fun testSearchUser() = runBlocking {
        val users = listOf(
            User("asdf", ""),
            User("asdg", ""),
            User("asdh", ""),
            User("asdi", "")
        )
        whenever(userRepository.find(eq("asd"), any(), any(), any(), any())).thenReturn(users)
        openPage()
        onView(withId(R.id.searchView)).perform(typeText("asd"), closeSoftKeyboard())
        delayedTest {
            users.forEachIndexed { index, user ->
                onView(
                    onRecyclerView(R.id.resultListView).atPositionWithId(
                        index,
                        R.id.usernameText
                    )
                ).check(matches(withText(user.name)))
            }
        }
    }

    private fun openPage() {
        loadKoinModules(
            module {
                single { AppSettings(rowPerPage = 50) }
                single { userRepository }
                single { SearchUsers(get()) }
                viewModel { UserListViewModel(get(), get()) }
            }
        )
        scenario = launchActivity()
    }
}