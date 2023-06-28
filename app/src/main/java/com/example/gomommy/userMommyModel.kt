package com.example.gomommy

data class userMommyModel(
    val loginCredentials: LoginCredentials,
    val userProfile: UserProfile
) {
    data class LoginCredentials(
        val email: String?,
        val password: String?
    )

    data class UserProfile(
        val userId: String?
    )
}


