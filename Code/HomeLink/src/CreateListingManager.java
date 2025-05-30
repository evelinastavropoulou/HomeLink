import java.util.*;

public class CreateListingManager {
    private ManageDB db = new ManageDB();
    private CreateListingScreen screen;

    public CreateListingManager(CreateListingScreen screen) {
        this.screen = screen;
        this.screen.setManager(this);
    }

    public void initiateListingCreation(String ownerID) {
        System.out.print("🔎 Έλεγχος Αγγελιών Σε Εξέλιξη...");

        simulateLoading("Εντοπισμός ενεργών αγγελιών");

        List<Listing> listings = db.getListingsForOwner(ownerID);
        List<Listing> active = Listing.filterActiveListings(listings);

        if (!checkListingLimit(active)) {
            screen.showArchiveOptions(listings); // Εναλλακτική ροή
            return;
        }

        screen.displayCreateListingScreen(ownerID); // Κανονική ροή

        Listing newListing = CreateListingForm.fillListingForm();

        newListing.setOwnerID(ManageDB.getLoggedInOwner());  // <-- πολύ σημαντικό


        if (!CreateListingForm.validateRequiredFields(newListing)) {
            Message.createErrorMessage("Ο μέγιστος αριθμός συγκατοίκων πρέπει να είναι θετικός.");
            screen.displayMessage("Ο μέγιστος αριθμός συγκατοίκων πρέπει να είναι θετικός.");
            return;
        }

        if (!validateRoommateCompatibility(newListing)) {
            Message.createErrorMessage("Ο αριθμός συγκατοίκων υπερβαίνει τα δωμάτια.");
            screen.displayMessage("Ο αριθμός συγκατοίκων υπερβαίνει τα δωμάτια.");
            return;
        }

        System.out.print("\nΜεταφόρτωση Φωτογραφιών");
        List<String> photos = UploadPhotoForm.uploadPhotos();

        // Επιστροφή μη έγκυρων φωτογραφιών
        List<String> invalidPhotos = validatePhotos(photos);

        // Αν υπάρχουν μη έγκυρες, τις αφαιρούμε και ειδοποιούμε
        if (!invalidPhotos.isEmpty()) {
            UploadPhotoForm.removeInvalidPhotos(photos, invalidPhotos);

            Message.createErrorMessage("Κάποιες φωτογραφίες δεν έγιναν δεκτές (.jpg, .png μόνο): " + invalidPhotos);
            screen.displayMessage("Κάποιες φωτογραφίες αγνοήθηκαν:\n" + String.join(", ", invalidPhotos));
        }

        // Αν μετά την αφαίρεση δεν υπάρχει καμία φωτογραφία, ακυρώνουμε
        if (photos.isEmpty()) {
            Message.createErrorMessage("Δεν υπάρχουν έγκυρες φωτογραφίες (.jpg, .png).");
            screen.displayMessage("Δεν υπάρχουν έγκυρες φωτογραφίες (.jpg, .png).");
            return;
        }

        // 👉 Προσθήκη των φωτογραφιών στο listing ΠΡΙΝ την αποθήκευση
        newListing.attachPhotos(photos); // Το "photos" εδώ είναι η λίστα με τα paths από UploadPhotoForm

        db.saveListing(newListing);

        Message.createSuccessMessage("Η αγγελία καταχωρήθηκε με επιτυχία.");
        screen.displayMessage("Η αγγελία καταχωρήθηκε με επιτυχία.");
        LocationManager.triggerLocationEntry(newListing.getId());
    }

    private boolean checkListingLimit(List<Listing> activeListings) {
        return activeListings.size() <= 3;
    }

    public void cancelListingCreation() {
        // λογική για επιστροφή στην MainScreen (αν υπάρχει)
        // προς το παρόν: dummy
    }

    private boolean validateRoommateCompatibility(Listing listing) {
        return listing.getMaxRoommates() <= listing.getRooms();
    }

    private List<String> validatePhotos(List<String> photos) {
        List<String> invalidPhotos = new ArrayList<>();
        for (String photo : photos) {
            if (!photo.toLowerCase().endsWith(".jpg") && !photo.toLowerCase().endsWith(".png")) {
                invalidPhotos.add(photo);
            }
        }
        return invalidPhotos;
    }


    private void simulateLoading(String task) {
        System.out.print("\n⏳ " + task);
        try {
            for (int i = 0; i < 3; i++) {
                Thread.sleep(400);
                System.out.print(".");
            }
            System.out.println(" ✔️");
        } catch (InterruptedException e) {
            System.out.println(" ⚠️ [Διακοπή]");
        }
    }


}
