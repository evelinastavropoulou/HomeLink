public class Owner {
    private String ownerID;

    public Owner(String ownerID) {
        this.ownerID = ownerID;
    }

    public void onCreateListingClicked(MainScreen screen) {
        screen.onCreateListingClicked(this.ownerID);
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void archiveListing(String listingID) {
        System.out.println("Owner archived listing " + listingID);
    }

    public void cancelListingArchive() {
        System.out.println("Owner canceled archive action.");
    }
}
