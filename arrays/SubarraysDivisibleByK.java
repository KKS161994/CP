import java.util.*;

class SubarraysDivisibleByK{
    public int elementsCOuntDivisibleByK(int[] nums,int k){
        Map<Integer,Integer> modulosCount = new HashMap<>();
        int sum = 0,count = 0;
        modulosCount.put(0,1);
        for(int num: nums){
            sum+=num;
            int modulo = sum % k;
            if(modulo<0) modulo = k+modulo;
            if(modulosCount.containsKey(modulo)) 
                count+=modulosCount.get(modulo);
            modulosCount.putIfAbsent(modulo, 0);
            modulosCount.put(modulo,modulosCount.get(modulo)+1);

        }
        return count;
    }

public int countDivisibleByK(int[] nums, int k) {
    Map<Integer, Integer> modCount = new HashMap<>();
    modCount.put(0, 1);
    int sum = 0, count = 0;
    for (int num : nums) {
        sum += num;
        int mod = ((sum % k) + k) % k;
        count += modCount.getOrDefault(mod, 0);
        modCount.merge(mod, 1, Integer::sum);
    }
    return count;
}
}