
class MWS{
    public static void main(String args[]){
        int[] counts = new int[26];
        String secret = "hello";
        String s = "hello how are you";
        int left = 0,right=0;
        for(char c: secret.toCharArray()){
            // Ascii
            counts[c-'a']++;
        }   
        int n = secret.length();
        int[] tempCount = new int[26];
        int minLength = Integer.MAX_VALUE;
        int temp = 0;
        while(right<s.length()){
            char c = s.charAt(right);
            if(counts[c-'a']>0 ){
                
                if(tempCount[c-'a']<counts[c-'a'])
                temp++;
                tempCount[c-'a']++;
            }

            
            while(temp==n){
                minLength = Math.min(minLength,right-left+1);
                c = s.charAt(left);
                if(counts[c-'a']>0 ){

                    if(counts[c-'a']==tempCount[c-'a']){
                     temp--;
                    }
                    tempCount[c-'a']--;
                }
                left++;
            }
            right++;
        }
    }   
}