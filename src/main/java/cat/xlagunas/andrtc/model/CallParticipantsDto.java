package cat.xlagunas.andrtc.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CallParticipantsDto {


    private final List<RosterDto> participants;

    @JsonCreator
    public CallParticipantsDto(@JsonProperty("participants") List<RosterDto> participants) {
        this.participants = participants;
    }

    public List<RosterDto> getParticipants() {
        return participants;
    }
}
