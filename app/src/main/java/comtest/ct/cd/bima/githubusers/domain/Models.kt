package comtest.ct.cd.bima.githubusers.domain

data class User(val name: String, val avatarUrl: String?)

enum class SortType(val value: String) {
    ASC("asc"),
    DESC("desc")
}

data class AppSettings(
    val rowPerPage: Int
)