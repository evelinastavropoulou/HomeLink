import java.util.*;

public class UserAvailabilityScreen {

    public static void showAvailabilitySelectionScreen(String userID, String listingID) {
        Scanner sc = new Scanner(System.in);
        List<String> timeSlots = new ArrayList<>();

        System.out.println("\n🕒 Δηλώστε τα διαθέσιμα χρονικά σας διαστήματα (π.χ., Δευτέρα 10:00-12:00).");
        System.out.println("Πληκτρολογήστε 'τέλος' για να ολοκληρώσετε.");

        while (true) {
            System.out.print("➤ Χρονικό διάστημα: ");
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("τέλος")) {
                break;
            }
            if (!input.isEmpty()) {
                timeSlots.add(input);
            }
        }

        submitAvailability(userID, listingID, timeSlots);
    }

    public static void submitAvailability(String userID, String listingID, List<String> userTimeSlots) {
        UserAvailability.createTimeslots(userID, listingID, userTimeSlots);
        Message.createSuccessMessage("✅ Τα χρονικά διαστήματα διαθεσιμότητας καταχωρήθηκαν επιτυχώς.");

        // Έλεγχος διαθεσιμότητας ιδιοκτήτη
        RentalInterestManager manager = new RentalInterestManager();
        List<String> ownerTimeSlots = manager.getOwnerAvailability(listingID);

        if (ownerTimeSlots == null || ownerTimeSlots.isEmpty()) {
            Message.createErrorMessage("⛔ Δεν υπάρχει δηλωμένη διαθεσιμότητα από τον ιδιοκτήτη.");
        } else {
            List<String> common = UserAvailability.compareWith(userTimeSlots, ownerTimeSlots);
            if (common.isEmpty()) {
                Message.createErrorMessage("❌ Δεν βρέθηκε κοινή διαθεσιμότητα με τον ιδιοκτήτη.");
            } else {
                System.out.println("✅ Κοινή διαθεσιμότητα:");
                for (String slot : common) {
                    System.out.println("📅 " + slot);
                }
            }
        }
    }

}
