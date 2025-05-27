import java.util.ArrayList;
import java.util.List;

public class Listing {
    private String id;
    private String ownerID;
    private String type;
    private int size;
    private double price;
    private int floor;
    private int rooms;
    private boolean canShare;
    private int maxRoommates;
    private boolean isActive;

    public Listing(String id, String type, int size, double price, int floor,
                   int rooms, boolean canShare, int maxRoommates) {
        this.id = id;
        this.type = type;
        this.size = size;
        this.price = price;
        this.floor = floor;
        this.rooms = rooms;
        this.canShare = canShare;
        this.maxRoommates = maxRoommates;
        this.isActive = true; // default
    }

    // Νέος constructor για φόρτωση από βάση με πεδίο active
    public Listing(String id, String ownerID, String type, int size, double price, int floor,
                   int rooms, boolean canShare, int maxRoommates, boolean isActive) {
        this.id = id;
        this.ownerID = ownerID;
        this.type = type;
        this.size = size;
        this.price = price;
        this.floor = floor;
        this.rooms = rooms;
        this.canShare = canShare;
        this.maxRoommates = maxRoommates;
        this.isActive = isActive;
    }

    // Getters
    public String getId() { return id; }
    public String getOwnerID() { return ownerID; }
    public String getType() { return type; }
    public int getSize() { return size; }
    public double getPrice() { return price; }
    public int getFloor() { return floor; }
    public int getRooms() { return rooms; }
    public boolean canShare() { return canShare; }
    public int getMaxRoommates() { return maxRoommates; }
    public boolean isActive() { return isActive; }
    public boolean isArchived() { return !isActive; }
    public void setActive(boolean active) {
        this.isActive = active;
    }

    // Setters
    public void setOwnerID(String ownerID) { this.ownerID = ownerID; }
    public void setArchived(boolean archived) { this.isActive = !archived; }

    public static List<Listing> filterActiveListings(List<Listing> listings) {
        List<Listing> active = new ArrayList<>();
        for (Listing l : listings) {
            if (l.isActive()) {
                active.add(l);
            }
        }
        return active;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                ", Owner: " + (ownerID != null ? ownerID : "N/A") +
                ", Τύπος: " + type +
                ", Εμβαδόν: " + size +
                ", Τιμή: " + price +
                ", Όροφος: " + floor +
                ", Δωμάτια: " + rooms +
                ", Συγκατοίκηση: " + (canShare ? "Ναι" : "Όχι") +
                ", Μέγιστοι Συγκάτοικοι: " + maxRoommates +
                ", Ενεργή: " + (isActive ? "Ναι" : "Όχι");
    }
}
