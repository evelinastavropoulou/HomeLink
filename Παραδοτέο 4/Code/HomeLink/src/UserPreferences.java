public class UserPreferences {
    private String location;
    private String type;
    private boolean canShare;
    private double rentPrice;     // Νέα ιδιότητα: Τιμή ενοικίου
    private double sizeSqm;       // Νέα ιδιότητα: Τετραγωνικά μέτρα

    // Getters και Setters
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isCanShare() { return canShare; }
    public void setCanShare(boolean canShare) { this.canShare = canShare; }

    public double getRentPrice() { return rentPrice; }
    public void setRentPrice(double rentPrice) { this.rentPrice = rentPrice; }

    public double getSizeSqm() { return sizeSqm; }
    public void setSizeSqm(double sizeSqm) { this.sizeSqm = sizeSqm; }
}
