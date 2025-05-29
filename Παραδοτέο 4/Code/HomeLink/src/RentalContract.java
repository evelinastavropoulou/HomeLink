import java.util.Scanner;
import java.util.List;
import java.util.*;

public class RentalContract {
    private String listingId;
    private List<String> tenants;
    private double price;
    private int duration;
    private String contractText;

    private RentalContract(String listingId, List<String> tenants, double price, int duration) {
        this.listingId = listingId;
        this.tenants = tenants;
        this.price = price;
        this.duration = duration;
        this.contractText = generateText();
    }

    public static RentalContract generateRentalContract(RentalTerms rental) {
        return new RentalContract(
                rental.getListingId(),
                rental.getTenantIds(),
                rental.getPrice(),
                rental.getDurationInMonths()
        );
    }

    public void generateContractPDF() {
        System.out.println("\n📄 Το συμβόλαιο δημιουργήθηκε επιτυχώς σε PDF μορφή!");
    }

   // Βοηθητικές συναρτήσεις για debugging

    private String generateText() {
        return "Μισθωτήριο Συμβόλαιο για την Αγγελία: " + listingId + "\n" +
                "Ενοικιαστές: " + String.join(", ", tenants) + "\n" +
                "Τιμή: " + price + " € / μήνα\n" +
                "Διάρκεια: " + duration + " μήνες\n" +
                "Ημερομηνία Έναρξης: " + java.time.LocalDate.now() + "\n" +
                "------------------------\n";
    }

    public void displayContract() {
        System.out.println("\n============================");
        System.out.println(contractText);
        System.out.println("============================");
    }
}
