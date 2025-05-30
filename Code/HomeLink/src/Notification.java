
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

    public static Notification createNotification(String listingId, String ownerId, String messageText) {
        return new Notification(listingId, messageText, Collections.singletonList(ownerId));
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

    public static void notifyAllParticipants(RentalTerms rental) {
        String listingId = rental.getListingId();

        // Δημιουργούμε νέα λίστα με όλους τους συμμετέχοντες
        List<String> participants = new ArrayList<>(rental.getTenantIds()); // ✅ μετατρέπεται σε mutable

        for (String userId : participants) {
            String msg = "📢 Η σύμβαση για την αγγελία " + listingId + " ενεργοποιήθηκε.";
            ManageDB.saveAsUnread(listingId, msg, userId); // ✅ αποθήκευση στη βάση
        }
    }

}

