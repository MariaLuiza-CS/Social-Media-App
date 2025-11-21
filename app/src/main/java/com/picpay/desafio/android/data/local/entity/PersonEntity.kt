package com.picpay.desafio.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "people")
data class PersonEntity(
    @PrimaryKey val id: Int,
    val fistName: String,
    val lastName: String,
    val title: String,
    val gender: String,
    val email: String,
    val profilePicture: String,
)
