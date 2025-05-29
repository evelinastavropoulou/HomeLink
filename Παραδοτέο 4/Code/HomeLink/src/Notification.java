
import java.util.*;

public class Notification {
    private String message;
    private String listingId;
    private List<String> recipients;

    public Notification(String message, String listingId, List<String> recipients) {
        this.message = message;
        this.listingId = listingId;
        this.recipients = recipients;
    }

    public static Notification createRentalRequestNotification(RentalTerms rental, List<String> tenantIds) {
        String msg = "Η ενοικίαση για το Listing #" + rental.getListingId() + " επιβεβαιώθηκε για " + tenantIds.size() + " ενοικιαστή/ές.";
        return new Notification(msg, rental.getListingId(), tenantIds);
    }

    public String getMessage() {
        return message;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public String getListingId() {
        return listingId;
    }
}
