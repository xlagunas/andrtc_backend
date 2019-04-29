package cat.xlagunas.andrtc.user

import java.time.Instant

object UserTestBuilder {

    val user: User
        get() = getUser(1, "aPass", "https://google.com/aPic2")

    fun getUserWithId(id: Int): User {
        return getUser(id, "aPass", "https://google.com/aPic2")
    }

    fun getUserWithPass(pass: String): User {
        return getUser(1, pass, "https://google.com/aPic2")
    }

    fun getUserWithProfilePic(profilePic: String): User {
        return getUser(1, "aPass", profilePic)
    }

    fun getUser(id: Int, password: String, profilePic: String): User {
        return User(id = id.toLong(),
                username = "xlagunas" + Instant.now() + password,
                firstname = "Xavier",
                lastname = "Lagunas Calpe",
                email = System.currentTimeMillis().toString() + "xlagunas@gmail.com" + password,
                password = password,
                profilePic = profilePic,
                modifiedDate = null)

    }
}