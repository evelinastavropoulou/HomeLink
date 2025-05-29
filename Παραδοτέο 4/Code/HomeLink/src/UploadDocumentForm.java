import java.util.Scanner;

public class UploadDocumentForm {

    private Scanner scanner = new Scanner(System.in);

    public void displayDocumentUploadForm(RentalTerms rental, RentalApplicationScreen screen) {
        System.out.println("\n>>> Μεταφόρτωση Εγγράφων για Αγγελία: " + rental.getListingId());

        System.out.print("Πληκτρολογήστε το path του Δελτίου Ταυτότητας: ");
        String idCard = scanner.nextLine();

        System.out.print("Πληκτρολογήστε το path του Φορολογικού Αριθμού: ");
        String taxInfo = scanner.nextLine();

        System.out.print("Πληκτρολογήστε το path του Αποδεικτικού Εισοδήματος: ");
        String incomeProof = scanner.nextLine();

        if (idCard.isEmpty() || taxInfo.isEmpty() || incomeProof.isEmpty()) {
            screen.displayMessage("❌ Αποτυχία: Πρέπει να εισάγετε και τα 3 αρχεία.");
            return;
        }

        RentalApplicationManager.onUpload(rental, idCard, taxInfo, incomeProof, screen, this);
    }

    public boolean checkFileFormatAndSize(String idCard, String taxInfo, String incomeProof,
                                          RentalApplicationScreen screen) {

        StringBuilder errors = new StringBuilder();

        if (!idCard.endsWith(".pdf")) {
            errors.append("- Το Δελτίο Ταυτότητας πρέπει να είναι σε μορφή PDF\n");
        }

        if (!taxInfo.endsWith(".pdf")) {
            errors.append("- Ο Φορολογικός Αριθμός πρέπει να είναι σε μορφή PDF\n");
        }

        if (!incomeProof.endsWith(".pdf")) {
            errors.append("- Το Αποδεικτικό Εισοδήματος πρέπει να είναι σε μορφή PDF\n");
        }

        if (errors.length() > 0) {
            screen.displayMessage(Message.createErrorMessage("❌ Προέκυψαν σφάλματα κατά τον έλεγχο αρχείων."));
            highlightErrors(errors.toString());
            return false;
        }

        return true;
    }

    public void highlightErrors(String errorDetails) {
        System.out.println("🔍 Εντοπίστηκαν προβλήματα με τα αρχεία:");
        System.out.println(errorDetails);
    }
}
