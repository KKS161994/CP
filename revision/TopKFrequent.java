import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class TopKFrequent {
    public static void main(String[] args){
        int nums[] = {1,2,3,4,5,6,6,7,8},k=3;
        Map<Integer, Integer> freqMap = new HashMap<>();
        for(int num: nums){
            freqMap.put(num, freqMap.getOrDefault(num,0)+1);
        }

        List<Integer>[] buckets = new List[nums.length+1];
        List<Integer> result = new ArrayList<>();
        for(Map.Entry<Integer,Integer> entry : freqMap.entrySet()){
            int key = entry.getKey();
            int value = entry.getValue();

            if(buckets[value] == null){
                buckets[value] = new ArrayList<>();
            }
            buckets[value].add(key);
        }


        for(int frequency = buckets.length-1;
            result.size()<k&&frequency>=0;
            frequency--){
            if(buckets[frequency] == null) continue;
            for(int num: buckets[frequency]){
                result.add(num);
                if(result.size()>=k){
                    break;
                }
            }
        }

        for(int num:result){
            System.out.println(num+" ");
        }
    }
}
