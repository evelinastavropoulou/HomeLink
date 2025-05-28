public class Owner {
    private String ownerID;

    public Owner(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getId() {
        return this.ownerID;
    }

    public void onCreateListingClicked(MainScreen screen) {
        screen.onCreateListingClicked(this.ownerID);
    }

    public void onSearchHousingClicked(MainScreen screen, String userID) {
        SearchHousingManager manager = new SearchHousingManager();
        SearchHousingScreen searchScreen = new SearchHousingScreen();
        searchScreen.setManager(manager);
        manager.setScreen(searchScreen);
        manager.initiateSearchHousing(userID);
    }


    public void onInterestDeclarationsClicked(MainScreen screen, String ownerID) {
        RentalRequestManager.startRentalProcess(screen, ownerID);
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
