import java.util.Scanner;
import java.util.List;

public class SearchHousingScreen {
    private SearchHousingManager manager;

    public void setManager(SearchHousingManager manager) {
        this.manager = manager;
    }

    public void displaySearchResults(List<Listing> listings) {
        System.out.println("\n=== Αποτελέσματα Αναζήτησης ===");
        for (Listing l : listings) {
            System.out.println(l);
        }
    }

    public void displaySearchForm(SearchHousingForm form, String userID) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Εισάγετε περιοχή:");
        form.setLocation(sc.nextLine());

        System.out.println("Εισάγετε τύπο κατοικίας (π.χ. Studio, Διαμέρισμα):");
        form.setType(sc.nextLine());

        System.out.println("Επιθυμείτε δυνατότητα συγκατοίκησης; (ναι/όχι):");
        String input = sc.nextLine().trim().toLowerCase();
        form.setCanShare(input.equals("ναι"));

        manager.loadUserPreferences(form, userID); // συνεχίζει με preferences check
    }

    public void askToApplyPreferences(UserPreferences prefs, SearchHousingForm form) {
        System.out.println("📁 Εντοπίστηκαν αποθηκευμένες προτιμήσεις:");
        System.out.println("- Περιοχή: " + prefs.getLocation());
        System.out.println("- Τύπος: " + prefs.getType());
        System.out.println("- Συγκατοίκηση: " + (prefs.isCanShare() ? "Ναι" : "Όχι"));
        System.out.print("❓ Θέλετε να τις εφαρμόσουμε; (ναι/όχι): ");

        Scanner sc = new Scanner(System.in);
        String answer = sc.nextLine().trim().toLowerCase();

        if (answer.equals("ναι")) {
            acceptFilters(prefs); // αποδοχή από την οθόνη
        } else {
            declineFilters(form); // απόρριψη και συνέχιση με τη φόρμα
        }
    }

    public void acceptFilters(UserPreferences prefs) {
        manager.applyPreferences(prefs); // μόνο prefs, νέα form θα φτιαχτεί μέσα στον manager
    }

    public void declineFilters(SearchHousingForm form) {
        manager.continueWithoutPreferences(form); // συνεχίζει κανονικά με τη φόρμα
    }

    public void displayMessage(String msg) {
        System.out.println("[Μήνυμα]: " + msg);
    }

    public void displayMap(List<Marker> markers) {
        System.out.println("\n\n🗺️ Χάρτης Αγγελιών:");
        for (Marker m : markers) {
            System.out.println(m);
        }
    }

    public void displaySearchResults(List<Listing> listings, List<Marker> markers) {
        displayMap(markers);
        System.out.println("\n\n🔍 Αποτελέσματα Αναζήτησης:");
        for (Listing l : listings) {
            System.out.println(l + " [Score: " + l.getScore() + "]");
        }
    }
}
