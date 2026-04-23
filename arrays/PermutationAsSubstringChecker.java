
class PermutationAsASubstringChecker {

public boolean isPermutationPresentAsSubstringv1(String m, String n) {
    if (m.length() < n.length()) return false;

    int[] freq = new int[128];

    // Build frequency of target string
    for (char c : n.toCharArray()) {
        freq[c]++;
    }

    int required = n.length();
    int left = 0;

    for (int right = 0; right < m.length(); right++) {

        char rightChar = m.charAt(right);

        // include rightChar into window
        if (freq[rightChar] > 0) {
            required--;
        }
        freq[rightChar]--;

        // maintain fixed window size
        if (right - left + 1 > n.length()) {
            char leftChar = m.charAt(left);

            freq[leftChar]++;
            if (freq[leftChar] > 0) {
                required++;
            }
            left++;
        }

        // check valid permutation
        if (required == 0) {
            return true;
        }
    }

    return false;
}


    public boolean isPermutationPresentAsSubstring(String m, String n) {
        if (m.length() < n.length()) {
            return false;
        }

        int required = n.length();
        int[] letters = new int[128];
        for (char ch : n.toCharArray()) {
            letters[ch]++;
        }

        // calculate initial window size first
        int left = 0, right = 0;
        while (right < n.length()) {
            if (letters[m.charAt(right)] > 0) {
                required--;
            }
            letters[m.charAt(right++)]--;
        }

        // If initial window itself contain n
        if (required == 0) {
            return true;
        }

        while (right < m.length()) {
            char leftChar = m.charAt(left);
            letters[leftChar]++;
            if (letters[leftChar] > 0) {
                required++;
            }
            left++;
            right++;
            if (letters[m.charAt(right)] > 0) {
                required--;
            }
            if (required == 0) {
                return true;
            }
            letters[m.charAt(right)]--;
        }
        return false;
    }
}
