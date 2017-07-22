package cat.xlagunas.andrtc;

import cat.xlagunas.andrtc.repository.*;
import cat.xlagunas.andrtc.repository.rowmapper.RosterRowMapper;
import cat.xlagunas.andrtc.repository.rowmapper.UserRowMapper;
import cat.xlagunas.andrtc.service.UserService;
import cat.xlagunas.andrtc.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class AndrtcApplication {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    PasswordEncoder encoder;

    @Bean
    UserService provideUserService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Bean
    UserRepository provideUserRepository(JdbcTemplate template) {
        return new UserRepositoryImpl(template, new UserRowMapper(), encoder);
    }

    @Bean
    RosterRepository provideRosterRepository(NamedParameterJdbcTemplate template) {
        return new RosterRepositoryImpl(template, new RosterRowMapper());
    }

    public static void main(String[] args) {
        SpringApplication.run(AndrtcApplication.class, args);
    }
}
