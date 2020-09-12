package comtest.ct.cd.bima.githubusers.common

data class User(val name: String, val avatarUrl: String?)

enum class SortType(val value: String) {
    ASC("asc"),
    DESC("desc")
}