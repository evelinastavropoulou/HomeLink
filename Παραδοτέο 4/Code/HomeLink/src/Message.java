public class Message {
    public static void createMessage(String listingID, String status) {
        System.out.println("[Message] Listing " + listingID + " status: " + status);
    }

    public static String createErrorMessage(String message) {
        System.out.println("[ERROR] " + message);
        return message;
    }

    public static String createSuccessMessage(String message) {
        System.out.println("[SUCCESS] " + message);
        return message;
    }

        public static void createPromptMessage(String msg) {
            System.out.println("[ΕΡΩΤΗΣΗ] " + msg);
        }


}
