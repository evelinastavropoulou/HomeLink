import java.util.Scanner;

public class MainScreen {
    private Scanner scanner = new Scanner(System.in);
    private CreateListingManager listingManager;
    private Owner owner;

    public MainScreen() {
        this.owner = new Owner("owner123");
        this.listingManager = new CreateListingManager(this);
    }

    public static void main(String[] args) {
        MainScreen screen = new MainScreen();
        screen.showMainMenu();
    }

    public void showMainMenu() {
        while (true) {
            System.out.println("----- HomeLink Main Menu -----");
            System.out.println("1. Δημιουργία Αγγελίας");
            System.out.println("0. Έξοδος");
            System.out.print("Επιλογή: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (choice == 1) {
                owner.onCreateListingClicked(this);
            } else if (choice == 0) {
                System.out.println("Έξοδος.");
                break;
            } else {
                System.out.println("Μη έγκυρη επιλογή.");
            }
        }
    }

    public void onCreateListingClicked(String ownerID) {
        listingManager.startCreateListingProcess(ownerID);
    }

    public void displayMessage(String message) {
        System.out.println("[Μήνυμα]: " + message);
    }
}
