import java.util.List;

public class RentalTerms {
    private String listingId;
    private List<String> tenantIds;
    private double price;
    private int durationInMonths;

    public RentalTerms(String listingId, List<String> tenantIds, double price, int durationInMonths) {
        this.listingId = listingId;
        this.tenantIds = tenantIds;
        this.price = price;
        this.durationInMonths = durationInMonths;
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
}
