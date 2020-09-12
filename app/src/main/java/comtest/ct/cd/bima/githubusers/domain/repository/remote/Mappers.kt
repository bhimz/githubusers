package comtest.ct.cd.bima.githubusers.domain.repository.remote

import comtest.ct.cd.bima.githubusers.domain.User

fun UserItem.asUser(): User {
    val name = login ?: throw NoSuchElementException("login")
    return User(name, avatarUrl)
}