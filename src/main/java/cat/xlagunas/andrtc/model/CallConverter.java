package cat.xlagunas.andrtc.model;

import cat.xlagunas.andrtc.repository.model.Conference;
import cat.xlagunas.andrtc.repository.model.JoinedConferenceAttendee;

public class CallConverter {
    public static CallDto convertFrom(Conference conference) {
        return new CallDto(conference.callId, conference.date);
    }


    public static Conference convertFrom(CallDto callDto) {
        return new Conference(callDto.callId, callDto.date);
    }

    public static CallDetailsDto convertFrom(JoinedConferenceAttendee attendee) {
        return new CallDetailsDto.Builder()
                .isStarter(attendee.isStarter)
                .id(attendee.roster.id)
                .username(attendee.roster.username)
                .name(attendee.roster.name)
                .email(attendee.roster.email)
                .profilePic(attendee.roster.profilePic)
                .status(attendee.roster.status)
                .build();
    }
}
