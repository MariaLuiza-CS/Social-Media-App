package com.picpay.desafio.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.picpay.desafio.android.data.local.entity.PersonEntity
import com.picpay.desafio.android.data.local.entity.PersonWithPhotosEntity
import com.picpay.desafio.android.data.local.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PeopleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: PersonEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<PhotoEntity>)

    @Transaction
    suspend fun insertPersonWithPhotos(
        person: PersonEntity,
        photos: List<PhotoEntity>
    ) {
        val personId = insertPerson(person)
        val photosToInsert = photos.map {
            it.copy(
                personId = personId
            )
        }
        insertPhotos(photosToInsert)
    }

    @Transaction
    @Query("SELECT * FROM people")
    fun getAllPeopleWithPhotos(): Flow<List<PersonWithPhotosEntity>>

    @Query("DELETE FROM people")
    suspend fun clearPeople()
}
