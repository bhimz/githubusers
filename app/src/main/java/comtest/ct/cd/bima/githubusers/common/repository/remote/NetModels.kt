package comtest.ct.cd.bima.githubusers.common.repository.remote

import com.google.gson.annotations.SerializedName

data class Result<T>(
    val items: List<T>?
)

data class UserItem(
    val login: String?,
    @SerializedName("avatar_url")
    val avatarUrl: String?
)