import java.util.List;
import java.util.Scanner;

public class RentalApplicationScreen {

    private Scanner scanner = new Scanner(System.in);

    public void displayRentalTerms(List<RentalTerms> rentalRequests) {
        System.out.println("\n>>> Αιτήσεις προς Αποδοχή <<<");

        for (int i = 0; i < rentalRequests.size(); i++) {
            RentalTerms rt = rentalRequests.get(i);
            System.out.println("[" + (i + 1) + "] Αγγελία: " + rt.getListingId() +
                    " | Τιμή: " + rt.getPrice() +
                    " € | Διάρκεια: " + rt.getDurationInMonths() + " μήνες");
        }

        System.out.print("\nΕπιλέξτε αριθμό αίτησης για αποδοχή ή 0 για επιστροφή: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 0 || choice < 1 || choice > rentalRequests.size()) {
            System.out.println("Επιστροφή στο μενού...");
            return;
        }

        RentalTerms selected = rentalRequests.get(choice - 1);

        System.out.println("1. Αποδοχή Όρων Ενοικίασης");
        System.out.println("2. Απόρριψη Αίτησης");
        System.out.print("Επιλογή: ");
        int action = scanner.nextInt();
        scanner.nextLine();

        if (action == 1) {
            acceptRentalTerms(selected);
        } else if (action == 2) {
            rejectRentalTerms(selected);
        } else {
            System.out.println("[Σφάλμα] Μη έγκυρη επιλογή.");
        }
    }

    public void acceptRentalTerms(RentalTerms rental) {
        RentalApplicationManager.proceedToDocumentUpload(rental, this);
    }

    public void rejectRentalTerms(RentalTerms rental) {
        RentalApplicationManager.handleTermsRejection(rental, this);
    }

    public void displayMessage(String msg) {
        System.out.println("\n>>> Μήνυμα: " + msg);
    }
}
