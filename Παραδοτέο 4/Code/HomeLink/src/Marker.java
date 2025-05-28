public class Marker {
    private double latitude;
    private double longitude;
    private String label;
    private String listingId;

    public Marker(double latitude, double longitude, String label, String listingId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.label = label;
        this.listingId = listingId;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getLabel() { return label; }
    public String getListingId() { return listingId; }

    @Override
    public String toString() {
        return "📍 Marker [ID: " + listingId + ", " + label + " @ " + latitude + "," + longitude + "]";
    }
}
