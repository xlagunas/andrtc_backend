package cat.xlagunas.andrtc.token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TokenServiceImplTest {

    private final static int USER_ID = 10;
    private final static String VALUE = "ASDFG1234512";
    private final static String PLATFORM = "ANDROID";
    @Mock
    TokenRepository tokenRepository;
    private TokenService tokenService;

    private static Token getToken() {
        Token token = new Token.Builder()
                .userId(USER_ID)
                .platform(PLATFORM)
                .value(VALUE)
                .build();
        return token;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tokenService = new TokenServiceImpl(tokenRepository);
    }

    @Test
    public void whenAddingNewToken_thenNewTokenValid() throws Exception {
        tokenService.addToken(getToken());

        verify(tokenRepository).addToken(USER_ID, VALUE, PLATFORM);
    }

    @Test
    public void whenRemovingNewToken_thenDeleted() throws Exception {
        tokenService.removeToke(getToken());

        verify(tokenRepository).removeToken(USER_ID, VALUE);
    }

    @Test
    public void getUserToken() throws Exception {
        when(tokenRepository.getUserTokens(USER_ID)).thenReturn(Arrays.asList(getToken()));

        List<String> tokenList = tokenService.getUsersToken(USER_ID);

        assertThat(tokenList.get(0)).contains(VALUE);
    }

}