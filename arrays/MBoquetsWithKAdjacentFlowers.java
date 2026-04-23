
class MBoquetsWithKAdjacentFlowers{
    public int minDays(int[] bloomDays, int K, int M){
        if(M*K>bloomDays.length) return -1;
        int left= bloomDays[0], right = bloomDays[0];
        for(int bloomDay: bloomDays){
            left = Math.min(left, bloomDay);
            right = Math.max(right, bloomDay);
        }
        if(!isvalid(bloomDays, K, M, right)) return -1;
        int answer = right;
        while(left<=right){
            int mid = left + (right-left)/2;
            if(isvalid(bloomDays, K, M, mid)){
                answer = mid;
                right = mid-1;
            } else{
                left = mid+1;
            }
        }
        return answer;
    }

    public boolean isvalid(int[] bloomDays, int K,int M,  int day){
        int countOfAdj = 0, boquetCOunt = 0;
        for(int bloomDay: bloomDays){
            if(bloomDay<=day){
                countOfAdj++;
                if(countOfAdj==K){
                    boquetCOunt++;
                    if(boquetCOunt == M) return true;
                    countOfAdj=0;
                } 
                   
            }
            else countOfAdj = 0;
        }
        return false;
    }
}