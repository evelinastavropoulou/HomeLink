import java.util.ArrayList;
import java.util.List;

public class RentalInterest {

    public static void getRentalInterests(String listingID) {
        System.out.println("[RentalInterest] Retrieved interests for listing: " + listingID);
    }

    public static void deleteAssociatedInterests(String listingID) {
        System.out.println("[RentalInterest] Deleted associated interests for listing: " + listingID);
    }

    public static List<String> getListingInterests(List<Listing> listings) {
        return ManageDB.queryInterestsByListings(listings);
    }

    public static List<String> getUserIdsFromInterests(List<Listing> listings) {
        return ManageDB.queryUserIdsFromInterests(listings);
    }


}
