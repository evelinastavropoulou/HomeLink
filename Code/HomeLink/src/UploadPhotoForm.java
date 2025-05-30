import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UploadPhotoForm {

    public static List<String> uploadPhotos() {
        Scanner sc = new Scanner(System.in);
        List<String> photos = new ArrayList<>();

        System.out.println("\nΔώστε τα ονόματα αρχείων (π.χ., photo1.jpg)");
        System.out.println("Πληκτρολογήστε 'τέλος' ή 'end' ή 'stop' ή 'exit' για ολοκλήρωση.\n");

        while (true) {
            System.out.print("🖼️  Φωτογραφία: ");
            String filename = sc.nextLine().trim();

            if (filename.equalsIgnoreCase("τέλος") ||
                    filename.equalsIgnoreCase("end") ||
                    filename.equalsIgnoreCase("stop") ||
                    filename.equalsIgnoreCase("exit")) {
                break;
            }

            if (filename.isEmpty()) {
                System.out.println("⚠️  Το όνομα αρχείου δεν μπορεί να είναι κενό.");
                continue;
            }

            if (!filename.contains(".")) {
                System.out.println("⚠️  Το αρχείο πρέπει να έχει επέκταση (π.χ., .jpg, .png).");
                continue;
            }

            photos.add(filename);
            System.out.println("✅ Προστέθηκε: " + filename);
        }

        System.out.println("\n📦 Συνολικές φωτογραφίες που καταχωρήθηκαν: " + photos.size());
        for (String photo : photos) {
            System.out.println("• " + photo);
        }

        return photos;
    }

    public static void removeInvalidPhotos(List<String> allPhotos, List<String> invalidPhotos) {
        allPhotos.removeAll(invalidPhotos);
    }

}
