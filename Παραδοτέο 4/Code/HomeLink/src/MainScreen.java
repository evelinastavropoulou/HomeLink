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
        displayTitle("Σύστημα Σύνδεσης");
        while (!loggedIn) {
            System.out.print("Εισάγετε το Owner ID: ");
            String inputID = scanner.nextLine();

            if (ManageDB.isValidOwner(inputID)) {
                this.owner = new Owner(inputID);
                loggedIn = true;
                displayMessage("Σύνδεση επιτυχής με ID: " + inputID);
            } else {
                displayMessage("Μη έγκυρο Owner ID. Δοκιμάστε ξανά.");
            }
        }
    }

    public void showMainMenu() {
        while (loggedIn) {
            displayTitle("Αρχική Οθόνη");
            System.out.println("1. Δημιουργία Αγγελίας");
            System.out.println("2. Αποσύνδεση (Logout)");
            System.out.println("0. Έξοδος");
            System.out.print("Επιλογή: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    owner.onCreateListingClicked(this);
                    break;
                case 2:
                    logout();
                    break;
                case 0:
                    System.out.println("Έξοδος.");
                    System.exit(0);
                default:
                    System.out.println("Μη έγκυρη επιλογή.");
            }
        }
    }

    public void logout() {
        displayMessage("Αποσυνδεθήκατε.");
        this.loggedIn = false;
        this.owner = null;
    }

    public void onCreateListingClicked(String ownerID) {
        listingManager.startCreateListingProcess(ownerID);
    }

    public void displayMessage(String message) {
        System.out.println("[Μήνυμα]: " + message);
    }

    public void displayTitle(String title) {
        System.out.println("\n--- " + title + " ---");
    }
}
