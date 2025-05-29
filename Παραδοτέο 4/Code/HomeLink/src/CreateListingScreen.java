import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.*;

public class CreateListingScreen {
    private CreateListingManager manager;

    public void setManager(CreateListingManager manager) {
        this.manager = manager;
    }

    public void displayCreateListingScreen(String ownerID) {
        System.out.print("\n📝 Οθόνη Δημιουργίας Νέας Αγγελίας");
    }

    public void showArchiveOptions(List<Listing> listings) {
        System.out.print("\n📁 Αρχειοθέτηση Αγγελίας");

        System.out.println("\n⚠️ Έχετε υπερβεί το όριο ενεργών αγγελιών.");
        System.out.println("📌 Επιλέξτε αγγελία προς αρχειοθέτηση ή πληκτρολογήστε 0 για ακύρωση:\n");

        for (int i = 0; i < listings.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + listings.get(i));
        }

        System.out.print("\n🔢 Επιλογή: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        if (choice == 0) {
            cancelListingArchive();
            manager.cancelListingCreation();  // επιστροφή στην αρχική οθόνη
        } else if (choice >= 1 && choice <= listings.size()) {
            Listing listingToArchive = listings.get(choice - 1);
            archiveListing(listingToArchive.getId());
        } else {
            System.out.println("❌ Μη έγκυρη επιλογή.");
        }
    }


    public void displayMessage(String message) {
        System.out.println("\n📢 [Μήνυμα]: " + message + "\n");
    }


    private void archiveListing(String listingId) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:homelink.db")) {
            // 1. Ενημέρωση κατάστασης στην Listing
            Listing.updateListingStatus(listingId, false, true);  // active = false, archived = true

            // 2. Λήψη ενδιαφερόντων
            List<String> interestIds = RentalInterest.getRentalInterests(listingId);

            // 3. Διαγραφή ενδιαφερόντων
            ManageDB.deleteAssociatedInterests(listingId);

            // 4. Μήνυμα
            Message.createMessage(listingId, "Archived");
            displayMessage("Η αγγελία " + listingId + " αρχειοθετήθηκε.");
        } catch (SQLException e) {
            e.printStackTrace();
            displayMessage("❌ Σφάλμα κατά την αρχειοθέτηση.");
        }
    }


    public void cancelListingArchive() {
        System.out.println("[Ακύρωση] Δεν έγινε καμία αλλαγή.");
    }
}
