import java.util.Scanner;
import java.util.List;
import java.util.*;

public class FinalizeRentalManager {

    public static void fetchTemporaryRentals(String ownerId, MainScreen screen) {
        List<RentalTerms> temporaryRentalRequests = RentalTerms.getTemporaryRentals(ownerId);

        if (temporaryRentalRequests.isEmpty()) {
            String msg = Message.createErrorMessage("Δεν υπάρχουν προσωρινά αποδεκτές αιτήσεις για οριστικοποίηση.");
            screen.displayMessage(msg);
        } else {
            FinalizeRentalScreen finalizeScreen = new FinalizeRentalScreen();
            finalizeScreen.showFinalizeOptions(temporaryRentalRequests);
        }
    }

    public static void prefillWithValidData(RentalTerms rental) {
        FinalizeRentalScreen screen = new FinalizeRentalScreen();
        screen.displayContractPreview(rental);  // Απλή προβολή στοιχείων rental χωρίς καμία φόρμα
    }

    public static void terminateContractCreation() {
        System.out.println("[🚫] Η διαδικασία δημιουργίας της σύμβασης ακυρώθηκε από τον χρήστη.");
        // Εδώ μπορείς να προσθέσεις λογική καθαρισμού ή rollback αν χρειαστεί
    }

}
