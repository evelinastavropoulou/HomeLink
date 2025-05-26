import java.util.*;

public class CreateListingManager {
    private ManageDB db = new ManageDB();
    private MainScreen screen;

    public CreateListingManager(MainScreen screen) {
        this.screen = screen;
    }

    public void startCreateListingProcess(String ownerID) {
        // Βήμα 2: Ανάκτηση αγγελιών από βάση
        List<Listing> listings = db.getListingsForOwner(ownerID);

        // Βήμα 3: Φιλτράρισμα ενεργών
        List<Listing> active = Listing.filterActiveListings(listings);

        // Βήμα 4: Έλεγχος αν υπερβαίνει το όριο
        if (!checkListingLimit(active)) {
            screen.displayMessage("Έχετε φτάσει το όριο ενεργών αγγελιών. Παρακαλώ αρχειοθετήστε μία.");
            return;
        }

        // Βήμα 6: Εμφάνιση φόρμας καταχώρησης
        Listing newListing = CreateListingForm.fillListingForm();

        // Βήμα 8: Έλεγχος πληρότητας πεδίων
        if (!validateRequiredFields(newListing)) {
            screen.displayMessage("Σφάλμα: Ο μέγιστος αριθμός συγκατοίκων πρέπει να είναι θετικός.");
            return;
        }

        // Βήμα 9: Συμβατότητα συγκατοίκων - δωματίων
        if (!validateRoommateCompatibility(newListing)) {
            screen.displayMessage("Σφάλμα: Ο αριθμός συγκατοίκων υπερβαίνει τα διαθέσιμα δωμάτια.");
            return;
        }

        // Βήμα 11: Μεταφόρτωση φωτογραφιών
        List<String> photos = UploadPhotoForm.uploadPhotos();

        // Βήμα 13: Έλεγχος εγκυρότητας φωτογραφιών
        if (!validatePhotos(photos)) {
            screen.displayMessage("Σφάλμα: Επιτρεπτοί τύποι φωτογραφιών είναι .jpg και .png.");
            return;
        }

        // Βήμα 15: Αποθήκευση αγγελίας
        db.saveListing(newListing);

        // Βήμα 16: Εμφάνιση επιτυχίας
        screen.displayMessage("Η αγγελία καταχωρήθηκε με επιτυχία.");

        // Βήμα 17: Ενεργοποίηση τοποθεσίας
        LocationManager.triggerLocationEntry(newListing.getId());
    }

    public boolean validateRequiredFields(Listing listing) {
        return listing != null && listing.getMaxRoommates() > 0;
    }

    public boolean validateRoommateCompatibility(Listing listing) {
        return listing.getMaxRoommates() <= listing.getRooms();
    }

    public boolean validatePhotos(List<String> photos) {
        for (String photo : photos) {
            if (!photo.endsWith(".jpg") && !photo.endsWith(".png")) {
                return false;
            }
        }
        return true;
    }

    public boolean checkListingLimit(List<Listing> activeListings) {
        int MAX_ACTIVE_LISTINGS = 3;
        return activeListings.size() < MAX_ACTIVE_LISTINGS;
    }
}
