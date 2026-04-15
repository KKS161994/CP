/***
 * 
 * You are given a large integer represented as an integer array digits, where each digits[i] is the ith digit of the integer. The digits are ordered from most significant to least significant in left-to-right order. The large integer does not contain any leading 0's.

Increment the large integer by one and return the resulting array of digits.

 

Example 1:

Input: digits = [1,2,3]
Output: [1,2,4]
Explanation: The array represents the integer 123.
Incrementing by one gives 123 + 1 = 124.
Thus, the result should be [1,2,4].
Example 2:

Input: digits = [4,3,2,1]
Output: [4,3,2,2]
Explanation: The array represents the integer 4321.
Incrementing by one gives 4321 + 1 = 4322.
Thus, the result should be [4,3,2,2].
Example 3:

Input: digits = [9]
Output: [1,0]
Explanation: The array represents the integer 9.
Incrementing by one gives 9 + 1 = 10.
Thus, the result should be [1,0].
 

Constraints:

1 <= digits.length <= 100
0 <= digits[i] <= 9
digits does not contain any leading 0's.
Java	
class Solution {
    public int[] plusOne(int[] digits) {
        
    }
}
 */
class PlusOne {

    public int[] plusOne(int[] digits) {
        // No negative integers
        int[] result = new int[digits.length+1];
        int pos,len = digits.length,sum;
        int carry = (digits[len-1]+1)/10;
        
        // Add 1 to last index
        result[len] = (digits[len-1]+1)%10;
        
        //Carry forward and result putting
        for(pos=len-2;pos>=0;pos--){
             sum = digits[pos]+carry;
            carry = sum/10;
            result[pos+1]= sum % 10;
        }
        result[0] = carry;
        return result;
    }
}
