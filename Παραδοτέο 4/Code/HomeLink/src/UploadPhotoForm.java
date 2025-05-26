import java.util.*;

public class UploadPhotoForm {
    public static List<String> uploadPhotos() {
        Scanner sc = new Scanner(System.in);
        List<String> photos = new ArrayList<>();

        System.out.println("Πόσες φωτογραφίες θέλεις να ανεβάσεις;");
        int count = sc.nextInt();
        sc.nextLine(); // consume newline

        for (int i = 0; i < count; i++) {
            System.out.print("Όνομα φωτογραφίας " + (i + 1) + ": ");
            String name = sc.nextLine();
            photos.add(name);
        }

        return photos;
    }
}
