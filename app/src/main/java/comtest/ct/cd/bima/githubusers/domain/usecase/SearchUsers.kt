package comtest.ct.cd.bima.githubusers.domain.usecase

import comtest.ct.cd.bima.githubusers.domain.SortType
import comtest.ct.cd.bima.githubusers.domain.User
import comtest.ct.cd.bima.githubusers.domain.repository.UserRepository

class SearchUsers(
    private val userRepository: UserRepository
) : BaseUseCase<SearchUsers.Params, List<User>>() {
    override suspend fun execute(param: Params?): List<User> {
        param ?: throw NoSuchElementException("param")
        return userRepository.find(param.query, param.pageNumber, param.rowCount, param.sortType)
    }

    class Params(
        val query: String,
        val pageNumber: Int,
        val rowCount: Int = 10,
        val sortType: SortType = SortType.ASC
    ) : UseCaseParam
}