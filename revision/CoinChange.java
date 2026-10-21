import java.util.Arrays;

public class CoinChange {
    public int coinChange(int[] coins, int amount) {
       int minCoin = minCoins(
        coins, amount, 0, 0, Integer.MAX_VALUE);
        if(minCoin == Integer.MAX_VALUE) return -1;
        return minCoin;
    }

    private int minCoins(int[] coins, int amount, 
        int count, int total, int minCount){
            if (count >= minCount) return minCount;
            if(total>amount) return minCount;
            if(total == amount) return Math.min(count,minCount);
            for(int coin:coins){
                minCount = Math.min(minCount,
                    minCoins(coins, amount, count+1, total+coin, minCount));
            }
            return minCount;
    }


    public int minCoinsv1(int[] coins, int amount) {
        int dpCoin[] = new int[amount+1];
        Arrays.fill(dpCoin, Integer.MAX_VALUE);
        dpCoin[0]=0;
        for(int i = 1; i<=amount;i++){
            for(int coin:coins){
                int left = i - coin;
                if(left<0 || dpCoin[left] == Integer.MAX_VALUE) continue;
                dpCoin[i] = Math.min(dpCoin[i],dpCoin[left]+1);
                
            }
        }
        if(dpCoin[amount] != Integer.MAX_VALUE) return dpCoin[amount];
        return -1;
    }
}
