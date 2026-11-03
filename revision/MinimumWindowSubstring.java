import java.io.*;
public class MinimumWindowSubstring {

    public String minWindowv1(String s, String t) {
        if (s.equals("") || t.equals("")) return "";
        int[] freq = new int[128];
        for(char c: t.toCharArray()){
            
            freq[c]--;
        }
        int left = 0,right = 0, count = 0, startIndex = -1, minLength = Integer.MAX_VALUE;
        String orginString = s;
        // s=s.toUpperCase();
        for(right = 0 ; right <s.length();right++){
            if(freq[s.charAt(right)]<0) count++;
            freq[s.charAt(right)]++;
            while(count == t.length()){
                if(minLength>right-left+1) {
                    startIndex = left;
                    minLength = right-left+1;
                }
                freq[s.charAt(left)]--;
                if(freq[s.charAt(left)] < 0) count --;
                left++;
            }
        }
        if(startIndex == -1) return "";
        return orginString.substring(startIndex,startIndex+minLength);
    }

    public static void main(String[] args) {
        MinimumWindowSubstring mWindowSubstring = new MinimumWindowSubstring();
        System.out.println(mWindowSubstring.minWindowv1("ADOBECODEBANC", "ABC")); // BANC
        System.out.println(mWindowSubstring.minWindowv1("a", "a"));               // a
        System.out.println(mWindowSubstring.minWindowv1("a", "aa"));              // ""
        System.out.println(mWindowSubstring.minWindowv1("aa", "aa"));             // aa
        System.out.println(mWindowSubstring.minWindowv1("ab", "b"));              // b
        System.out.println(mWindowSubstring.minWindowv1("aa", "aa"));              // "aa"
    }
}
