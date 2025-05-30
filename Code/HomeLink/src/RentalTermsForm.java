import java.util.List;
import java.util.Scanner;

public class RentalTermsForm {

    public RentalTerms displayRentalTermsForm(Listing listing, List<String> selectedUsers, RentalRequestScreen screen) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n>>> Φόρμα Συμπλήρωσης Όρων Ενοικίασης <<<");

        System.out.print("Εισάγετε τιμή ενοικίου (€): ");
        String priceInput = scanner.nextLine();

        System.out.print("Εισάγετε διάρκεια μίσθωσης (σε μήνες): ");
        String durationInput = scanner.nextLine();

        if (!validateFormFields(priceInput, durationInput)) {
            highlightInvalidFormFields();
            String error = Message.createErrorMessage("Μη έγκυρη τιμή ή διάρκεια. Παρακαλώ δοκιμάστε ξανά.");
            screen.displayMessage(error);
            return null;
        }

        double price = Double.parseDouble(priceInput);
        int duration = Integer.parseInt(durationInput);

        System.out.println("\n✅ Όροι καταχωρήθηκαν επιτυχώς!");
        System.out.println("Αγγελία: " + listing.getId());
        System.out.println("Ενοικιαστές: " + String.join(", ", selectedUsers));
        System.out.println("Τιμή: " + price + " €");
        System.out.println("Διάρκεια: " + duration + " μήνες");

        String status = "accepted";  // ✅ Προεπιλεγμένο status για νέο RentalTerms

        return new RentalTerms(listing.getId(), selectedUsers, price, duration, status);
    }



    private boolean validateFormFields(String priceStr, String durationStr) {
        try {
            double price = Double.parseDouble(priceStr);
            int duration = Integer.parseInt(durationStr);
            return price > 0 && duration > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void highlightInvalidFormFields() {
        System.out.println("\n[!] Σφάλμα: Επισήμανση λανθασμένων πεδίων!!!");
    }
}
