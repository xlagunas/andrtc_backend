package cat.xlagunas.andrtc.repository;

import cat.xlagunas.andrtc.exception.ExistingUserException;
import cat.xlagunas.andrtc.repository.model.Conference;
import cat.xlagunas.andrtc.repository.model.JoinedConferenceAttendee;
import cat.xlagunas.andrtc.repository.rowmapper.ConferenceRowMapper;
import cat.xlagunas.andrtc.repository.rowmapper.UserRowMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CallRepositoryImplTest {

    @Autowired
    NamedParameterJdbcTemplate template;
    @Autowired
    UserRepository userRepositoryImpl;
    @Autowired
    JdbcTemplate jdbcTemplate;
    PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
    long callerId;
    long calleeId;
    long anotherCalleeId;
    private CallRepository callRepository;

    @Before
    public void setup() throws ExistingUserException {
        userRepositoryImpl = new UserRepositoryImpl(jdbcTemplate, new UserRowMapper(), encoder);
        callRepository = new CallRepositoryImpl(template, new ConferenceRowMapper());

        callerId = userRepositoryImpl.insertUser(UserTestBuilder.getUser());
        calleeId = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(12));
        anotherCalleeId = userRepositoryImpl.insertUser(UserTestBuilder.getUserWithId(25));

    }

    @Test
    public void createAndQueryCall() throws Exception {
        Conference call = callRepository.createCall();

        assertThat(call.callId).isNotNull();
        Conference conference = callRepository.getConferenceById(call.callId);
        assertThat(conference).isNotNull();
        assertThat(conference.callId).isEqualTo(call.callId);
    }

    @Test
    public void joinCall() throws Exception {
        Conference call = callRepository.createCall();
        callRepository.updateCallContact(call.callId, calleeId, true, CallRepository.Status.ACCEPTED);
        callRepository.updateCallContact(call.callId, callerId, false, CallRepository.Status.UNATTENDED);
        callRepository.updateCallContact(call.callId, anotherCalleeId, false, CallRepository.Status.UNATTENDED);

        List<JoinedConferenceAttendee> totalUsersIncall = callRepository.getAttendees(call.callId, callerId);
        assertThat(totalUsersIncall).hasSize(2);
    }

}