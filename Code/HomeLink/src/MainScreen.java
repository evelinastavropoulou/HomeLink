import java.util.InputMismatchException;
import java.util.Scanner;

public class MainScreen {
    private Scanner scanner = new Scanner(System.in);
    private CreateListingManager listingManager;
    private Owner owner;
    private boolean loggedIn = false;

    public MainScreen() {
        CreateListingScreen listingScreen = new CreateListingScreen();
        this.listingManager = new CreateListingManager(listingScreen);
    }

    public static void main(String[] args) {
        MainScreen screen = new MainScreen();
        screen.run();
    }

    public void run() {
        while (true) {
            if (!loggedIn) {
                loginFlow();
            }
            showMainMenu();
        }
    }

    public void loginFlow() {
        while (!loggedIn) {
            System.out.print("\n🔑 Εισάγετε το Owner ID: ");
            String inputID = scanner.nextLine().trim();

            if (ManageDB.isValidOwner(inputID)) {
                ManageDB.setLoggedInOwner(inputID);   // Αποθηκεύει τον συνδεδεμένο χρήστη
                this.owner = new Owner(inputID);      // Δημιουργεί αντικείμενο Owner
                loggedIn = true;
                displayMessage("✅ Σύνδεση επιτυχής! Καλώς ήρθες, " + inputID + "!");
            } else {
                displayMessage("❌ Μη έγκυρο Owner ID. Δοκιμάστε ξανά.");
            }
        }
    }


    public void showMainMenu() {
        while (loggedIn) {
            displayTitle("🏠 Κεντρικό Μενού HomeLink");

            System.out.println("╔═════════════════════════════════════════╗");
            System.out.println("║                ΕΠΙΛΟΓΕΣ                 ║");
            System.out.println("╠═════════════════════════════════════════╣");
            System.out.println("║ 1. ➕ Δημιουργία Αγγελίας               ║");
            System.out.println("║ 2. 🔍 Αναζήτηση Κατοικίας               ║");
            System.out.println("║ 3. 📬 Διαχείριση Δηλώσεων Ενδιαφέροντος ║");
            System.out.println("║ 4. ✅ Αποδοχή Όρων Ενοικίασης           ║");
            System.out.println("║ 5. 📄 Οριστικοποίηση Ενοικίασης         ║");
            System.out.println("║ 6. 🔓 Αποσύνδεση (Logout)               ║");
            System.out.println("╠═════════════════════════════════════════╣");
            System.out.println("║ 0. ❌ Έξοδος                            ║");
            System.out.println("╚═════════════════════════════════════════╝");
            System.out.print("📌 Επιλογή σας: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
            } catch (InputMismatchException e) {
                System.out.println("❗ Παρακαλώ δώστε αριθμό επιλογής.");
                scanner.nextLine(); // flush
                continue;
            }

            switch (choice) {
                case 1 -> owner.onCreateListingClicked(this);
                case 2 -> owner.onSearchHousingClicked(this, owner.getId());
                case 3 -> owner.onInterestDeclarationsClicked(this, owner.getId());
                case 4 -> onAcceptRentalClicked();
                case 5 -> onFinalizeRentalClicked(owner.getId());
                case 6 -> logout();
                case 0 -> {
                    System.out.println("👋 Έξοδος από το σύστημα.");
                    System.exit(0);
                }
                default -> System.out.println("❌ Μη έγκυρη επιλογή. Δοκιμάστε ξανά.");
            }
        }
    }


    public void logout() {
        displayMessage("Αποσυνδεθήκατε.");
        this.loggedIn = false;
        this.owner = null;
    }

    public void onCreateListingClicked(String ownerID) {
        listingManager.initiateListingCreation(ownerID);
    }

    public void onAcceptRentalClicked() {
        RentalApplicationManager.loadPendingRequests(this, owner.getId());
    }

    public void onFinalizeRentalClicked(String ownerID) {
        FinalizeRentalManager.fetchTemporaryRentals( owner.getId(), this);
    }

    public void displayMessage(String message) {
        System.out.println("[Μήνυμα]: " + message);
    }

    public void displayTitle(String title) {
        System.out.println("\n--- " + title + " ---");
    }

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("⚠️ Αδυναμία εκκαθάρισης οθόνης.");
        }
    }

}
