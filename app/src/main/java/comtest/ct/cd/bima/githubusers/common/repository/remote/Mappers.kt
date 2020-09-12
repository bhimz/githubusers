package comtest.ct.cd.bima.githubusers.common.repository.remote

import comtest.ct.cd.bima.githubusers.common.User

fun UserItem.asUser(): User {
    val name = login ?: throw NoSuchElementException("login")
    return User(name, avatarUrl)
}