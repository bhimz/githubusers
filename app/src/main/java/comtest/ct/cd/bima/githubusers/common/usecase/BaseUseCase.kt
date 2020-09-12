package comtest.ct.cd.bima.githubusers.common.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class BaseUseCase<P : UseCaseParam, T> {
    suspend fun invoke(param: P?): Flow<T> = flow { emit(execute(param)) }

    abstract suspend fun execute(param: P? = null): T
}

interface UseCaseParam