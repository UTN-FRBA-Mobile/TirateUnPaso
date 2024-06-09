package com.example.tirateunpaso.database.user

import kotlinx.coroutines.flow.Flow

class OfflineUsersRepository(private val userDao: UserDao) :
    UsersRepository {
    override fun getAllList(): List<User> = userDao.getAllList()

    override fun getAllStream(): Flow<List<User>> = userDao.getAllStream()

    override fun getOneStream(id: Int): Flow<User?> = userDao.getOne(id)

    override suspend fun insertOne(user: User) = userDao.insertOne(user)

    override suspend fun deleteOne(user: User) = userDao.deleteOne(user)

    override suspend fun updateOne(user: User) = userDao.updateOne(user)

    override suspend fun deleteAll() = userDao.deleteAll()

    override suspend fun insertMany(vararg users: User) = userDao.insertMany(*users)
}
