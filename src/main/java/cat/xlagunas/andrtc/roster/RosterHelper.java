package cat.xlagunas.andrtc.roster;

import cat.xlagunas.andrtc.roster.FriendshipStatus;
import cat.xlagunas.andrtc.roster.Roster;

public class RosterHelper {
    public static Roster updateRelationship(Roster roster, String relationStatus) {
        return new Roster.Builder(roster)
                .relationStatus(relationStatus)
                .build();
    }

    public static Roster createRequestFriendship(long ownerId, long contactId) {
        return new Roster.Builder()
                .owner(ownerId)
                .contact(contactId)
                .relationStatus(FriendshipStatus.REQUESTED.name())
                .build();
    }

    public static Roster createPendingFriendship(long ownerId, long contactId) {
        return new Roster.Builder()
                .owner(ownerId)
                .contact(contactId)
                .relationStatus(FriendshipStatus.PENDING.name())
                .build();
    }

    public static Roster acceptFriendship(Roster roster) {
        return updateRelationship(roster, FriendshipStatus.ACCEPTED.name());
    }
}
