package comtest.ct.cd.bima.githubusers

import android.app.Application
import comtest.ct.cd.bima.githubusers.domain.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GithubSearchApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GithubSearchApplication)
            modules(appModule)
        }
    }
}