import java.util.*;

public class ManageDB {
    private static List<Listing> fakeDB = new ArrayList<>();

    public List<Listing> getListingsForOwner(String ownerID) {
        return new ArrayList<>(fakeDB);
    }

    public void saveListing(Listing listing) {
        fakeDB.add(listing);
    }
}
