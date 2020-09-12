package comtest.ct.cd.bima.githubusers.domain.di

import com.google.gson.GsonBuilder
import comtest.ct.cd.bima.githubusers.UserListViewModel
import comtest.ct.cd.bima.githubusers.domain.repository.UserRepository
import comtest.ct.cd.bima.githubusers.domain.repository.remote.RestUserRepository
import comtest.ct.cd.bima.githubusers.domain.repository.remote.source.ApiService
import comtest.ct.cd.bima.githubusers.domain.usecase.SearchUsers
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { GsonBuilder().create() }
    single {
        Retrofit.Builder().baseUrl("http://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build().create(ApiService::class.java)
    }
    factory<UserRepository> { RestUserRepository(get()) }
    factory { SearchUsers(get()) }
    viewModel { UserListViewModel(get()) }
}