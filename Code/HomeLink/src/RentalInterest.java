import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class RentalInterest {


    public static List<String> getListingInterests(List<Listing> listings) {
        return ManageDB.queryInterestsByListings(listings);
    }

    public static List<String> getUserIdsFromInterests(List<Listing> listings) {
        return ManageDB.queryUserIdsFromInterests(listings);
    }

    public static List<String> getRentalInterests(String listingId) {
        List<String> ids = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:homelink.db")) {
            String sql = "SELECT id FROM interests WHERE listing_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, listingId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ids.add(rs.getString("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }


    public static void deleteListingInterests(String listingId) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:homelink.db")) {
            String sql = "DELETE FROM interests WHERE listing_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, listingId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
