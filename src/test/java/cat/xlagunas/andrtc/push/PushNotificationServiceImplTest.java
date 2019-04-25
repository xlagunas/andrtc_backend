package cat.xlagunas.andrtc.push;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

import cat.xlagunas.andrtc.token.Token;

public class PushNotificationServiceImplTest {

    @Mock
    PushNotificationRepository pushNotificationRepository;
    @Mock
    PushMessageData pushMessageData;
    @Captor
    ArgumentCaptor<PushMessage> pushMessageArgumentCaptor;

    private PushNotificationService pushNotificationService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        pushNotificationService = new PushNotificationServiceImpl(pushNotificationRepository);
    }

    @Test
    public void sendPush() throws Exception {
        long fakeUserId = 1;
        List<Token> fakeTokenList = Arrays.asList(new Token.Builder().value("ABCD").build());
        PushMessage pushMessage = new PushMessage(Arrays.asList("ABCD"), pushMessageData);

        pushNotificationService.sendPush(Lists.transform(fakeTokenList, token -> token.value), pushMessageData);

        verify(pushNotificationRepository).sendPush(pushMessageArgumentCaptor.capture());
        assertThat(pushMessageArgumentCaptor.getValue()).isEqualToComparingFieldByField(pushMessage);
    }

}