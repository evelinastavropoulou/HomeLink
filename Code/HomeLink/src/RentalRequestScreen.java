import java.util.*;

public class RentalRequestScreen {


    public void displayDeclarations(List<String> interests, List<String> userIds, Map<String, Integer> trustScores, List<Listing> listings) {
        System.out.println("\n>>> Δηλώσεις Ενδιαφέροντος <<<");
        for (String interest : interests) {
            System.out.println("Δήλωση: " + interest);
        }

        System.out.println("\n>>> Χρήστες με Ενδιαφέρον <<<");
        for (String userId : userIds) {
            int score = trustScores.getOrDefault(userId, 0);
            System.out.println("Χρήστης: " + userId + " | Trust Score: " + score);
        }

        System.out.println("\n>>> Διαθέσιμες Αγγελίες <<<");
        for (int i = 0; i < listings.size(); i++) {
            Listing l = listings.get(i);
            System.out.println("[" + (i + 1) + "] ID: " + l.getId() + " | Τίτλος: " + l.getId() + " | Τοποθεσία: " + l.getAddress());
        }

        System.out.print("Επιλέξτε αριθμό αγγελίας: ");
        Scanner scanner = new Scanner(System.in);
        int selectedIndex = Integer.parseInt(scanner.nextLine()) - 1;

        if (selectedIndex < 0 || selectedIndex >= listings.size()) {
            System.out.println("[Σφάλμα] Μη έγκυρη επιλογή.");
            return;
        }

        Listing selectedListing = listings.get(selectedIndex);

        List<String> selectedUsers = selectUsers(new HashSet<>(userIds), selectedListing);

        if (selectedUsers.isEmpty()) {
            System.out.println("[!] Η διαδικασία δεν συνεχίστηκε λόγω άκυρης επιλογής.");
            return;
        }

        // Προαιρετικά εκτύπωση — καθαρά για debugging/εμφάνιση
        System.out.println(">>> Επιλεγμένοι Χρήστες για την Αγγελία: " + selectedListing.getId());
        for (String u : selectedUsers) {
            System.out.println("- " + u);
        }

        // 🚫 Μην καλέσεις ξανά startRentalProcedure εδώ — έχει ήδη γίνει μέσα από selectUsers
    }



    public List<String> selectUsers(Set<String> availableUserIds, Listing listing) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nΠληκτρολογήστε τα IDs των χρηστών που επιλέγετε, χωρισμένα με κόμμα (π.χ. user1,user2): ");
        String input = scanner.nextLine();
        String[] selected = input.split(",");

        List<String> selectedUsers = new ArrayList<>();
        for (String userId : selected) {
            userId = userId.trim();
            if (availableUserIds.contains(userId)) {
                selectedUsers.add(userId);
            } else {
                System.out.println("\n[!] Αγνόησα άγνωστο χρήστη: " + userId);
            }
        }

        RentalRequestScreen screen = new RentalRequestScreen();
        boolean isValid = RentalRequestManager.validateTenantSelection(listing, selectedUsers, screen);

        if (isValid) {
            RentalTermsForm form = new RentalTermsForm();
            RentalTerms terms = form.displayRentalTermsForm(listing, selectedUsers, screen);

            if (terms != null) {
                RentalRequestManager.startRentalProcedure(terms, screen);  // ✅ Εκτέλεση μόνο αν όλα OK
            }
        }

        return selectedUsers;
    }




    public void displayValidationResult(boolean isValid, String message) {
        if (isValid) {
            System.out.println("\n✅ Επιτυχία: " + message);
        } else {
            System.out.println("\n❌ Αποτυχία: " + message);
        }
    }

    public void displayMessage(String error) {
    }
}
