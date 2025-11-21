package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.domain.model.Result
import com.picpay.desafio.android.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ContactUserRepository {
    fun getContactUsers(): Flow<Result<List<User>>>
}
