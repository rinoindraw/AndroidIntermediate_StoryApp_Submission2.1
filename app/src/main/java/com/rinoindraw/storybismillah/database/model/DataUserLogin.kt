package com.rinoindraw.storybismillah.database.model

data class DataUserLogin(
    val token: String,
    val email: String,
    val password: String
)