
import java.util.Scanner;

public class ContainerWithMostWater{
    private  static int maxArea(int[] heights){
        int left = 0, right = heights.length-1, maxArea = Integer.MIN_VALUE;
        
        while(left<right){
            int length = right-left;
            int breadth = Math.min(heights[right],heights[left]);
            maxArea = Math.max(maxArea,length*breadth);
            if(heights[left]<heights[right])
                left++;
            else
                right--;
        }

        return maxArea;
    }
    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)){
               int n = scanner.nextInt();
               int height[] = new int[n];
               for(int i = 0; i<n; i++){
                    height[i] = scanner.nextInt();
               }
               System.out.println("Max height is "+ maxArea(height));
        }
    }
}

