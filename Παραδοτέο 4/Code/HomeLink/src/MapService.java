import java.util.ArrayList;
import java.util.List;

public class MapService {

    public static void fetchGeolocation(List<Listing> listings) {
        for (Listing l : listings) {
            // 🎯 Dummy γεωεντοπισμός βάσει διεύθυνσης
            String address = l.getAddress();
            if (address != null && !address.isEmpty()) {
                // Για απλό mock: μετατροπή ASCII τιμών
                double lat = 37.9 + (address.charAt(0) % 10) * 0.01;
                double lon = 23.7 + (address.charAt(1) % 10) * 0.01;
                l.setLatitude(lat);
                l.setLongitude(lon);
            } else {
                l.setLatitude(0);
                l.setLongitude(0);
            }
        }
    }

    public static List<Marker> generateMarkers(List<Listing> listings) {
        List<Marker> markers = new ArrayList<>();
        for (Listing l : listings) {
            markers.add(new Marker(l.getLatitude(), l.getLongitude(), l.getAddress(), l.getId()));
        }
        return markers;
    }
}
