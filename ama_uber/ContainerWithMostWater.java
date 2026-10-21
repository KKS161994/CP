
class ContainerWithMostWater{
    public int maxWater(int[] containers){
        int i = 0, j = 1,area = 0;
        int n = containers.length;
        for(i = 0; i< n-1;i++){
            for(j = i+1; j< n;j++){
                area = Math.max(
                    area,
                    Math.min(containers[i],containers[j])*(j-i)
                );
            }
        }
        return area;

    }
}