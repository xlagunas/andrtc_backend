package cat.xlagunas.andrtc.user

object UserConverter {

    fun convert(user: User): UserDto {
        return UserDto.Builder()
                .id(user.id)
                .username(user.username)
                .firstname(user.firstname)
                .lastname(user.lastname)
                .password(user.password)
                .profilePic(user.profilePic)
                .email(user.email)
                .passwordUpdate(user.modifiedDate)
                .build()
    }

    fun convert(user: UserDto): User {
        return User(
                id = user.id,
                username = user.username,
                firstname = user.firstname,
                lastname = user.lastname,
                password = user.password,
                profilePic = user.profilePic,
                email = user.email,
                modifiedDate = user.passwordUpdate)
    }
}
