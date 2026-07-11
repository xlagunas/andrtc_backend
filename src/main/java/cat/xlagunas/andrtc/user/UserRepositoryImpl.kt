package cat.xlagunas.andrtc.user

import java.sql.Time
import java.sql.Timestamp
import java.util.Calendar
import java.util.HashMap
import java.util.Optional

import org.springframework.dao.DuplicateKeyException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.security.crypto.password.PasswordEncoder

import cat.xlagunas.andrtc.common.UserRowMapper

class UserRepositoryImpl(private val jdbcTemplate: JdbcTemplate,
                         private val rowMapper: UserRowMapper,
                         private val passwordEncoder: PasswordEncoder) : UserRepository {

    @Throws(ExistingUserException::class)
    override fun insertUser(user: User): Long {
        try {
            val simpleJdbcInsert = SimpleJdbcInsert(jdbcTemplate)
            simpleJdbcInsert.withTableName("USER")
                    .usingGeneratedKeyColumns("ID")
            val parameters = HashMap<String, Any>()
            parameters["ID"] = user.id
            parameters["EMAIL"] = user.email
            parameters["USERNAME"] = user.username
            parameters["FIRST_NAME"] = user.firstname
            parameters["LAST_NAME"] = user.lastname
            parameters["PASSWORD"] = passwordEncoder.encode(user.password)
            parameters["PROFILE_PIC"] = user.profilePic ?: ""
            parameters["LAST_PASSWORD_UPDATE"] = Timestamp.from(Calendar.getInstance().toInstant())

            return simpleJdbcInsert.executeAndReturnKey(parameters).toLong()
        } catch (ex: DuplicateKeyException) {
            throw ExistingUserException("A user with same username already exists in database", ex)
        }

    }

    @Throws(UserNotFoundException::class)
    override fun findUser(userId: Long): User {
        try {
            return jdbcTemplate.queryForObject(FIND_USER_BY_ID, arrayOf(userId), rowMapper.insertUserRowMapper)
        } catch (ex: EmptyResultDataAccessException) {
            throw UserNotFoundException(String.format("User with id %s not found in database", userId), ex)
        }

    }

    @Throws(UserNotFoundException::class)
    override fun findUser(username: String): User {
        try {
            return jdbcTemplate.queryForObject(FIND_USER_BY_USERNAME, arrayOf(username), rowMapper.insertUserRowMapper)
        } catch (ex: EmptyResultDataAccessException) {
            throw UserNotFoundException(String.format("User with username %s not found in database", username), ex)
        }

    }

    override fun findUsers(username: String): List<User> {
        return jdbcTemplate.query(SEARCH_USERS_BY_USERNAME, arrayOf("%$username%"), rowMapper.searchUsersRowMapper)
    }

    override fun updatePassword(userId: Long, password: String): Boolean {
        val currentTime = Time.from(Calendar.getInstance().toInstant())
        val affectedRows = jdbcTemplate.update(UPDATE_PASSWORD, passwordEncoder.encode(password), currentTime, userId)
        return affectedRows > 0
    }

    override fun updateProfilePic(userId: Long, profilePic: String): Boolean {
        val affectedRows = jdbcTemplate.update(UPDATE_PROFILE_PIC, profilePic, userId)
        return affectedRows > 0
    }

    override fun findUserOptional(userId: Long): Optional<User> {
        return try {
            Optional.of(findUser(userId))
        } catch (ex: UserNotFoundException) {
            Optional.empty()
        }
    }

    companion object {
        private val FIND_USER_BY_ID = "SELECT * FROM USER WHERE ID = ?"
        private val FIND_USER_BY_USERNAME = "SELECT * FROM USER WHERE USERNAME = ?"
        private val SEARCH_USERS_BY_USERNAME = "SELECT * FROM USER WHERE USERNAME LIKE ?"
        private val UPDATE_PASSWORD = "UPDATE USER SET PASSWORD = ?, LAST_PASSWORD_UPDATE = ? WHERE ID = ?"
        private val UPDATE_PROFILE_PIC = "UPDATE USER SET PROFILE_PIC = ? WHERE ID = ?"
    }

}
