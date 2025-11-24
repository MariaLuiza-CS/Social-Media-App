package com.picpay.desafio.android.presentation.profile

sealed class ProfileEvent {
    data object LoadCurrentUser : ProfileEvent()
    data object LoadContactUserList : ProfileEvent()
    data object LoadPeopleWithPhotoList : ProfileEvent()
    data object SignOut : ProfileEvent()
}
