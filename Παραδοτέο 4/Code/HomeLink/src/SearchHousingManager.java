import java.util.*;

public class SearchHousingManager {
    private SearchHousingScreen screen;

    public void setScreen(SearchHousingScreen screen) {
        this.screen = screen;
    }


    public void loadUserPreferences(SearchHousingForm form, String userID) {

        if (!form.validateSearchForm()) {
            Message.createErrorMessage("Empty Required Fields");
            screen.displayMessage("Empty Required Fields");
            return;
        }

        UserPreferences prefs = ManageDB.getUserPreferences(userID);

        if (prefs != null) {
            screen.askToApplyPreferences(prefs, form);
        } else {
            continueWithoutPreferences(form); // νέα μέθοδος, απλό forward
        }
    }

    public void continueWithoutPreferences(SearchHousingForm form) {
        handleSearchCriteria(form);
    }

    public void applyPreferences(UserPreferences prefs) {
        SearchHousingForm form = new SearchHousingForm();
        form.setLocation(prefs.getLocation());
        form.setType(prefs.getType());
        form.setCanShare(prefs.isCanShare());

        handleSearchCriteria(form);
    }


    public void initiateSearchHousing(String userID) {
        SearchHousingForm form = new SearchHousingForm();
        screen.displaySearchForm(form, userID); // ο χρήστης τη συμπληρώνει
    }


    public void handleSearchCriteria(SearchHousingForm form) {
        if (!form.validateSearchForm()) {
            Message.createErrorMessage("Empty Required Fields");
            screen.displayMessage("Empty Required Fields");
            return;
        }

        executeSearch(form);
    }



    public void executeSearch(SearchHousingForm criteria) {

        List<Listing> allListings = ManageDB.getAllListings();

        List<Listing> results = Listing.fetchListings(criteria);

        System.out.println("\n📦 Όλες οι διαθέσιμες αγγελίες:");
        for (Listing l : allListings) {
            System.out.println(l);
        }

        // Εμφάνιση απορριφθεισών αγγελιών και αιτίες
        System.out.println("\n❌ Αγγελίες που απορρίφθηκαν:");
        for (Listing l : allListings) {
            if (!results.contains(l)) {
                System.out.println("⛔ Απορρίφθηκε: " + l.getId() + " λόγω:");

                if (l.getAddress() == null || !l.getAddress().equalsIgnoreCase(criteria.getLocation())) {
                    System.out.println(" - διαφορετική περιοχή");
                }
                if (!l.getType().equalsIgnoreCase(criteria.getType())) {
                    System.out.println(" - διαφορετικός τύπος");
                }
                if (criteria.isCanShare() && !l.canShare()) {
                    System.out.println(" - δεν υποστηρίζει συγκατοίκηση");
                }
            }
        }

        // Αν δεν βρέθηκαν αγγελίες, πρότεινε διεύρυνση
        if (results.isEmpty()) {
            Message.createPromptMessage("Δεν βρέθηκαν αποτελέσματα. Θέλετε να διευρύνετε τα κριτήρια αναζήτησης; (ναι/όχι)");
            Scanner sc = new Scanner(System.in);
            String choice = sc.nextLine().trim().toLowerCase();

            if (choice.equals("ναι")) {
                broadenSearch();
            } else {
                screen.displayMessage("Η αναζήτηση ακυρώθηκε.");
            }

        } else {
            // Ενημέρωση αποτελεσμάτων με γεωεντοπισμό και score
            MapService mapService = new MapService();
            mapService.fetchGeolocation(results);
            List<Marker> markers = mapService.generateMarkers(results);

            for (Listing l : results) {
                l.computeSuitabilityScore(criteria);
            }

            sortListingsByScore(results);
            screen.displaySearchResults(results, markers);
        }
    }



    public void broadenSearch() {
        screen.displayMessage("🔍 Διεύρυνση αναζήτησης χωρίς περιοριστικά φίλτρα...");

        // Δημιουργούμε ένα νέο 'χαλαρό' φορμάρισμα
        SearchHousingForm relaxedForm = new SearchHousingForm();
        relaxedForm.setLocation("");       // Καμία τοποθεσία συγκεκριμένη
        relaxedForm.setType("");           // Όλοι οι τύποι
        relaxedForm.setCanShare(false);    // Δεν επιμένουμε σε συγκατοίκηση

        executeSearch(relaxedForm);        // Εκτελούμε αναζήτηση με ελάχιστα φίλτρα
    }

    public static void sortListingsByScore(List<Listing> listings) {
        listings.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
    }



}
