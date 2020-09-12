package comtest.ct.cd.bima.githubusers

import androidx.lifecycle.*
import comtest.ct.cd.bima.githubusers.domain.AppSettings
import comtest.ct.cd.bima.githubusers.domain.SortType
import comtest.ct.cd.bima.githubusers.domain.User
import comtest.ct.cd.bima.githubusers.domain.usecase.SearchUsers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserListViewModel(
    private val appSettings: AppSettings,
    private val searchUsers: SearchUsers

) : ViewModel() {

    private var query = ""
    private var page = 0
    private var order = SortType.ASC

    private val searchParams = MutableLiveData<Triple<String, Int, SortType>>()

    val uiState = MutableLiveData<UserListState>()

    private val userList = mutableListOf<User>()

    val users = Transformations.switchMap(searchParams) {
        val (query, page, order) = it
        search(query, page, order)
    }

    private fun search(query: String, page: Int, order: SortType): LiveData<List<User>> {
        if (page == 1 || query.isEmpty()) userList.clear()
        val target = MutableLiveData<List<User>>()
        if (query.isEmpty()) {
            target.value = userList
            uiState.value = UserListState.Ready
        } else viewModelScope.launch {
            searchUsers.invoke(
                SearchUsers.Params(
                    query,
                    page,
                    rowCount = appSettings.rowPerPage,
                    sortType = order
                )
            )
                .catch {
                    uiState.value = UserListState.Error(it)
                }
                .collect {
                    userList += it
                    target.value = userList
                    uiState.value = UserListState.Ready
                }
        }
        return target
    }

    fun updateSearch(query: String, order: SortType, page: Int = 1) {
        this.query = query
        this.page = page
        this.order = order
        viewModelScope.launch {
            uiState.value = UserListState.Loading
            searchParams.value = Triple(query, page, order)
        }
    }

    fun clearResults() = viewModelScope.launch {
        query = ""
        page = 1
        search(query, page, order)
    }

    fun nextPage() = viewModelScope.launch {
        if (uiState.value != UserListState.Ready) return@launch
        page++
        uiState.value = UserListState.LoadNext
        searchParams.value = Triple(query, page, order)
    }

}

sealed class UserListState {
    object Loading : UserListState()
    object LoadNext : UserListState()
    object Ready : UserListState()
    class Error(val error: Throwable) : UserListState()
}