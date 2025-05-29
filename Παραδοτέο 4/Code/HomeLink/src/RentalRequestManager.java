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
        RentalRequestScreen screenToShow = new RentalRequestScreen();
        screenToShow.displayDeclarations(interests, userIds, trustScores, listings);
    }

    public static boolean validateTenantSelection(Listing listing, List<String> selectedUsers, RentalRequestScreen screen) {
        String[] info = listing.getTypeAndCapacity();
        String type = info[0];
        int maxCapacity = Integer.parseInt(info[1]);

        if (type.equals("Private") && selectedUsers.size() > 1) {
            String msg = Message.createErrorMessage("Το ακίνητο δεν επιτρέπει συγκατοίκηση.");
            screen.displayValidationResult(false, msg);
            return false;
        } else if (selectedUsers.size() > maxCapacity) {
            String msg = Message.createErrorMessage("Υπερβαίνετε τον μέγιστο αριθμό συγκατοίκων (" + maxCapacity + ").");
            screen.displayValidationResult(false, msg);
            return false;
        } else {
            String msg = Message.createSuccessMessage("Η επιλογή ενοικιαστών είναι έγκυρη.");
            screen.displayValidationResult(true, msg);
            return true;
        }
    }



    // Δεν χρειαζόμαστε User αντικείμενα, οπότε επιστρέφουμε απλά τα IDs
    public static List<String> getSelectedTenantUsers(List<String> userIds) {
        // Εδώ μπορείς να προσθέσεις λογική αν θέλεις π.χ. έλεγχος εγκυρότητας
        return new ArrayList<>(userIds);
    }

    public static void startRentalProcedure(RentalTerms rental, RentalRequestScreen screen) {
        System.out.println("\n🚀 Εκκίνηση Διαδικασίας Ενοικίασης");

        for (String userId : rental.getTenantIds()) {
            System.out.println("\n✔ Επιλεγμένος Ενοικιαστής: " + userId);
        }

        // 💾 Αποθήκευση όρων στη βάση
        ManageDB.saveRentalTerms(rental);

        // 🔹 Δημιουργία ειδοποίησης
        Notification notification = Notification.createRentalRequestNotification(rental, rental.getTenantIds());
        ManageDB.saveNotification(notification);

        // 🔹 Επιβεβαίωση στον χρήστη
        String success = Message.createSuccessMessage("\n✅ Η ενοικίαση καταχωρήθηκε επιτυχώς.");
        screen.displayMessage(success);
    }



}
