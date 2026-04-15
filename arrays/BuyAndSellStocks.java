/***
 * You are given an array prices where prices[i] is the price of a given stock on the ith day.

You want to maximize your profit by choosing a single day to buy one stock and choosing a different day in the future to sell that stock.

Return the maximum profit you can achieve from this transaction. If you cannot achieve any profit, return 0.

 

Example 1:

Input: prices = [7,1,5,3,6,4]
Output: 5
Explanation: Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
Note that buying on day 2 and selling on day 1 is not allowed because you must buy before you sell.
Example 2:

Input: prices = [7,6,4,3,1]
Output: 0
Explanation: In this case, no transactions are done and the max profit = 0.
 

Constraints:

1 <= prices.length <= 105
0 <= prices[i] <= 104
 */
import java.util.*;
class BuyAndSellStocks{

public static int maxProfit(int[] prices){
    int minPrice = Integer.MAX_VALUE;
    int maxProfit = 0;
    for(int price : prices){
        minPrice = Math.min(minPrice, price);
        maxProfit = Math.max(maxProfit, price - minPrice);
    }
    return maxProfit;
}
public static void main(String[] args) {
    try(Scanner s = new Scanner(System.in)){
        System.out.println("Enter the number of days: ");
        int n = s.nextInt();
        int[] prices = new int[n];
        System.out.println("Enter the prices for each day: ");
        for(int i = 0; i < n; i++){
            prices[i] = s.nextInt();    
        }
        System.out.println("Maximum profit: " + maxProfit(prices));
    }
}
}