package cat.xlagunas.andrtc.token;

import cat.xlagunas.andrtc.user.ExistingUserException;
import cat.xlagunas.andrtc.user.UserRepository;
import cat.xlagunas.andrtc.user.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenRepositoryImplTest {

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    UserRepository userRepositoryImpl;

    @Autowired
    PasswordEncoder encoder;

    long userId;


    @Before
    public void setup() {
        try {
            userId = userRepositoryImpl.insertUser(new User.Builder().username("aUsername").password("aPassword").email("anEmail").firstname("aFirstName").build());
        } catch (ExistingUserException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Transactional
    @Rollback
    public void addToken() throws Exception {
        Token token = new Token.Builder().userId(userId).value("aToken").platform("ANDROID").build();

        tokenRepository.addToken(token.userId, token.value, token.platform);

        List<Token> tokens = tokenRepository.getUserTokens(userId);
        assertThat(tokens).hasSize(1);
        assertThat(tokens.get(0)).isEqualToIgnoringGivenFields(token, "id");
    }

    @Test
    @Transactional
    @Rollback
    public void getUserTokens() throws Exception {
        Token.Builder builder = new Token.Builder().userId(userId).value("aToken").platform("ANDROID");
        Token token = builder.build();
        Token token2 = builder.value("anotherToken").build();
        Token token3 = builder.value("anotherToken2").build();
        Token token4 = builder.value("anotherToken3").build();
        tokenRepository.addToken(token.userId, token.value, token.platform);
        tokenRepository.addToken(token2.userId, token2.value, token2.platform);
        tokenRepository.addToken(token3.userId, token3.value, token3.platform);
        tokenRepository.addToken(token4.userId, token4.value, token4.platform);

        List<Token> tokens = tokenRepository.getUserTokens(userId);

        assertThat(tokens).hasSize(4);
    }

    @Test
    @Transactional
    @Rollback
    public void removeToken() throws Exception {
        Token.Builder builder = new Token.Builder().userId(userId).value("aToken").platform("ANDROID");
        Token token = builder.build();
        tokenRepository.addToken(token.userId, token.value, token.platform);
        Token token2 = builder.value("anotherToken").build();
        tokenRepository.addToken(token2.userId, token2.value, token2.platform);
        Token token3 = builder.value("anotherToken2").build();
        tokenRepository.addToken(token3.userId, token3.value, token3.platform);
        Token token4 = builder.value("anotherToken3").build();
        tokenRepository.addToken(token4.userId, token4.value, token4.platform);
        List<Token> tokens = tokenRepository.getUserTokens(userId);
        assertThat(tokens).hasSize(4);

        assertThat(tokenRepository.removeToken(userId, token.value)).isTrue();
        assertThat(tokenRepository.getUserTokens(userId)).hasSize(3);
    }

}