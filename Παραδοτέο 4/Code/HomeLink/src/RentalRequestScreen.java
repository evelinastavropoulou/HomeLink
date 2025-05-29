import java.util.*;

public class RentalRequestScreen {

    private Scanner scanner = new Scanner(System.in);

    public void displayDeclarations(List<String> interests, List<String> userIds, Map<String, Integer> trustScores) {
        System.out.println(">>> Δηλώσεις Ενδιαφέροντος <<<");

        for (String interest : interests) {
            System.out.println("Δήλωση: " + interest);
        }

        System.out.println("\n>>> Χρήστες με Ενδιαφέρον <<<");
        for (String userId : userIds) {
            int score = trustScores.getOrDefault(userId, 0);
            System.out.println("Χρήστης: " + userId + " | Trust Score: " + score);
        }
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
