import java.util.List;
import java.util.Scanner;

public class FinalizeRentalScreen {

    public void showFinalizeOptions(List<RentalTerms> temporaryRentals) {
        System.out.println("\n>>> Προσωρινά Αποδεκτές Αιτήσεις <<<");

        for (int i = 0; i < temporaryRentals.size(); i++) {
            RentalTerms rt = temporaryRentals.get(i);
            System.out.println("[" + (i + 1) + "] Αγγελία: " + rt.getListingId() +
                    " | Ενοικιαστής: " + rt.getTenantIds().get(0) +
                    " | Τιμή: " + rt.getPrice() + " €" +
                    " | Διάρκεια: " + rt.getDurationInMonths() + " μήνες");
        }

        System.out.println("\nΕπέλεξε μια αίτηση για οριστικοποίηση ή πληκτρολόγησε 0 για επιστροφή:");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= temporaryRentals.size()) {
            selectRentalForFinalization(temporaryRentals.get(choice - 1));
        } else {
            displayMessage("🔙 Επιστροφή στο μενού.");
        }
    }

    public void selectRentalForFinalization(RentalTerms rental) {
        boolean updated = RentalTerms.markForFinalization(rental);

        if (!updated) {
            String msg = Message.createErrorMessage("❌ Σφάλμα κατά την ενημέρωση της κατάστασης.");
            displayMessage(msg);
            return;
        }

        String msg = Message.createSuccessMessage("🏁 Η αίτηση σημάνθηκε για οριστικοποίηση.");
        displayMessage(msg);

        rental.getAllRentalDetails();

        RentalContract contract = RentalContract.generateRentalContract(rental);

        if (contract == null) {
            String errorMsg = Message.createErrorMessage("❌ Missing Or Invalid Data");
            displayMessage(errorMsg);
            FinalizeRentalManager.prefillWithValidData(rental);
            return;
        }

        contract.displayContract();
        contract.generateContractPDF();

        boolean saved = ManageDB.saveRentalContract(rental);

        if (!saved) {
            String errorMsg = Message.createErrorMessage("❌ Αποτυχία αποθήκευσης συμβολαίου στη βάση.");
            displayMessage(errorMsg);
            return;
        }

        displayMessage(Message.createSuccessMessage("📎 Contract Ready"));

        // 🔄 Επιλογή χρήστη για συνέχιση ή ακύρωση
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nΘέλετε να υποβάλετε τη σύμβαση ή να ακυρώσετε; (submit/cancel): ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("submit")) {
            submitContract(rental); // ✅ Υποβολή και ενεργοποίηση
        } else if (choice.equals("cancel")) {
            cancelContractFinalization(rental);
        } else {
            displayMessage(Message.createErrorMessage("❌ Μη έγκυρη επιλογή."));
        }
    }

    private void cancelContractFinalization(RentalTerms rental) {
        Message.createPromptMessage("❓ Confirm Cancellation?");
        FinalizeRentalManager.terminateContractCreation();
        displayMessage(Message.createSuccessMessage("⛔ Η διαδικασία δημιουργίας συμβολαίου ακυρώθηκε."));
        //returnToMainScreen();
    }


    private void submitContract(RentalTerms rental) {
        // 1. Ενημέρωση καταστάσεων και βάσεων
        Listing.markAsUnavailable(rental.getListingId());
        RentalInterest.deleteListingInterests(rental.getListingId());
        RentalTerms.activateRental(rental); // ενημερώνει το status σε "active"
        ManageDB.updateRentalStatus(rental.getListingId(), rental.getTenantIds().get(0), "active");

        // 2. Εμφάνιση επιτυχίας
        displayMessage(Message.createSuccessMessage("✅ Η σύμβαση υποβλήθηκε και ενεργοποιήθηκε."));

        // 3. Ειδοποίηση συμμετεχόντων
        Notification.notifyAllParticipants(rental);

        // 4. Επιστροφή στην αρχική οθόνη
        //returnToMainScreen();
    }



    public void displayContractPreview(RentalTerms rental) {
        System.out.println("\n>>> Προεπισκόπηση Συμβολαίου <<<");
        System.out.println("🆔 Αγγελία: " + rental.getListingId());
        System.out.println("👤 Ενοικιαστής: " + rental.getTenantIds().get(0));
        System.out.println("💶 Τιμή: " + rental.getPrice() + " € / μήνα");
        System.out.println("📆 Διάρκεια: " + rental.getDurationInMonths() + " μήνες");
        System.out.println("📌 Κατάσταση: " + rental.getStatus());
    }


    public void displayMessage(String message) {
        System.out.println("\n[📢] " + message);
    }

}
