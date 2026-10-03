package com.pemmob.smartcanteen.data.model

enum class UserRole {
    MAHASISWA,
    PENJUAL
}

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val qrisImageUrl: String? = null
)