import java.util.Scanner;
import java.util.UUID;

public class CreateListingForm {
    public static Listing fillListingForm() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n--- Φόρμα Καταχώρησης Αγγελίας ---");

        System.out.print("Τύπος Κατοικίας: ");
        String type = sc.nextLine();

        System.out.print("Εμβαδόν: ");
        int size = sc.nextInt();

        System.out.print("Τιμή: ");
        double price = sc.nextDouble();

        System.out.print("Όροφος: ");
        int floor = sc.nextInt();

        System.out.print("Αριθμός Δωματίων: ");
        int rooms = sc.nextInt();

        System.out.print("Δυνατότητα Συγκατοίκησης (true/false): ");
        boolean canShare = sc.nextBoolean();

        System.out.print("Μέγιστος Αριθμός Συγκατοίκων: ");
        int maxRoommates = sc.nextInt();

        return new Listing(UUID.randomUUID().toString(), type, size, price, floor, rooms, canShare, maxRoommates);
    }
}
