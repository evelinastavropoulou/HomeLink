import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class UploadPhotoForm {

    public static List<String> uploadPhotos() {
        Scanner sc = new Scanner(System.in);
        List<String> photos = new ArrayList<>();

        System.out.println("\n--- Φόρτωση Φωτογραφιών ---");
        System.out.println("Δώστε ονόματα αρχείων (π.χ., photo1.jpg). Πληκτρολόγησε 'τέλος' για ολοκλήρωση.");

        while (true) {
            System.out.print("Φωτογραφία: ");
            String filename = sc.nextLine();
            if (filename.equalsIgnoreCase("τέλος")) {
                break;
            }
            photos.add(filename);
        }

        return photos;
    }

    public static void removeInvalidPhotos(List<String> allPhotos, List<String> invalidPhotos) {
        allPhotos.removeAll(invalidPhotos);
    }

}
