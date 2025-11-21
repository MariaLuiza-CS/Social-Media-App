package com.picpay.desafio.android.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PersonWithPhotosEntity(
    @Embedded
    val person: PersonEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "personId"
    )
    val photos: List<PhotoEntity>
)
