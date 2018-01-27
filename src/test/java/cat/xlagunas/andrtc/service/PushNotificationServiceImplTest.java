package cat.xlagunas.andrtc.service;

import cat.xlagunas.andrtc.repository.PushNotificationRepository;
import cat.xlagunas.andrtc.repository.TokenRepository;
import cat.xlagunas.andrtc.repository.model.PushMessage;
import cat.xlagunas.andrtc.repository.model.PushMessageData;
import cat.xlagunas.andrtc.repository.model.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PushNotificationServiceImplTest {

    @Mock
    PushNotificationRepository pushNotificationRepository;
    @Mock
    TokenRepository tokenRepository;
    @Mock
    PushMessageData pushMessageData;
    @Captor
    ArgumentCaptor<PushMessage> pushMessageArgumentCaptor;

    private PushNotificationService pushNotificationService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        pushNotificationService = new PushNotificationServiceImpl(pushNotificationRepository, tokenRepository);
    }

    @Test
    public void sendPush() throws Exception {
        long fakeUserId = 1;
        List<Token> fakeTokenList = Arrays.asList(new Token.Builder().value("ABCD").build());
        PushMessage pushMessage = new PushMessage.Builder().tokenList(Arrays.asList("ABCD")).content(pushMessageData).build();
        when(tokenRepository.getUserListToken(Arrays.asList(fakeUserId))).thenReturn(fakeTokenList);

        pushNotificationService.sendPush(fakeUserId, pushMessageData);

        verify(tokenRepository).getUserListToken(Arrays.asList(fakeUserId));
        verify(pushNotificationRepository).sendPush(pushMessageArgumentCaptor.capture());
        assertThat(pushMessageArgumentCaptor.getValue()).isEqualToComparingFieldByField(pushMessage);
    }

}