package cat.xlagunas.andrtc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;

import cat.xlagunas.andrtc.call.CallRepository;
import cat.xlagunas.andrtc.call.CallRepositoryImpl;
import cat.xlagunas.andrtc.call.CallService;
import cat.xlagunas.andrtc.call.CallServiceImpl;
import cat.xlagunas.andrtc.call.ConferenceRowMapper;
import cat.xlagunas.andrtc.push.PushNotificationRepository;
import cat.xlagunas.andrtc.push.PushNotificationService;
import cat.xlagunas.andrtc.push.PushNotificationServiceImpl;
import cat.xlagunas.andrtc.roster.RosterRepository;
import cat.xlagunas.andrtc.roster.RosterRepositoryImpl;
import cat.xlagunas.andrtc.roster.RosterRowMapper;
import cat.xlagunas.andrtc.roster.RosterService;
import cat.xlagunas.andrtc.roster.RosterServiceImpl;
import cat.xlagunas.andrtc.token.TokenRepository;
import cat.xlagunas.andrtc.token.TokenRepositoryImpl;
import cat.xlagunas.andrtc.token.TokenService;
import cat.xlagunas.andrtc.token.TokenServiceImpl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
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
    RosterService provideRosterService(RosterRepository rosterRepository) {
        return new RosterServiceImpl(rosterRepository);
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
        return new PushNotificationServiceImpl(pushNotificationRepository);
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
        ObjectMapper objectMapper = new ObjectMapper().registerModules(new KotlinModule());
        Retrofit retrofit = new Retrofit.Builder().addCallAdapterFactory(Java8CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
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
