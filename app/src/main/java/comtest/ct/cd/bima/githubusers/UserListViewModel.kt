package comtest.ct.cd.bima.githubusers

import androidx.lifecycle.*
import comtest.ct.cd.bima.githubusers.domain.SortType
import comtest.ct.cd.bima.githubusers.domain.User
import comtest.ct.cd.bima.githubusers.domain.usecase.SearchUsers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserListViewModel(
    private val searchUsers: SearchUsers
) : ViewModel() {

    private var query = ""
    private var page = 0
    private var order = SortType.ASC

    private val searchParams = MutableLiveData<Triple<String, Int, SortType>>()

    val uiState = MutableLiveData<UserListState>()

    val users = Transformations.switchMap(searchParams) {
        val (query, page, order) = it
        search(query, page, order)
    }

    private fun search(query: String, page: Int, order: SortType): LiveData<List<User>> {
        val target = MutableLiveData<List<User>>()
        if (page > 1) {
            target.value = users.value
        }
        if (query.isEmpty()) {
            target.value = listOf()
            uiState.value = UserListState.READY
        } else {
            viewModelScope.launch {
                searchUsers.invoke(SearchUsers.Params(query, page, sortType = order))
                    .catch {
                        uiState.value = UserListState.ERROR(it)
                    }
                    .collect {
                        target.value = it
                        uiState.value = UserListState.READY
                    }
            }
        }
        return target
    }

    fun updateSearch(query: String, order: SortType, page: Int = 1) {
        this.query = query
        this.page = page
        this.order = order
        viewModelScope.launch {
            uiState.value = UserListState.LOADING
            searchParams.value = Triple(query, page, order)
        }
    }

    fun clearResults() = viewModelScope.launch {
        query = ""
        page = 1
        search(query, page, order)
    }

    fun nextPage() = viewModelScope.launch {
        if (uiState.value == UserListState.LOADING) return@launch
        page++
        updateSearch(query, order, page)
    }

}

sealed class UserListState {
    object LOADING : UserListState()
    object READY : UserListState()
    class ERROR(val error: Throwable) : UserListState()
}