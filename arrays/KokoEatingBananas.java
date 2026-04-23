// 875. Koko Eating Bananas
// Medium
// Topics
// conpanies icon
// Companies
// Koko loves to eat bananas. There are n piles of bananas, the ith pile has piles[i] bananas. The guards have gone and will come back in h hours.

// Koko can decide her bananas-per-hour eating speed of k. Each hour, she chooses some pile of bananas and eats k bananas from that pile. If the pile has less than k bananas, she eats all of them instead and will not eat any more bananas during this hour.

// Koko likes to eat slowly but still wants to finish eating all the bananas before the guards return.

// Return the minimum integer k such that she can eat all the bananas within h hours.


class KokoEatingBananas{
 public int minEatingSpeed(int[] piles, int H){
    if(piles.length>H) return -1;
    int left = 1, right = piles[0];
    for(int pile: piles){
        
        right = Math.max(right, pile);
    }
    int answer = right;
    while(left<=right){
        int mid = left+(right-left)/2;
        if(isValid(piles, H, mid)){
            answer = mid;
            right = mid-1;
        }
        else left = mid + 1;
    }
    return answer;
 }

 public boolean isValid(int[] piles, int H, int K){
    int hoursCOunt = 0;
    for(int pile: piles){
        hoursCOunt = hoursCOunt + pile/K + (pile%K>0?1:0);
        if(hoursCOunt>H)
            return false;
    }
    return true;
 }   
}