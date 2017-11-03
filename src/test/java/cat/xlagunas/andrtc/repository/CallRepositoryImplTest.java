package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.repository.model.Conference;
import cat.xlagunas.andrtc.repository.rowmapper.ConferenceRowMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CallRepositoryImplTest {

    private CallRepository callRepository;

    @Autowired
    NamedParameterJdbcTemplate template;

    @Before
    public void setup() {

        callRepository = new CallRepositoryImpl(template, new ConferenceRowMapper());
    }

    @Test
    public void createCall() throws Exception {
        String callId = callRepository.createCall(1);

        assertThat(callId).isNotNull();
        Conference conference = callRepository.getConferenceById(callId);
        assertThat(conference).isNotNull();

    }

    @Test
    public void joinCall() throws Exception {
    }

    @Test
    public void rejectCall() throws Exception {
    }

}