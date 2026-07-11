package cat.xlagunas.andrtc.user

import java.time.Instant

data class User(val username: String, val firstname: String,
    val lastname: String,
    val email: String,
    val profilePic: String?,
    val password: String?,
    val id: Long = 0,
    val modifiedDate: Instant?)
