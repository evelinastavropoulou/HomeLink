import java.util.*;

public class RentalRequestScreen {

    private Scanner scanner = new Scanner(System.in);

    public List<String> displayDeclarations(Map<String, Integer> trustScores, Listing listing) {
        System.out.println("Οι παρακάτω χρήστες εκδήλωσαν ενδιαφέρον:");
        for (Map.Entry<String, Integer> entry : trustScores.entrySet()) {
            System.out.println("- Χρήστης: " + entry.getKey() + ", Trust Score: " + entry.getValue());
        }

        // Επιστροφή επιλεγμένων χρηστών με validation
        return selectUsers(trustScores.keySet(), listing);
    }


    public List<String> selectUsers(Set<String> availableUserIds, Listing listing) {
        System.out.println("\nΠληκτρολογήστε τα IDs των χρηστών που επιλέγετε, χωρισμένα με κόμμα (π.χ. user1,user2):");
        String input = scanner.nextLine();
        String[] selected = input.split(",");

        List<String> selectedUsers = new ArrayList<>();
        for (String userId : selected) {
            userId = userId.trim();
            if (availableUserIds.contains(userId)) {
                selectedUsers.add(userId);
            } else {
                System.out.println("[!] Αγνόησα άγνωστο χρήστη: " + userId);
            }
        }

        // ✅ Κλήση validation
        RentalRequestManager.validateTenantSelection(listing, selectedUsers, this);

        return selectedUsers;
    }


    public void displayValidationResult(boolean isValid, String message) {
        if (isValid) {
            System.out.println("✅ Επιτυχία: " + message);
        } else {
            System.out.println("❌ Αποτυχία: " + message);
        }
    }
}
