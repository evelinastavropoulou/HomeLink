public class VerificationProvider {
    public static boolean sendForDigitalVerification(String idCard, String taxInfo, String incomeProof) {
        // Προσομοίωση: πάντα αποτυγχάνει αν κάποιο αρχείο περιέχει "fail"
        return !idCard.contains("fail") && !taxInfo.contains("fail") && !incomeProof.contains("fail");
    }
}
