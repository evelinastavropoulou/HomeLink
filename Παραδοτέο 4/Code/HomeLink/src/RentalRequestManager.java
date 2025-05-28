import java.util.*;

public class RentalRequestManager {

    public static void startRentalProcess(MainScreen screen, String ownerID) {
        List<Listing> listings = ManageDB.getListingsByOwner(ownerID);

        if (listings == null || listings.isEmpty()) {
            screen.displayMessage("[ERROR] No Listings Found.");
            return;
        }

        boolean foundInterest = RentalInterest.getListingInterests(listings);
        if (!foundInterest) {
            screen.displayMessage("[ERROR] No Interest Declarations Found.");
            return;
        }

        List<String> userIds = RentalInterest.getUserIdsFromInterests(listings);
        if (userIds == null || userIds.isEmpty()) {
            screen.displayMessage("[ERROR] Δεν βρέθηκαν χρήστες με ενδιαφέρον.");
            return;
        }

        Map<String, Integer> trustScores = TrustScore.getTrustScore(userIds);

        screen.displayMessage("✅ Διαδικασία ενοικίασης ξεκίνησε. Βρέθηκαν οι εξής ενδιαφερόμενοι:\n");

        // → Κλήση της νέας οθόνης εμφάνισης
        RentalRequestScreen requestScreen = new RentalRequestScreen();
        requestScreen.displayDeclarations(trustScores, listings.get(0));
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
