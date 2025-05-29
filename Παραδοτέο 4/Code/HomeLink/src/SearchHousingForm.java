public class SearchHousingForm {
    private String location;
    private String type;
    private boolean canShare;
    private int rooms;
    private String preferredFloor;

    public boolean validateSearchForm() {
        return location != null && !location.isEmpty() &&
                type != null && !type.isEmpty();
    }

    public Boolean getCanShare() {
        return canShare;
    }

    public void setRooms(int rooms) { this.rooms = rooms; }
    public void setPreferredFloor(String floor) { this.preferredFloor = floor; }

    // Getters and Setters
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isCanShare() { return canShare; }
    public void setCanShare(boolean canShare) { this.canShare = canShare; }
}

