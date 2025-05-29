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
}
