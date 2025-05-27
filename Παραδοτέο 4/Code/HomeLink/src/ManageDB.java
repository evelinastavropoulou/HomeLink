import java.sql.*;
import java.util.*;

public class ManageDB {
    
    private static final String DB_URL = "jdbc:sqlite:homelink.db";

    // Ενεργός χρήστης
    private static String currentLoggedInOwner;

    public static void setLoggedInOwner(String ownerID) {
        currentLoggedInOwner = ownerID;
    }

    public static String getLoggedInOwner() {
        return currentLoggedInOwner;
    }

    public static boolean isValidOwner(String ownerID) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM owners WHERE id = ?");
            stmt.setString(1, ownerID);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveListing(Listing listing) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO listings " +
                            "(id, owner_id, type, size, price, floor, rooms, can_share, max_roommates, archived, active) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            stmt.setString(1, listing.getId());
            stmt.setString(2, listing.getOwnerID());
            stmt.setString(3, listing.getType());
            stmt.setInt(4, listing.getSize());
            stmt.setDouble(5, listing.getPrice());
            stmt.setInt(6, listing.getFloor());
            stmt.setInt(7, listing.getRooms());
            stmt.setBoolean(8, listing.canShare());
            stmt.setInt(9, listing.getMaxRoommates());
            stmt.setBoolean(10, listing.isArchived());
            stmt.setBoolean(11, listing.isActive()); // νέο πεδίο
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateListingStatusWithConnection(Connection conn, Listing listing) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "UPDATE listings SET archived = ?, active = ? WHERE id = ?")) {
            stmt.setBoolean(1, listing.isArchived());
            stmt.setBoolean(2, listing.isActive());
            stmt.setString(3, listing.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteAssociatedInterestsWithConnection(Connection conn, String listingID) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM rental_interests WHERE listing_id = ?")) {
            stmt.setString(1, listingID);
            stmt.executeUpdate();
            System.out.println("[RentalInterest] Deleted associated interests for listing: " + listingID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public List<Listing> getListingsForOwner(String ownerID) {
        List<Listing> listings = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM listings WHERE owner_id = ?");
            stmt.setString(1, ownerID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Listing l = new Listing(
                        rs.getString("id"),
                        rs.getString("type"),
                        rs.getInt("size"),
                        rs.getDouble("price"),
                        rs.getInt("floor"),
                        rs.getInt("rooms"),
                        rs.getBoolean("can_share"),
                        rs.getInt("max_roommates")
                );
                l.setOwnerID(ownerID);
                l.setArchived(rs.getBoolean("archived"));
                l.setActive(rs.getBoolean("active")); // διαβάζει και το active
                listings.add(l);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listings;
    }
}
