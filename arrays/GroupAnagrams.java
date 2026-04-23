
import java.util.*;

class GroupAnagrams {

    public static List<List<String>> anagrams(List<String> words) {
        Map<String, List<String>> wordsMap = new HashMap<>();
        for (String word : words) {
            String currentWordKey = buildKey(word);
            wordsMap.computeIfAbsent(currentWordKey, k -> new ArrayList<>())
                    .add(word);
        }
        List<List<String>> result = new ArrayList<>(wordsMap.values());
        return result;
    }

    // Frequency key
    // O(nk)
    // O(n)
   
    public static String buildKey(String word) {
        int[] freq = new int[26];
        for (char c : word.toCharArray()) {
            freq[c - 'a'] += 1;
        }
        StringBuilder key = new StringBuilder();
        for (int f : freq) {
            key.append(f).append('#');
        }
        return key.toString();
    }

    // Hashing complex because keys as maps will be complex
    // Total time
    // 👉 O(n × k) (theoretical)
    // BUT ⚠️:
    // High constant factors (object creation, hashing, comparisons)
    // Space
    // Each word creates a map → O(n × 26)
    // 👉 effectively O(n) but memory heavy
    // public static Map<Character, Integer> convertWordToMap(String word) {
    //     Map<Character, Integer> wordMap = new HashMap<>();
    //     for (char c : word.toCharArray()) {
    //         wordMap.putIfAbsent(c, 0);
    //         wordMap.put(c, wordMap.get(c) + 1);
    //     }
    //     return wordMap;
    // }
    //     
    //     Total time

    // 👉 O(n × k log k)

    // Space
    // Sorted string per word → O(n × k)
    // public static List<List<String>> anagrams(List<String> words) {
    //     Map<String, List<String>> map = new HashMap<>();
    //     for (String word : words) {
    //         char[] chars = word.toCharArray();
    //         Arrays.sort(chars);
    //         String key = new String(chars);
    //         map.putIfAbsent(key, new ArrayList<>());
    //         map.get(key).add(word);
    //     }
    //     return new ArrayList<>(map.values());
    // }
}
