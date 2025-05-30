import java.util.*;

public class RentalApplicationManager {

    public static void loadPendingRequests(MainScreen screen, String userId) {
        List<RentalTerms> rentalRequests = RentalTerms.getPendingRentalRequests(userId);

        if (rentalRequests.isEmpty()) {
            String error = Message.createErrorMessage("Δεν υπάρχουν προς αποδοχή αιτήσεις ενοικίασης.");
            screen.displayMessage(error);
        } else {
            RentalApplicationScreen rentalScreen = new RentalApplicationScreen();
            rentalScreen.displayRentalTerms(rentalRequests);
        }
    }

    public static void proceedToDocumentUpload(RentalTerms rental, RentalApplicationScreen screen) {
        UploadDocumentForm form = new UploadDocumentForm();
        form.displayDocumentUploadForm(rental, screen);
    }

    public static void handleTermsRejection(RentalTerms rental, RentalApplicationScreen screen) {
        String tenantId = rental.getTenantIds().get(0);
        boolean updated = ManageDB.updateRentalStatus(rental.getListingId(), tenantId, "rejected");

        if (updated) {
            String ownerId = ManageDB.getOwnerOfListing(rental.getListingId());

            if (ownerId != null) {
                Notification notification = Notification.createNotification(
                        rental.getListingId(),
                        ownerId,
                        "Η αίτηση για την αγγελία " + rental.getListingId() + " απορρίφθηκε από τον χρήστη " + tenantId
                );

                ManageDB.saveNotification(notification);
            }

            screen.displayMessage("❌ Η αίτηση απορρίφθηκε και ενημερώθηκε ο ιδιοκτήτης.");
        } else {
            screen.displayMessage("⚠️ Σφάλμα κατά την ενημέρωση της αίτησης.");
        }
    }


    public static void onUpload(RentalTerms rental, String idCard, String taxInfo, String incomeProof,
                                RentalApplicationScreen screen, UploadDocumentForm form) {

        boolean valid = form.checkFileFormatAndSize(idCard, taxInfo, incomeProof, screen);

        if (!valid) return;

        screen.displayMessage("✅ Τα έγγραφα επικυρώθηκαν. Γίνεται αποστολή για ψηφιακή ταυτοποίηση...");

        boolean verificationPassed = VerificationProvider.sendForDigitalVerification(idCard, taxInfo, incomeProof);

        if (!verificationPassed) {
            boolean updated = ManageDB.updateRentalStatus(rental.getListingId(), rental.getTenantIds().get(0), "rejected");

            if (updated) {
                String ownerId = ManageDB.getOwnerOfListing(rental.getListingId());

                Notification notification = Notification.createNotification(
                        rental.getListingId(),
                        "❌ Rental Terms Rejected",
                        ownerId
                );

                ManageDB.saveNotification(notification);
                screen.displayMessage("❌ Η ψηφιακή ταυτοποίηση απέτυχε. Η αίτηση απορρίφθηκε.");
            } else {
                screen.displayMessage("⚠️ Σφάλμα κατά την ενημέρωση της κατάστασης.");
            }

            return;
        }

        // ✅ Αν όλα πήγαν καλά:
        boolean updated = ManageDB.updateRentalStatus(rental.getListingId(), rental.getTenantIds().get(0), "accepted");

        if (updated) {
            String ownerId = ManageDB.getOwnerOfListing(rental.getListingId());

            Notification notification = Notification.createNotification(
                    rental.getListingId(),
                    "📄 Documents Verified. Η αίτηση ενοικίασης προχωρά.",
                    ownerId
            );

            ManageDB.saveNotification(notification);

            String success = Message.createSuccessMessage("✅ Η αίτηση σας υποβλήθηκε επιτυχώς!");
            screen.displayMessage(success);
        } else {
            screen.displayMessage("⚠️ Σφάλμα κατά την αποθήκευση της επιτυχούς αίτησης.");
        }
    }



}
