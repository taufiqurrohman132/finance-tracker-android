package com.example.financetrackerapplication.domain.model

sealed class UserStatus {
    object LoggedOut: UserStatus()
    object Guest: UserStatus()
    object LoggedIn: UserStatus()
}