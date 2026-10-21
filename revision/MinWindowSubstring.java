public class MinWindowSubstring {
    public String minWindow(String s, String t) {
        // Your implementation
        int a[] = new int[26];
        for (char letter : t.toCharArray()) {
            a[letter - 'a']--;
        }
        int count = 0, lastIndex = 0;
        int[] result = new int[2];
        int minLength = Integer.MAX_VALUE;

        for (int i = 0; i < s.length(); i++) {
            if (a[s.charAt(i) - 'a'] < 0) {
                count++;
            }
            a[s.charAt(i) - 'a']++;
            while (count == t.length()){
                int currentLength = i - lastIndex + 1;

                if (minLength > currentLength) {
                    minLength = currentLength;
                    result[0]=lastIndex;
                    result[1]=i;
                }
                a[s.charAt(lastIndex) - 'a']--;
                if(a[s.charAt(lastIndex)-'a']<0){
                    count--;
                }
                lastIndex++;
        }
    }
            if (minLength == Integer.MAX_VALUE) {
            return "";
        }

        return s.substring(result[0],result[1]+1);
    }
}
