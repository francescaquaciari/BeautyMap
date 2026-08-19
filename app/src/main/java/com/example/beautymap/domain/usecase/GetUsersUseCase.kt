package com.example.beautymap.domain.usecase

import com.example.beautymap.common.Result
import com.example.beautymap.domain.model.User
import com.example.beautymap.domain.repositories.LocalRepository
import com.example.beautymap.domain.repositories.RemoteRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUsersUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) {
    operator fun invoke(): Flow<Result<List<User>>> = flow {
        emit(Result.Loading("Loading..."))

        try {
            val remoteData = remoteRepository.downloadData()
            if (remoteData.isNotEmpty()) {
                localRepository.save(remoteData)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val localData = localRepository.getAll()
        if (localData.isNotEmpty()) {
            emit(Result.Success(localData))
        } else {
            emit(Result.Error("Impossibile caricare i dati delle estetiste"))
        }
    }
}