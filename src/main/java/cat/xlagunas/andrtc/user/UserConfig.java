package cat.xlagunas.andrtc.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import cat.xlagunas.andrtc.common.UserRowMapper;

@Configuration
public class UserConfig {

    @Bean
    UserRepository provideUserRepository(JdbcTemplate template, PasswordEncoder encoder) {
        return new UserRepositoryImpl(template, new UserRowMapper(), encoder);
    }

    @Bean
    UserService provideUserService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(7);
    }

}
