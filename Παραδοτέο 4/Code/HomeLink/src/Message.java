public class Message {
    public static void createMessage(String listingID, String status) {
        System.out.println("[Message] Listing " + listingID + " status: " + status);
    }

    public static void createErrorMessage(String message) {
        System.out.println("[ERROR] " + message);
    }

    public static void createSuccessMessage(String message) {
        System.out.println("[SUCCESS] " + message);
    }

        public static void createPromptMessage(String msg) {
            System.out.println("[ΕΡΩΤΗΣΗ] " + msg);
        }


}
