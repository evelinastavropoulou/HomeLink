import java.util.*;

public class UserAvailability {
    public static void createTimeslots(String userID, String listingID, List<String> timeSlots) {
        ManageDB.saveUserAvailability(userID, listingID, timeSlots);
    }

    public static List<String> getOwnerAvailability(String ownerID) {
        return ManageDB.queryOwnerAvailability(ownerID);
    }

    public static List<String> compareWith(List<String> userSlots, List<String> ownerSlots) {
        List<String> common = new ArrayList<>();
        for (String slot : userSlots) {
            if (ownerSlots.contains(slot)) {
                common.add(slot);
            }
        }
        return common;
    }

}
