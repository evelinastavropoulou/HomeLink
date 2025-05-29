import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

public class CreateListingForm {
    public static Listing fillListingForm() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n📋 ── Φόρμα Καταχώρησης Νέας Αγγελίας ──");

        System.out.print("🏠 Τύπος Κατοικίας (π.χ., Διαμέρισμα): ");
        String type = sc.nextLine();

        int size = readInt(sc, "📏 Εμβαδόν (τ.μ.): ");
        double price = readDouble(sc, "💶 Τιμή (€): ");
        int floor = readInt(sc, "🏢 Όροφος: ");
        int rooms = readInt(sc, "🚪 Αριθμός Δωματίων: ");
        boolean canShare = readBoolean(sc, "👥 Δυνατότητα Συγκατοίκησης (true/false): ");
        int maxRoommates = readInt(sc, "👫 Μέγιστος Αριθμός Συγκατοίκων: ");

        return new Listing(
                UUID.randomUUID().toString(),
                type,
                size,
                price,
                floor,
                rooms,
                canShare,
                maxRoommates
        );
    }

    public static boolean validateRequiredFields(Listing listing) {
        return listing != null && listing.getMaxRoommates() > 0;
    }

    private static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("❌ Μη έγκυρη τιμή. Δοκιμάστε ξανά (ακέραιος αριθμός).");
                sc.nextLine(); // flush
            }
        }
    }

    private static double readDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return sc.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("❌ Μη έγκυρη τιμή. Δοκιμάστε ξανά (δεκαδικός αριθμός).");
                sc.nextLine(); // flush
            }
        }
    }

    private static boolean readBoolean(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return sc.nextBoolean();
            } catch (InputMismatchException e) {
                System.out.println("❌ Εισάγετε true ή false.");
                sc.nextLine(); // flush
            }
        }
    }
}
