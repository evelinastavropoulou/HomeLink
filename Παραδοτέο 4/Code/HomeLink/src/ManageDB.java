import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

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


    public static List<Listing> getListingsByOwner(String ownerID) {
        List<Listing> listings = new ArrayList<>();
        String sql = "SELECT * FROM listings WHERE active = 1 AND owner_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:homelink.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ownerID);
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
                l.setAddress(rs.getString("address"));
                listings.add(l);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listings;
    }


    public static List<String> queryInterestsByListingId(String listingID) {
        List<String> interests = new ArrayList<>();
        String sql = "SELECT user_id, listing_id FROM interests WHERE listing_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:homelink.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, listingID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String userId = rs.getString("user_id");
                String lId = rs.getString("listing_id");
                interests.add(userId + ";" + lId); // ή userId + " για " + lId αν θες για εμφάνιση
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return interests;
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
        String loggedInUser = getLoggedInOwner(); // <-- το παίρνουμε από παντού

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:homelink.db")) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM listings WHERE active = 1 AND owner_id != ?"
            );
            stmt.setString(1, loggedInUser);
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
        StringBuilder sql = new StringBuilder("SELECT * FROM listings WHERE active = 1");

        String loggedInUser = getLoggedInOwner(); // <-- παίρνουμε τον συνδεδεμένο χρήστη

        // Αποκλείουμε αγγελίες του ίδιου χρήστη
        if (loggedInUser != null && !loggedInUser.isEmpty()) {
            sql.append(" AND owner_id != ?");
        }

        if (criteria.getLocation() != null && !criteria.getLocation().isEmpty()) {
            sql.append(" AND LOWER(address) LIKE ?");
        }
        if (criteria.getType() != null && !criteria.getType().isEmpty()) {
            sql.append(" AND LOWER(type) = ?");
        }
        if (criteria.getCanShare() != null) {
            sql.append(" AND can_share = ?");
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:homelink.db");
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;

            // 1. owner_id != ?
            if (loggedInUser != null && !loggedInUser.isEmpty()) {
                stmt.setString(index++, loggedInUser);
            }

            // 2. location
            if (criteria.getLocation() != null && !criteria.getLocation().isEmpty()) {
                stmt.setString(index++, "%" + criteria.getLocation().toLowerCase() + "%");
            }

            // 3. type
            if (criteria.getType() != null && !criteria.getType().isEmpty()) {
                stmt.setString(index++, criteria.getType().toLowerCase());
            }

            // 4. can_share
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


    public static Listing getListingDetails(String listingID) {
        Listing listing = null;

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM listings WHERE id = ?");
            stmt.setString(1, listingID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                listing = new Listing(
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
                listing.setArchived(rs.getBoolean("archived"));
                listing.setAddress(rs.getString("address"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listing;
    }


    public static boolean interestExists(String userId, String listingId) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:homelink.db")) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT 1 FROM interests WHERE user_id = ? AND listing_id = ?");
            stmt.setString(1, userId);
            stmt.setString(2, listingId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Map<String, String> getInterest(String userId, String listingId) {
        Map<String, String> details = new HashMap<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:homelink.db")) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT message, timestamp FROM interests WHERE user_id = ? AND listing_id = ?");
            stmt.setString(1, userId);
            stmt.setString(2, listingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                details.put("message", rs.getString("message"));
                details.put("timestamp", rs.getString("timestamp"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }


    public static void insertInterest(String userId, String listingId, String message) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:homelink.db")) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO interests (user_id, listing_id, message, timestamp) VALUES (?, ?, ?, CURRENT_TIMESTAMP)");
            stmt.setString(1, userId);
            stmt.setString(2, listingId);
            stmt.setString(3, message);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean deleteInterest(String userId, String listingId) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:homelink.db")) {
            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM interests WHERE user_id = ? AND listing_id = ?");
            stmt.setString(1, userId);
            stmt.setString(2, listingId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void saveUserAvailability(String userID, String listingID, List<String> timeSlots) {
        String sql = "INSERT INTO availability (user_id, listing_id, timeslot) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:homelink.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (String slot : timeSlots) {
                pstmt.setString(1, userID);
                pstmt.setString(2, listingID);
                pstmt.setString(3, slot);
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            System.out.println("[DB] ✅ Αποθηκεύτηκε διαθεσιμότητα για " + userID);

        } catch (SQLException e) {
            System.err.println("[DB] ❌ Σφάλμα κατά την αποθήκευση διαθεσιμότητας: " + e.getMessage());
        }
    }


    public static List<String> queryOwnerAvailability(String ownerID) {
        List<String> timeSlots = new ArrayList<>();
        String sql = "SELECT timeslot FROM availability WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:homelink.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ownerID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                timeSlots.add(rs.getString("timeslot"));
            }

        } catch (SQLException e) {
            System.err.println("[DB] ❌ Σφάλμα ανάκτησης availability: " + e.getMessage());
        }

        return timeSlots;
    }


    public static String getOwnerIDByListing(String listingID) {
        // Προσομοίωση
        return "owner123"; // Replace with real query: SELECT ownerID FROM listings WHERE id = ?
    }


    public static Map<String, Integer> queryTrustScores(List<String> userIds) {
        Map<String, Integer> trustScores = new HashMap<>();

        String sql = "SELECT user_id, trust_score FROM users WHERE user_id IN ("
                + userIds.stream().map(id -> "?").collect(Collectors.joining(", ")) + ")";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:homelink.db");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int index = 1;
            for (String id : userIds) {
                stmt.setString(index++, id);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String userId = rs.getString("user_id");
                int score = rs.getInt("trust_score");
                trustScores.put(userId, score);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trustScores;
    }



}
