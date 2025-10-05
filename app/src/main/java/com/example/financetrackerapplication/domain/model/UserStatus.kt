package com.example.financetrackerapplication.domain.model

sealed class UserStatus(val value: String) {
    data object LoggedOut: UserStatus("logged_out")
    data object Anonymous: UserStatus("anonymous")
    data object LoggedIn: UserStatus("logged_in")

    companion object{
        // Konversi ke Enum / Model → supaya gampang dipakai di ViewModel/ UI:
        fun fromString(value: String?): UserStatus{
            return when(value){
                LoggedOut.value -> LoggedOut
                Anonymous.value -> Anonymous
                LoggedIn.value -> LoggedIn
                else -> LoggedOut
            }
        }
    }
}