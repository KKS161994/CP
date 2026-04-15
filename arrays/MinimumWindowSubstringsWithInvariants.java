class MinimumWindowSubstringsWithInvariants{
    public String minimumWindow(String m, String n){
        int[] need = new int[128];
        int required;
        for(char c : n.toCharArray()){
            need[c]++;
        }

        required = n.length();
        
        int left = 0, right = 0, minLength = Integer.MAX_VALUE;
        int start = 0;
        while(right<m.length()){
            char r = m.charAt(right);
            if(need[r]>0){
                required--;
            }            
            need[r]--;
            while(required == 0){
                if(right-left < minLength){
                    start = left;
                    minLength = right-left;
                }
                char l = m.charAt(left);
                need[l]++;
                if(need[l]>0){
                    required++;
                }
                left++;
            }
        }
        return minLength == Integer.MAX_VALUE ? "": m.substring(start, start+minLength);
    }
}