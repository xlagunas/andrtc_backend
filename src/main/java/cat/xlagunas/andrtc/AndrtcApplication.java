package cat.xlagunas.andrtc;

import cat.xlagunas.andrtc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class AndrtcApplication {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Bean
    UserRepository provideUserRepository(JdbcTemplate template) {
        return new UserRepositoryImpl(template, new UserRowMapper());
    }

    @Bean
    RosterRepository provideRosterRepository(JdbcTemplate template) {
        return new RosterRepositoryImpl(template, new FriendRowMapper());
    }

    public static void main(String[] args) {
        SpringApplication.run(AndrtcApplication.class, args);
    }
}
