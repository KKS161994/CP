/***
 * 1. Connect Ropes (LeetCode 1167 — Minimum Cost to Connect Sticks)
You have some number of ropes, each with a positive integer length. You want to connect all the ropes into one rope. The cost of connecting two ropes is equal to the sum of their lengths. Connect the ropes into one rope with minimum total cost.
Given an array sticks where sticks[i] is the length of the i-th stick, return the minimum cost of connecting all the sticks into one stick.
Example: sticks = [2, 4, 3] → return 14.
 */
import java.util.*;
class MinimumRopeCombineCost{
    public int minCost(int[] lengths){
        if(lengths == null || lengths.length<2) return 0;
        PriorityQueue<Integer> minSticks = new PriorityQueue<>();
        int minCost = 0, tempSum;
        for(int length: lengths){
            minSticks.add(length);
        } 
        while(minSticks.size()>1){
            tempSum = minSticks.poll() + minSticks.poll();
            minCost += tempSum;
            minSticks.add(tempSum);
        }
        return minCost;
    }
}