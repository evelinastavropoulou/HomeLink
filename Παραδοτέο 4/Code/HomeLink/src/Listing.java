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
    private boolean isActive = true;

    // Constructor
    public Listing(String id, String type, int size, double price, int floor, int rooms, boolean canShare, int maxRoommates) {
        this.id = id;
        this.type = type;
        this.size = size;
        this.price = price;
        this.floor = floor;
        this.rooms = rooms;
        this.canShare = canShare;
        this.maxRoommates = maxRoommates;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public int getRooms() {
        return rooms;
    }

    public int getMaxRoommates() {
        return maxRoommates;
    }

    public boolean isActive() {
        return isActive;
    }

    // Setters
    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public void setArchived(boolean archived) {
        this.isActive = !archived;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public int getFloor() {
        return floor;
    }

    public boolean canShare() {
        return canShare;
    }

    public boolean isArchived() {
        return !isActive;
    }


    // Static helper
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
