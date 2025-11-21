package com.picpay.desafio.android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.picpay.desafio.android.data.local.dao.ContactUserDao
import com.picpay.desafio.android.data.local.dao.PeopleDao
import com.picpay.desafio.android.data.local.dao.UserDao
import com.picpay.desafio.android.data.local.entity.ContactUserEntity
import com.picpay.desafio.android.data.local.entity.PersonEntity
import com.picpay.desafio.android.data.local.entity.PhotoEntity
import com.picpay.desafio.android.data.local.entity.UserEntity

@Database(
    entities = [
        ContactUserEntity::class,
        UserEntity::class,
        PersonEntity::class,
        PhotoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ConnectionsAppDataBase : RoomDatabase() {
    abstract fun userDao(): ContactUserDao
    abstract fun authenticationDao(): UserDao
    abstract fun peopleDao(): PeopleDao
}
