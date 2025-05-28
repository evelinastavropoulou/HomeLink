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

    public static UserPreferences getUserPreferences(String userID) {
        UserPreferences prefs = null;

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT location, type, can_share FROM user_preferences WHERE owner_id = ?"
            );
            stmt.setString(1, userID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                prefs = new UserPreferences();
                prefs.setLocation(rs.getString("location"));
                prefs.setType(rs.getString("type"));
                prefs.setCanShare(rs.getBoolean("can_share"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prefs;
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

    public static List<Listing> getAllListings() {
        List<Listing> listings = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:homelink.db")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM listings WHERE active = 1");

            while (rs.next()) {
                Listing l = new Listing(
                        rs.getString("id"),
                        rs.getString("owner_id"),
                        rs.getString("type"),
                        rs.getInt("size"),
                        rs.getDouble("price"),
                        rs.getInt("floor"),
                        rs.getInt("rooms"),
                        rs.getBoolean("can_share"),
                        rs.getInt("max_roommates"),
                        rs.getBoolean("active")
                );
                l.setAddress(rs.getString("address"));
                listings.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listings;
    }


    public static List<Listing> queryListings(SearchHousingForm criteria) {
        List<Listing> results = new ArrayList<>();
        String sql = "SELECT * FROM listings WHERE active = 1";

        if (criteria.getLocation() != null && !criteria.getLocation().isEmpty()) {
            sql += " AND LOWER(address) LIKE ?";
        }
        if (criteria.getType() != null && !criteria.getType().isEmpty()) {
            sql += " AND LOWER(type) = ?";
        }
        if (criteria.getCanShare() != null) {
            sql += " AND can_share = ?";
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:homelink.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int index = 1;
            if (criteria.getLocation() != null && !criteria.getLocation().isEmpty()) {
                stmt.setString(index++, "%" + criteria.getLocation().toLowerCase() + "%");
            }
            if (criteria.getType() != null && !criteria.getType().isEmpty()) {
                stmt.setString(index++, criteria.getType().toLowerCase());
            }
            if (criteria.getCanShare() != null) {
                stmt.setBoolean(index++, criteria.getCanShare());
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Listing l = new Listing(
                        rs.getString("id"),
                        rs.getString("owner_id"),
                        rs.getString("type"),
                        rs.getInt("size"),
                        rs.getDouble("price"),
                        rs.getInt("floor"),
                        rs.getInt("rooms"),
                        rs.getBoolean("can_share"),
                        rs.getInt("max_roommates"),
                        rs.getBoolean("active")
                );
                l.setArchived(rs.getBoolean("archived"));
                l.setAddress(rs.getString("address"));
                results.add(l);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }


}
