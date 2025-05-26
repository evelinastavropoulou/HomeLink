import java.util.*;

public class Listing {
    private String id;
    private String type;
    private int size;
    private double price;
    private int floor;
    private int rooms;
    private boolean canShare;
    private int maxRoommates;
    private boolean active = true;

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

    public static List<Listing> filterActiveListings(List<Listing> listings) {
        List<Listing> active = new ArrayList<>();
        for (Listing l : listings) {
            if (l.active) active.add(l);
        }
        return active;
    }

    public String getId() { return id; }

    public int getRooms() { return rooms; }

    public int getMaxRoommates() { return maxRoommates; }
}
