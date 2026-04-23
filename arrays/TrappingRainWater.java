// 42. Trapping Rain Water
// Solved
// Hard
// Topics
// conpanies icon
// Companies
// Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.



import java.io.*;
import java.util.*;

class TrappingRainWater {
    public int waterContained(int[] heights){
        
        return 0;
    }   


    public int leftpass(int[] heights){
        int leftMaxIndex = 0;
        int maxArea = 0,currentArea = 0;
        for(int index = 1; index<heights.length; index++){
            if(heights[index]>=heights[leftMaxIndex]){
                leftMaxIndex = index;
                maxArea +=currentArea;
                currentArea = 0;
            }
            else{
                currentArea += heights[leftMaxIndex]-heights[index];
            }
        }
        return maxArea;
    }
       public int rightpass(int[] heights){
        int rightMaxIndex = heights.length-1;
        int maxArea = 0,currentArea = 0;;
        for(int index =heights.length-2; index>0; index--){
            if(heights[index]>=heights[rightMaxIndex]){
                rightMaxIndex = index;
                maxArea +=currentArea;
                currentArea = 0;
            }
            else{
                currentArea += heights[rightMaxIndex]-heights[index];
            }
        }
        return maxArea;
    }

    public int maxArea(int[] heights){
        int left = 0, right = heights.length-1, leftMax = heights[left],
         rightMax = heights[right], maxArea = 0;

        while(left<right){
            if(heights[left]<heights[right]){
                if(leftMax<heights[left]){
                    leftMax = heights[left];
                }
                else{
                    maxArea+=leftMax-heights[left];
                }
                left++;
            }
            else {
                 if(rightMax<heights[right]){
                    rightMax = heights[right];
                }
                else{
                    maxArea+=rightMax-heights[right];
                }
                right--;
            }
        }
        return maxArea;
    }
}