import java.util.*;

public class TrustScore {

    public static Map<String, Integer> getTrustScore(List<String> userIds) {
        return ManageDB.queryTrustScores(userIds);
    }
}
