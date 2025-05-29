import java.util.*;

public class Photos {
    private List<String> photoPaths = new ArrayList<>();

    public void attachPhotos(List<String> photos) {
        this.photoPaths.addAll(photos);
    }

    public List<String> getPhotos() {
        return photoPaths;
    }
}
