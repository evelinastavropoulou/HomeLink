import java.util.ArrayList;
import java.util.List;

public class RentalInterest {

    public static void getRentalInterests(String listingID) {
        System.out.println("[RentalInterest] Retrieved interests for listing: " + listingID);
    }

    public static void deleteAssociatedInterests(String listingID) {
        System.out.println("[RentalInterest] Deleted associated interests for listing: " + listingID);
    }

    // Έλεγχος αν υπάρχει τουλάχιστον ένα interest για κάποιο από τα listings
    public static boolean getListingInterests(List<Listing> listings) {
        for (Listing listing : listings) {
            List<String> interests = ManageDB.queryInterestsByListingId(listing.getId());
            if (interests != null && !interests.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // Επιστροφή interests για ένα συγκεκριμένο listing
    public static List<String> getUserIDsForListing(String listingID) {
        return ManageDB.queryInterestsByListingId(listingID);
    }

    // ✅ Συγκέντρωση όλων των χρηστών που έδειξαν ενδιαφέρον για οποιοδήποτε listing
    public static List<String> getUserIdsFromInterests(List<Listing> listings) {
        List<String> allUserIds = new ArrayList<>();

        for (Listing listing : listings) {
            List<String> userIds = getUserIDsForListing(listing.getId());
            if (userIds != null) {
                allUserIds.addAll(userIds);
            }
        }

        return allUserIds;
    }
}
