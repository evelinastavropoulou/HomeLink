import java.util.List;

public class RentalApplicationScreen {

    public void displayRentalTerms(List<RentalTerms> rentalRequests) {
        System.out.println("\n>>> Αιτήσεις προς Αποδοχή <<<");

        for (int i = 0; i < rentalRequests.size(); i++) {
            RentalTerms rt = rentalRequests.get(i);
            System.out.println("[" + (i + 1) + "] Αγγελία: " + rt.getListingId() +
                    " | Τιμή: " + rt.getPrice() +
                    " € | Διάρκεια: " + rt.getDurationInMonths() + " μήνες");
        }

        System.out.println("\nΕπιστροφή στο μενού...");
    }
}
