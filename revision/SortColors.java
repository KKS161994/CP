import java.util.HashMap;
import java.util.Map;
class SortColors{
    public static void main(String[] args){
        int nums[] = {0,1,2,1,2,0};
        int[] buckets = new int[nums.length+1];
        Map<Integer,Integer> freqMap = new HashMap<>();
        int count = 0;
        for(int num:nums){
            freqMap.put(num,freqMap.getOrDefault(num,0)+1);
        }
        for(Map.Entry<Integer,Integer> entry : freqMap.entrySet()){
            int key = entry.getKey();
            int value = entry.getValue();
            buckets[key]= value;
        }
        for(int freq=0;freq<buckets.length&& count < nums.length;freq++){
            //Guard
            if(buckets[freq]==0) continue;
            int tempCount = 0;
            while(tempCount<buckets[freq]){
                nums[count++]=freq;
                tempCount++;
            }
        }   

    }
}