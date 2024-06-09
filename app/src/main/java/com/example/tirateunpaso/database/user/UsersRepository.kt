package com.example.tirateunpaso.database.user

import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun getAllList(): List<User>
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllStream(): Flow<List<User>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getOneStream(id: Int): Flow<User?>

    suspend fun insertOne(user: User)

    suspend fun deleteOne(user: User)

    suspend fun updateOne(user: User)

    suspend fun deleteAll()

    suspend fun insertMany(vararg users: User)
}
