import java.util.List;

public class RentalTerms {
    private String listingId;
    private List<String> tenantIds;
    private double price;
    private int durationInMonths;
    private String status;  // ✅ νέο πεδίο

    public RentalTerms(String listingId, List<String> tenantIds, double price, int durationInMonths, String status) {
        this.listingId = listingId;
        this.tenantIds = tenantIds;
        this.price = price;
        this.durationInMonths = durationInMonths;
        this.status = status;
    }

    public String getListingId() {
        return listingId;
    }

    public List<String> getTenantIds() {
        return tenantIds;
    }

    public double getPrice() {
        return price;
    }

    public int getDurationInMonths() {
        return durationInMonths;
    }

    public String getStatus() {
        return status;
    }

    public static List<RentalTerms> getPendingRentalRequests(String userId) {
        return ManageDB.queryPendingRentalRequests(userId);
    }

    public static List<RentalTerms> getTemporaryRentals(String ownerId) {
        return ManageDB.queryTemporaryRentals(ownerId);
    }

    public static boolean markForFinalization(RentalTerms rental) {
        return ManageDB.updateRentalStatus(rental.getListingId(), rental.getTenantIds().get(0), "ready_for_finalization");
    }

    public void getAllRentalDetails() {
        System.out.println("\n📄 Λεπτομέρειες Ενοικίασης:");
        System.out.println("Αγγελία: " + listingId);
        System.out.println("Ενοικιαστής(ες): " + String.join(", ", tenantIds));
        System.out.println("Τιμή: " + price + " €");
        System.out.println("Διάρκεια: " + durationInMonths + " μήνες");
        System.out.println("Κατάσταση: " + status);  // ✅ νέα γραμμή
    }
}
