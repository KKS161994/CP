
class NTaskFromMCpu {

    public int minTime(int M, int[] nTasks) {
        int sum = 0, min = nTasks[0];
        for (int task : nTasks) {
            min = Math.max(min, task);
            sum += task;
        }
        int left = min, right = sum, answer = sum;
        while(left<=right){
            int mid = left+(right-left)/2;
            if(isValid(M, nTasks, mid)){
                answer = mid;
                right = mid-1;
            }
            else{
                left = mid+1;
            }
        }
        return answer;

    }

    // public int binarySearchForTime(int M, int[] nTasks, int min, int max) {
    //     if (min > max) {
    //         return min;
    //     }

    //     int mid = min + (max - min) / 2;
    //     if (isValid(M, nTasks, mid)) {
    //         return binarySearchForTime(M, nTasks, min, mid - 1);
    //     } else {
    //         return binarySearchForTime(M, nTasks, mid + 1, max);
    //     }
    // }

    public boolean isValid(int M, int[] nTasks, int time) {
        int cpuCOunt = 1, occupiedTime = 0;
        for (int task : nTasks) {
            if (task > time) return false;
            if (occupiedTime + task <= time) {
                occupiedTime += task;
            } else {
                cpuCOunt++;
                if (cpuCOunt > M) {
                    return false;
                }
                occupiedTime = task;
            }
        }

        return true;
    }
}
