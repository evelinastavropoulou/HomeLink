import java.util.*;

public class CreateListingManager {
    private ManageDB db = new ManageDB();
    private CreateListingScreen screen;

    public CreateListingManager(CreateListingScreen screen) {
        this.screen = screen;
        this.screen.setManager(this);
    }

    public void startCreateListingProcess(String ownerID) {
        screen.displayTitle("Έλεγχος Αγγελιών");

        List<Listing> listings = db.getListingsForOwner(ownerID);
        List<Listing> active = Listing.filterActiveListings(listings);

        if (!checkListingLimit(active)) {
            screen.showArchiveOptions(listings); // Εναλλακτική ροή
            return;
        }

        screen.displayCreateListingScreen(ownerID); // Κανονική ροή
    }

    public void completeListingCreation(String ownerID) {
        Listing newListing = CreateListingForm.fillListingForm();

        if (!validateRequiredFields(newListing)) {
            Message.createErrorMessage("Ο μέγιστος αριθμός συγκατοίκων πρέπει να είναι θετικός.");
            screen.displayMessage("Ο μέγιστος αριθμός συγκατοίκων πρέπει να είναι θετικός.");
            return;
        }

        if (!validateRoommateCompatibility(newListing)) {
            Message.createErrorMessage("Ο αριθμός συγκατοίκων υπερβαίνει τα δωμάτια.");
            screen.displayMessage("Ο αριθμός συγκατοίκων υπερβαίνει τα δωμάτια.");
            return;
        }

        screen.displayTitle("Μεταφόρτωση Φωτογραφιών");
        List<String> photos = UploadPhotoForm.uploadPhotos();

        if (!validatePhotos(photos)) {
            Message.createErrorMessage("Επιτρεπτοί τύποι είναι μόνο .jpg και .png.");
            screen.displayMessage("Επιτρεπτοί τύποι είναι μόνο .jpg και .png.");
            return;
        }

        db.saveListing(newListing);
        Message.createSuccessMessage("Η αγγελία καταχωρήθηκε με επιτυχία.");
        screen.displayMessage("Η αγγελία καταχωρήθηκε με επιτυχία.");
        LocationManager.triggerLocationEntry(newListing.getId());
    }


    private boolean validateRequiredFields(Listing listing) {
        return listing != null && listing.getMaxRoommates() > 0;
    }

    private boolean validateRoommateCompatibility(Listing listing) {
        return listing.getMaxRoommates() <= listing.getRooms();
    }

    private boolean validatePhotos(List<String> photos) {
        for (String photo : photos) {
            if (!photo.endsWith(".jpg") && !photo.endsWith(".png")) {
                return false;
            }
        }
        return true;
    }

    private boolean checkListingLimit(List<Listing> activeListings) {
        return activeListings.size() < 3;
    }

    public void cancelListingCreation() {
        // λογική για επιστροφή στην MainScreen (αν υπάρχει)
        // προς το παρόν: dummy
    }

    public void displayToMainScreen(String message) {
        screen.displayMessage(message);
    }

}
