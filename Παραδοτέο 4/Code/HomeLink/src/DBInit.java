import java.sql.*;

public class DBInit {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:homelink.db")) {
            Statement stmt = conn.createStatement();

            // Δημιουργία πίνακα χρηστών
            stmt.execute("CREATE TABLE IF NOT EXISTS owners (" +
                    "id TEXT PRIMARY KEY)");

            // Δημιουργία πίνακα αγγελιών με επιπλέον πεδίο 'active'
            stmt.execute("CREATE TABLE IF NOT EXISTS listings (" +
                    "id TEXT PRIMARY KEY," +
                    "owner_id TEXT," +
                    "type TEXT," +
                    "size INTEGER," +
                    "price REAL," +
                    "floor INTEGER," +
                    "rooms INTEGER," +
                    "can_share BOOLEAN," +
                    "max_roommates INTEGER," +
                    "archived BOOLEAN," +
                    "active BOOLEAN)");

            // Εισαγωγή χρηστών
            stmt.execute("INSERT OR IGNORE INTO owners VALUES ('anna')");
            stmt.execute("INSERT OR IGNORE INTO owners VALUES ('owner2')");
            stmt.execute("INSERT OR IGNORE INTO owners VALUES ('owner3')");

            // Εισαγωγή αρχικών αγγελιών για την anna
            PreparedStatement insertListing = conn.prepareStatement(
                    "INSERT OR IGNORE INTO listings " +
                            "(id, owner_id, type, size, price, floor, rooms, can_share, max_roommates, archived, active) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            insertListing.setString(1, "listing001");
            insertListing.setString(2, "anna");
            insertListing.setString(3, "Διαμέρισμα");
            insertListing.setInt(4, 80);
            insertListing.setDouble(5, 450.0);
            insertListing.setInt(6, 2);
            insertListing.setInt(7, 3);
            insertListing.setBoolean(8, true);
            insertListing.setInt(9, 2);
            insertListing.setBoolean(10, false); // archived
            insertListing.setBoolean(11, true);  // active
            insertListing.executeUpdate();

            insertListing.setString(1, "listing002");
            insertListing.setString(2, "anna");
            insertListing.setString(3, "Studio");
            insertListing.setInt(4, 45);
            insertListing.setDouble(5, 300.0);
            insertListing.setInt(6, 1);
            insertListing.setInt(7, 2);
            insertListing.setBoolean(8, false);
            insertListing.setInt(9, 1);
            insertListing.setBoolean(10, false);
            insertListing.setBoolean(11, true);
            insertListing.executeUpdate();

            insertListing.setString(1, "listing003");
            insertListing.setString(2, "anna");
            insertListing.setString(3, "Studio");
            insertListing.setInt(4, 45);
            insertListing.setDouble(5, 300.0);
            insertListing.setInt(6, 1);
            insertListing.setInt(7, 2);
            insertListing.setBoolean(8, false);
            insertListing.setInt(9, 1);
            insertListing.setBoolean(10, false);
            insertListing.setBoolean(11, true);
            insertListing.executeUpdate();

            System.out.println("✅ Database initialized with users and listings.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
