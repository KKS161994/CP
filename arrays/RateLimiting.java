// * Rate limiting 
// Time bucket 
// Another request should be allowed 
// public RateLimiting(int n)
// Request be at user level
// Rate limiting 
// Irrespective of user
// allowRequest(int bucket) 
// bucket 
// every bucket should allow n request
// Map<bucket, request> -> <n
// Single threaded 

import java.util.HashMap;
import java.util.Map;

public class RateLimiting {
    // user -> <bucket to count map>
    private Map<Integer, Map<Integer, Integer>> usertoBucketMap;
    private int maxRequestAllowed;
    private int topK;
    // Map<Integer,PriorityQueue<>> // client id in bucket
    // Map<bucket to count for each client > 
    // Pull all elements 
    //  bucket 1 client id present precalculated 
    //  topK() -> 
    RateLimiting(int n) {
        this.maxRequestAllowed = n;
        this.usertoBucketMap = new HashMap<>();
        this.topK=10;
    }

    boolean allow(int userId, int bucket) {
        usertoBucketMap.putIfAbsent(userId, new HashMap<>());
        Map<Integer, Integer> bucketToCount = usertoBucketMap.getOrDefault(userId, new HashMap<>());
        int currentRequestCount = bucketToCount.getOrDefault(bucket, 0);
        if (currentRequestCount > maxRequestAllowed - 1)
            return false;
        bucketToCount.put(bucket, currentRequestCount + 1);
        return true;
    }

    public static void main(String[] args) {
        RateLimiting rateLimiting = new RateLimiting(2);
        System.out.println(rateLimiting.allow(1, 1));
        System.out.println(rateLimiting.allow(1, 1));
        System.out.println(rateLimiting.allow(2, 2));
        System.out.println(rateLimiting.allow(1, 1));

        System.out.println(rateLimiting.allow(2, 2));

        // Testing 0 requests
        // Testing for n requests
        // 

    }

    List<Integer> getTop10(int bucket){
    }
}
