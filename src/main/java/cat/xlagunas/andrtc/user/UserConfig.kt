package cat.xlagunas.andrtc.user

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

import cat.xlagunas.andrtc.common.UserRowMapper

@Configuration
class UserConfig {

    @Bean
    fun provideUserRepository(template: JdbcTemplate, encoder: PasswordEncoder): UserRepository {
        return UserRepositoryImpl(template, UserRowMapper(), encoder)
    }

    @Bean
    fun provideUserService(userRepository: UserRepository): UserService {
        return UserServiceImpl(userRepository)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(7)
    }

}
