
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

    public static Map<Character, Integer> convertWordToMap(String word) {
        Map<Character, Integer> wordMap = new HashMap<>();
        for (char c : word.toCharArray()) {
            wordMap.putIfAbsent(c, 0);
            wordMap.put(c, wordMap.get(c) + 1);
        }
        return wordMap;
    }
}
