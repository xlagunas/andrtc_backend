package cat.xlagunas.andrtc.call;

public class CallConverter {
    public static CallDto convertFrom(Conference conference) {
        return new CallDto(conference.callId, conference.date);
    }


    public static Conference convertFrom(CallDto callDto) {
        return new Conference(callDto.getCallId(), callDto.getDate());
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
