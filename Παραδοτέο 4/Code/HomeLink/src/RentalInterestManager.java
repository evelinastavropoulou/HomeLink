import java.util.Scanner;
import java.util.List;
import java.util.*;


public class RentalInterestManager {

    public boolean checkExistingInterest(String userID, String listingID) {
        return ManageDB.interestExists(userID, listingID);
    }

    public Map<String, String> getInterestDetails(String userID, String listingID) {
        return ManageDB.getInterest(userID, listingID);
    }

    public void createInterest(String userID, String listingID, String message) {
        ManageDB.insertInterest(userID, listingID, message);
    }

    public Listing fetchFullListingDetails(String listingID) {
        return ManageDB.getListingDetails(listingID);
    }
}
