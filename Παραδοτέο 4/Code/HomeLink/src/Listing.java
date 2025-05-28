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
    private String address;
    private double latitude;
    private double longitude;
    private double score;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
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

    public static List<Listing> fetchListings(SearchHousingForm criteria) {
        return ManageDB.queryListings(criteria);
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

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

    public void computeSuitabilityScore(SearchHousingForm criteria) {
        double score = 100.0;

        if (criteria.getLocation() != null && !criteria.getLocation().isEmpty()) {
            if (this.address == null || !this.address.equalsIgnoreCase(criteria.getLocation())) {
                score -= 30; // τιμωρία για λάθος περιοχή
            }
        }

        if (criteria.getType() != null && !criteria.getType().isEmpty()) {
            if (!this.type.equalsIgnoreCase(criteria.getType())) {
                score -= 20; // τιμωρία για λάθος τύπο
            }
        }

        if (criteria.isCanShare() && !this.canShare) {
            score -= 30; // τιμωρία για συγκατοίκηση που δεν υποστηρίζεται
        }

        this.score = Math.max(score, 0);
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
