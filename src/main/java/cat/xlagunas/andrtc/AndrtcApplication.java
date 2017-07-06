package cat.xlagunas.andrtc;

import cat.xlagunas.andrtc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;

@SpringBootApplication
public class AndrtcApplication {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Bean
    UserRepository provideUserRepository(JdbcTemplate template) {
        return new UserRepository(template, new UserRowMapper());
    }

    @Bean
    RosterRepository provideRosterRepository(JdbcTemplate template) {
        return new RosterRepository(template, new FriendRowMapper());
    }

    public static void main(String[] args) {
        SpringApplication.run(AndrtcApplication.class, args);
    }
}
