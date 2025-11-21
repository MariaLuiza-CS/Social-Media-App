package com.picpay.desafio.android.domain.util

import com.google.firebase.auth.FirebaseUser
import com.picpay.desafio.android.data.local.entity.UserEntity
import com.picpay.desafio.android.data.local.entity.ContactUserEntity
import com.picpay.desafio.android.data.local.entity.PersonEntity
import com.picpay.desafio.android.data.remote.dto.ContactUserResponseDto
import com.picpay.desafio.android.data.remote.dto.PeopleResponseDto
import com.picpay.desafio.android.data.remote.dto.PersonResponseDto
import com.picpay.desafio.android.domain.model.ContactUser
import com.picpay.desafio.android.domain.model.User

fun ContactUserEntity.toUser() =
    User(
        id = id,
        name = name,
        img = img,
        username = username
    )

fun ContactUserResponseDto.toUserEntity() =
    ContactUserEntity(
        id = id ?: "",
        name = name,
        img = img,
        username = username
    )

fun FirebaseUser.toAuthenticationPersonEntity() =
    UserEntity(
        id = uid,
        name = displayName ?: "",
        email = email ?: "",
        img = photoUrl.toString()
    )

fun UserEntity.toAuthenticationUser() =
    ContactUser(
        id = id,
        name = name ?: "",
        email = email ?: "",
        img = img ?: ""
    )

fun PeopleResponseDto.toPersonEntity() =
    PersonEntity
