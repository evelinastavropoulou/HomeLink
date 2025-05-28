import java.sql.*;

public class DBInit {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:homelink.db")) {
            Statement stmt = conn.createStatement();

            // Δημιουργία πίνακα χρηστών
            stmt.execute("CREATE TABLE IF NOT EXISTS owners (" +
                    "id TEXT PRIMARY KEY)");

            // Δημιουργία πίνακα αγγελιών με πεδίο 'address' και 'active'
            stmt.execute("CREATE TABLE IF NOT EXISTS listings (" +
                    "id TEXT PRIMARY KEY," +
                    "owner_id TEXT," +
                    "type TEXT," +
                    "address TEXT," +
                    "size INTEGER," +
                    "price REAL," +
                    "floor INTEGER," +
                    "rooms INTEGER," +
                    "can_share BOOLEAN," +
                    "max_roommates INTEGER," +
                    "archived BOOLEAN," +
                    "active BOOLEAN)");

            // Δημιουργία πίνακα προτιμήσεων χρήστη
            stmt.execute("CREATE TABLE IF NOT EXISTS user_preferences (" +
                    "owner_id TEXT PRIMARY KEY," +
                    "location TEXT," +
                    "type TEXT," +
                    "can_share BOOLEAN," +
                    "rent_price REAL," +
                    "size_sqm REAL)");


            // Εισαγωγή χρηστών
            stmt.execute("INSERT OR IGNORE INTO owners VALUES ('anna')");
            stmt.execute("INSERT OR IGNORE INTO owners VALUES ('owner2')");
            stmt.execute("INSERT OR IGNORE INTO owners VALUES ('owner3')");

            // Εισαγωγή αρχικών αγγελιών
            PreparedStatement insertListing = conn.prepareStatement(
                    "INSERT OR IGNORE INTO listings " +
                            "(id, owner_id, type, address, size, price, floor, rooms, can_share, max_roommates, archived, active) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            insertListing.setString(1, "listing001");
            insertListing.setString(2, "anna");
            insertListing.setString(3, "Διαμέρισμα");
            insertListing.setString(4, "Athens");
            insertListing.setInt(5, 80);
            insertListing.setDouble(6, 450.0);
            insertListing.setInt(7, 2);
            insertListing.setInt(8, 3);
            insertListing.setBoolean(9, true);
            insertListing.setInt(10, 2);
            insertListing.setBoolean(11, false);
            insertListing.setBoolean(12, true);
            insertListing.executeUpdate();

            insertListing.setString(1, "listing002");
            insertListing.setString(2, "anna");
            insertListing.setString(3, "Studio");
            insertListing.setString(4, "Athens");
            insertListing.setInt(5, 45);
            insertListing.setDouble(6, 300.0);
            insertListing.setInt(7, 1);
            insertListing.setInt(8, 2);
            insertListing.setBoolean(9, false);
            insertListing.setInt(10, 1);
            insertListing.setBoolean(11, false);
            insertListing.setBoolean(12, true);
            insertListing.executeUpdate();

            insertListing.setString(1, "listing004");
            insertListing.setString(2, "anna");
            insertListing.setString(3, "Studio");
            insertListing.setString(4, "Athens");
            insertListing.setInt(5, 45);
            insertListing.setDouble(6, 300.0);
            insertListing.setInt(7, 1);
            insertListing.setInt(8, 2);
            insertListing.setBoolean(9, false);
            insertListing.setInt(10, 1);
            insertListing.setBoolean(11, false);
            insertListing.setBoolean(12, true);
            insertListing.executeUpdate();

            insertListing.setString(1, "listing003");
            insertListing.setString(2, "anna");
            insertListing.setString(3, "Studio");
            insertListing.setString(4, "Piraeus");
            insertListing.setInt(5, 45);
            insertListing.setDouble(6, 300.0);
            insertListing.setInt(7, 1);
            insertListing.setInt(8, 2);
            insertListing.setBoolean(9, false);
            insertListing.setInt(10, 1);
            insertListing.setBoolean(11, false);
            insertListing.setBoolean(12, true);
            insertListing.executeUpdate();

            // Εισαγωγή ενδεικτικών προτιμήσεων για έναν χρήστη
            PreparedStatement insertPrefs = conn.prepareStatement(
                    "INSERT OR REPLACE INTO user_preferences " +
                            "(owner_id, location, type, can_share, rent_price, size_sqm) " +
                            "VALUES (?, ?, ?, ?, ?, ?)");
            insertPrefs.setString(1, "anna");
            insertPrefs.setString(2, "Athens");
            insertPrefs.setString(3, "Studio");
            insertPrefs.setBoolean(4, true);
            insertPrefs.setDouble(5, 400.0); // Τιμή ενοικίου
            insertPrefs.setDouble(6, 40.0);  // Τετραγωνικά
            insertPrefs.executeUpdate();


            System.out.println("✅ Database initialized with listings, owners and preferences.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
