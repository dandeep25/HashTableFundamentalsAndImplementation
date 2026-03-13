import java.util.*;

class Video {
    String videoId;
    String data;

    Video(String id, String data) {
        this.videoId = id;
        this.data = data;
    }
}

 class MultiLevelCache {

    // L1 Cache (LRU using LinkedHashMap)
    private LinkedHashMap<String, Video> L1;

    // L2 Cache
    private LinkedHashMap<String, Video> L2;

    // L3 Database
    private HashMap<String, Video> L3;

    // Access counter
    private HashMap<String, Integer> accessCount;

    int L1_CAPACITY = 10000;
    int L2_CAPACITY = 100000;

    int L1Hits = 0;
    int L2Hits = 0;
    int L3Hits = 0;

    public MultiLevelCache() {

        accessCount = new HashMap<>();
        L3 = new HashMap<>();

        L1 = new LinkedHashMap<>(L1_CAPACITY, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, Video> eldest) {
                return size() > L1_CAPACITY;
            }
        };

        L2 = new LinkedHashMap<>(L2_CAPACITY, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, Video> eldest) {
                return size() > L2_CAPACITY;
            }
        };
    }

    // Simulated database insert
    public void addVideoToDatabase(String id, String data) {
        L3.put(id, new Video(id, data));
    }

    // Get video
    public Video getVideo(String videoId) {

        long start = System.currentTimeMillis();

        // L1 Cache
        if (L1.containsKey(videoId)) {

            L1Hits++;
            System.out.println("L1 Cache HIT (0.5ms)");

            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        // L2 Cache
        if (L2.containsKey(videoId)) {

            L2Hits++;
            System.out.println("L2 Cache HIT (5ms)");

            Video v = L2.get(videoId);

            promoteToL1(videoId, v);

            return v;
        }

        System.out.println("L2 Cache MISS");

        // L3 Database
        if (L3.containsKey(videoId)) {

            L3Hits++;
            System.out.println("L3 Database HIT (150ms)");

            Video v = L3.get(videoId);

            L2.put(videoId, v);

            accessCount.put(videoId,
                    accessCount.getOrDefault(videoId, 0) + 1);

            return v;
        }

        System.out.println("Video not found");
        return null;
    }

    // Promote video from L2 → L1
    private void promoteToL1(String id, Video v) {

        int count = accessCount.getOrDefault(id, 0) + 1;

        accessCount.put(id, count);

        if (count > 2) {

            L1.put(id, v);
            System.out.println("Promoted to L1 cache");
        }
    }

    // Invalidate cache
    public void invalidate(String videoId) {

        L1.remove(videoId);
        L2.remove(videoId);
        L3.remove(videoId);

        System.out.println("Cache invalidated for video: " + videoId);
    }

    // Statistics
    public void getStatistics() {

        int total = L1Hits + L2Hits + L3Hits;

        if (total == 0) total = 1;

        System.out.println("\nCache Statistics");

        System.out.println("L1 Hit Rate: "
                + (L1Hits * 100.0 / total) + "%");

        System.out.println("L2 Hit Rate: "
                + (L2Hits * 100.0 / total) + "%");

        System.out.println("L3 Hit Rate: "
                + (L3Hits * 100.0 / total) + "%");

        double overall = ((L1Hits + L2Hits) * 100.0 / total);

        System.out.println("Overall Cache Hit Rate: " + overall + "%");
    }

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        // Add videos to database
        cache.addVideoToDatabase("video_123", "Movie Data");
        cache.addVideoToDatabase("video_999", "Series Data");

        System.out.println("\nFirst Request");

        cache.getVideo("video_123");

        System.out.println("\nSecond Request");

        cache.getVideo("video_123");

        System.out.println("\nAnother Request");

        cache.getVideo("video_999");

        cache.getStatistics();
    }
}
