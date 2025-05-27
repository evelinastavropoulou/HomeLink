import java.sql.*;

public class DBInit {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:homelink.db")) {
            Statement stmt = conn.createStatement();

            stmt.execute("CREATE TABLE IF NOT EXISTS owners (id TEXT PRIMARY KEY)");
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
                    "archived BOOLEAN)");

            // Προσθήκη χρήστη
            stmt.execute("INSERT OR IGNORE INTO owners VALUES ('owner123')");

            System.out.println("✅ Database ready.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
