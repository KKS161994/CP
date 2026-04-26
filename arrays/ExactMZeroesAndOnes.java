class ExactMZeroesAndOnes {

    public int findMaxFormExact(String[] strs, int m, int n) {

        int[][] dp = new int[m + 1][n + 1];

        // initialize with -1 meaning "unreachable"
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                dp[i][j] = Integer.MIN_VALUE;
            }
        }

        dp[0][0] = 0; // exact zero zeros and zero ones

        for (String s : strs) {

            int[] count = getCount(s);
            int zeros = count[0];
            int ones = count[1];

            for (int i = m; i >= zeros; i--) {
                for (int j = n; j >= ones; j--) {

                    if (dp[i - zeros][j - ones] != Integer.MIN_VALUE) {

                        dp[i][j] = Math.max(
                            dp[i][j],
                            dp[i - zeros][j - ones] + 1
                        );
                    }
                }
            }
        }

        return dp[m][n] == Integer.MIN_VALUE ? 0 : dp[m][n];
    }

    private int[] getCount(String s) {
        int z = 0, o = 0;
        for (char c : s.toCharArray()) {
            if (c == '0') z++;
            else o++;
        }
        return new int[]{z, o};
    }
}