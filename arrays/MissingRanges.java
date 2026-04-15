import java.util.*;
class MissingRanges{

// public static List<List<Integer>> findMissingRanges(int[] nums, int lower, int upper){
//     List<List<Integer>> missingRanges = new ArrayList<>();
//     int left = 0;
//     while(left < nums.length && nums[left] < lower ){
//         left++;
//     }
//     if(left == nums.length){
//         missingRanges.add(Arrays.asList(lower, upper));
//         return missingRanges;
//     }
//     if(nums[left] > lower){
//         missingRanges.add(Arrays.asList(lower, nums[left] - 1));
//     }
//     while(left < nums.length - 1){
//         if(nums[left + 1] > nums[left] + 1){
//             missingRanges.add(Arrays.asList(nums[left] + 1, nums[left + 1] - 1));
//         }
//         left++;
//     }
//     if(nums[left] < upper){
//         missingRanges.add(Arrays.asList(nums[left] + 1, upper));
//     }
//     return missingRanges;       
// }

public static List<List<Integer>> findMissingRanges(int[] nums, int lower, int upper){
    List<List<Integer>> missingRanges = new ArrayList<>();
    int currentLower = lower;
    for(int num : nums){
        if(num < currentLower) continue;
        if(num > upper) break;  
        if(num > currentLower){
                missingRanges.add(Arrays.asList(currentLower, num - 1));
            }
            currentLower = num + 1;
        
    }
    if(currentLower <= upper){
        missingRanges.add(Arrays.asList(currentLower, upper));
    }
    return missingRanges;   
}

public static void main(String[] args) {
    try(Scanner s = new Scanner(System.in)){
        System.out.println("Enter the number of elements in the array: ");
        int n = s.nextInt();
        int[] nums = new int[n];
        System.out.println("Enter the elements of the array: ");
        for(int i = 0; i < n; i++){
            nums[i] = s.nextInt();    
        }
        System.out.println("Enter the lower bound: ");
        int lower = s.nextInt();
        System.out.println("Enter the upper bound: ");
        int upper = s.nextInt();
        List<List<Integer>> missingRanges = findMissingRanges(nums, lower, upper);
        System.out.println("Missing ranges: " + missingRanges);
    }
}
}