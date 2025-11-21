package com.picpay.desafio.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.picpay.desafio.android.data.local.entity.ContactUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContactUsersList(contactUsersList: List<ContactUserEntity>)

    @Query("SELECT * FROM contact_users")
    fun getContactUsersList(): Flow<List<ContactUserEntity>>

    @Query("DELETE FROM contact_users")
    suspend fun cleanContactUsersList()
}
