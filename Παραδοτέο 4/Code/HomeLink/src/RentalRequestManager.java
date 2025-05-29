import java.util.*;

public class RentalRequestManager {

    public static void startRentalProcess(MainScreen screen, String ownerID) {

        List<Listing> listings = ManageDB.getListingsByOwner(ownerID);

        if (listings == null || listings.isEmpty()) {
            screen.displayMessage("[ERROR] No Listings Found.");
            return;
        }

        List<String> interests = RentalInterest.getListingInterests(listings);
        if (interests.isEmpty()) {
            screen.displayMessage("[ERROR] No Interest Declarations Found.");
            return;
        }
        
        // 👉 Εδώ παίρνεις τα userIds από τις δηλώσεις
        List<String> userIds = RentalInterest.getUserIdsFromInterests(listings);
        if (userIds == null || userIds.isEmpty()) {
            screen.displayMessage("[ERROR] Δεν βρέθηκαν χρήστες με ενδιαφέρον.");
            return;
        }

        // 👉 Τώρα παίρνεις τα trust scores
        Map<String, Integer> trustScores = TrustScore.getTrustScore(userIds);

        // ✅ Από εδώ μπορείς να συνεχίσεις με εμφάνιση ή επιλογή χρηστών
        RentalRequestScreen requestScreen = new RentalRequestScreen();
        requestScreen.displayDeclarations(interests, userIds, trustScores);

    }

    public static void validateTenantSelection(Listing listing, List<String> selectedUsers, RentalRequestScreen screen) {
        String[] info = listing.getTypeAndCapacity();
        String type = info[0];
        int maxCapacity = Integer.parseInt(info[1]);

        if (type.equals("Private") && selectedUsers.size() > 1) {
            String msg = Message.createErrorMessage("Το ακίνητο δεν επιτρέπει συγκατοίκηση.");
            screen.displayValidationResult(false, msg);
        } else if (selectedUsers.size() > maxCapacity) {
            String msg = Message.createErrorMessage("Υπερβαίνετε τον μέγιστο αριθμό συγκατοίκων (" + maxCapacity + ").");
            screen.displayValidationResult(false, msg);
        } else {
            String msg = Message.createSuccessMessage("Η επιλογή ενοικιαστών είναι έγκυρη.");
            screen.displayValidationResult(true, msg);
        }
    }


}
