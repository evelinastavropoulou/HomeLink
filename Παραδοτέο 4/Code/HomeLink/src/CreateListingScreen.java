import java.util.*;

public class CreateListingScreen {
    private CreateListingManager manager;

    public void setManager(CreateListingManager manager) {
        this.manager = manager;
    }

    public void displayCreateListingScreen(String ownerID) {
        displayTitle("Οθόνη Δημιουργίας Αγγελίας");
        manager.completeListingCreation(ownerID);
    }

    public void displayMessage(String message) {
        System.out.println("[Μήνυμα]: " + message);
    }

    public void displayTitle(String title) {
        System.out.println("\n--- " + title + " ---");
    }

    public void showArchiveOptions(List<Listing> listings) {
        displayTitle("Αρχειοθέτηση Αγγελίας");
        System.out.println("Έχετε υπερβεί το όριο ενεργών αγγελιών.");
        System.out.println("Επιλέξτε αγγελία προς αρχειοθέτηση ή 0 για ακύρωση:");

        for (int i = 0; i < listings.size(); i++) {
            System.out.println((i + 1) + ". " + listings.get(i));
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Επιλογή: ");
        int choice = scanner.nextInt();

        if (choice == 0) {
            cancelListingArchive();
            manager.cancelListingCreation();  // επιστροφή στη MainScreen
        } else if (choice >= 1 && choice <= listings.size()) {
            Listing listingToArchive = listings.get(choice - 1);
            listingToArchive.setArchived(true);  // αλλαγή κατάστασης

            // Διαγραφή δηλώσεων ενδιαφέροντος
            RentalInterest.deleteAssociatedInterests(listingToArchive.getId());

            // Εμφάνιση μηνύματος επιτυχίας
            Message.createMessage(listingToArchive.getId(), "Archived");
            manager.displayToMainScreen("Η αγγελία " + listingToArchive.getId() + " αρχειοθετήθηκε.");
        } else {
            System.out.println("Μη έγκυρη επιλογή.");
        }
    }

    public void cancelListingArchive() {
        System.out.println("[Ακύρωση] Δεν έγινε καμία αλλαγή.");
    }
}
