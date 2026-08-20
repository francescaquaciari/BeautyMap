package com.example.beautymap.data.local

import com.example.beautymap.data.local.entities.UserEntity
import com.example.beautymap.domain.model.User
import com.example.beautymap.domain.repositories.LocalRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers

private fun User.toEntity() = UserEntity(        //converte un oggetto User in un oggetto UserEntity per essere inserito nel database
    id = id.toLong(),
    name = name,
    username = username,
    email = email,
    city = city,
    address = address,
    phone = phone,
    facebook = facebook,
    website = website,
    instagram = instagram,
    lat = lat.toDoubleOrNull() ?: 0.0,
    lng = lng.toDoubleOrNull() ?: 0.0
)

private fun UserEntity.toDomain() = User(     //converte un oggetto UserEntity in un oggetto User per essere utilizzato in tutta l'app
    id = id?.toInt() ?:0,
    name = name,
    username = username,
    email = email,
    city = city,
    address = address,
    phone = phone,
    facebook = facebook,
    website = website,
    instagram = instagram,
    lat = lat.toString(),
    lng = lng.toString()
)

class RoomLocalRepository @Inject constructor(      //classe che implementa l'interfaccia LocalRepository
    private val userDao: UserDao
) : LocalRepository {

    override suspend fun save (data: List<User>) {
        with(Dispatchers.IO) {             //serve per cambiare il thread di esecuzione
            clear()
            userDao.insert(data.map { it.toEntity() })
        }
    }
    override suspend fun getAll(): List<User> {
        return userDao.getAll().map { it.toDomain() }
    }
    override suspend fun clear() {
        userDao.clear()
    }

}