
import java.util.*;

public class CommodityPriceTracker {
    Map<Integer, Integer> timestampToPrice;
    TreeMap<Integer, Integer> priceFrequency;
    int maxTimestamp;

    public CommodityPriceTracker() {
        // Implement
        timestampToPrice = new HashMap<>();
        priceFrequency = new TreeMap<>();
        maxTimestamp = -1;
    }

    public void update(int timestamp, int price) {
        // Implement
        maxTimestamp = Math.max(timestamp, maxTimestamp);
        int oldPrice = timestampToPrice.getOrDefault(timestamp, -1);
        if (oldPrice != -1) {
            int count = priceFrequency.get(oldPrice);
            if (count > 1) {
                priceFrequency.put(oldPrice, priceFrequency.getOrDefault(oldPrice, 0) - 1);
            } else
                priceFrequency.remove(oldPrice);
        }
        timestampToPrice.put(timestamp, price);
        priceFrequency.put(price, priceFrequency.getOrDefault(price, 0) + 1);
    }

    public int current() {
        // Implement
        if (maxTimestamp != 0) {
           return timestampToPrice.get(maxTimestamp);
        }
        return -1;
    }

    public int maximum() {
        // Implement
        if (maxTimestamp != -1)
            return priceFrequency.lastEntry().getKey();
        return -1;
    }

    public int minimum() {
        // Implement
        if (maxTimestamp != -1)
            return priceFrequency.firstEntry().getKey();
        return -1;
    }

    private static void check(String name, boolean condition) {
        System.out.println(
                (condition ? "PASS" : "FAIL") + " | " + name);
    }

    public static void main(String[] args) {
        CommodityPriceTracker tracker = new CommodityPriceTracker();

        tracker.update(1, 10);
        tracker.update(2, 5);

        check("Current price", tracker.current() == 5);
        check("Maximum price", tracker.maximum() == 10);
        check("Minimum price", tracker.minimum() == 5);

        tracker.update(1, 3);

        check("Corrected maximum", tracker.maximum() == 5);
        check("Corrected minimum", tracker.minimum() == 3);

        tracker.update(4, 2);

        check("Latest price", tracker.current() == 2);
        check("New minimum", tracker.minimum() == 2);
    }
}
