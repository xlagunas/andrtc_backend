package cat.xlagunas.andrtc;

import cat.xlagunas.andrtc.repository.*;
import cat.xlagunas.andrtc.repository.rowmapper.ConferenceRowMapper;
import cat.xlagunas.andrtc.repository.rowmapper.RosterRowMapper;
import cat.xlagunas.andrtc.repository.rowmapper.UserRowMapper;
import cat.xlagunas.andrtc.service.*;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import retrofit2.Retrofit;
import retrofit2.adapter.java8.Java8CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@SpringBootApplication
@EnableWebMvc
public class AndrtcApplication {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Value("${firebase.push.url}")
    String baseUrl;

    @Value("${firebase.push.authentication}")
    String firebaseKeyword;

    public static void main(String[] args) {
        SpringApplication.run(AndrtcApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(7);
    }

    @Bean
    UserService provideUserService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Bean
    RosterService provideRosterService(RosterRepository rosterRepository) {
        return new RosterServiceImpl(rosterRepository);
    }

    @Bean
    UserRepository provideUserRepository(JdbcTemplate template, PasswordEncoder encoder) {
        return new UserRepositoryImpl(template, new UserRowMapper(), encoder);
    }

    @Bean
    RosterRepository provideRosterRepository(NamedParameterJdbcTemplate template) {
        return new RosterRepositoryImpl(template, new RosterRowMapper());
    }

    @Bean
    CallRepository provideCallRepository(NamedParameterJdbcTemplate template) {
        return new CallRepositoryImpl(template, new ConferenceRowMapper());
    }

    @Bean
    TokenRepository provideTokenRepository(NamedParameterJdbcTemplate template) {
        return new TokenRepositoryImpl(template);
    }

    @Bean
    TokenService provideTokenService(TokenRepository tokenRepository) {
        return new TokenServiceImpl(tokenRepository);
    }

    @Bean
    CallService provideCallService(CallRepository callRepository) {
        return new CallServiceImpl(callRepository);
    }

    @Bean
    PushNotificationService providePushNotificationService(PushNotificationRepository pushNotificationRepository, TokenRepository tokenRepository) {
        return new PushNotificationServiceImpl(pushNotificationRepository, tokenRepository);
    }

    @Bean
    OkHttpClient provideOkClient(Interceptor interceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.interceptors().add(httpLoggingInterceptor);
        return builder.build();
    }

    @Bean
    PushNotificationRepository providePushRepository(OkHttpClient client) {
        Retrofit retrofit = new Retrofit.Builder().addCallAdapterFactory(Java8CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(PushNotificationRepository.class);
    }

    @Bean
    Interceptor provideAuthenticationInterceptor() {
        return chain -> {
            Request.Builder requestBuilder = chain.request().newBuilder();
            requestBuilder
                    .header("Content-Type", "application/json")
                    .header("Authorization", firebaseKeyword);
            return chain.proceed(requestBuilder.build());
        };
    }
}
