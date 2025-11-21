package com.picpay.desafio.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_users")
data class ContactUserEntity(
    @PrimaryKey val id: String,
    val name: String?,
    val username: String?,
    val img: String?
)
