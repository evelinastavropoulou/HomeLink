public class EmailService {

    public static String createCancellationEmail(String userID, String listingID) {
        return "Ο χρήστης " + userID + " ακύρωσε το ενδιαφέρον του για την αγγελία " + listingID + ".";
    }

    public static void sendEmail(String ownerID, String message) {
        System.out.println("\n📧 [Αποστολή email στον Ιδιοκτήτη με ID: " + ownerID + "]");
        System.out.println(message);
    }
}