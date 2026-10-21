import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordBreak {
    public boolean wordBreak(String s, List<String> wordDict) {
    // Your implementation
    Set<String> dictSet = new HashSet<>();
    int maxLength = Integer.MIN_VALUE;
    for(String word: wordDict){
        dictSet.add(word);
        maxLength = Math.max(maxLength, word.length());
    }
    Boolean[] dp = new Boolean[s.length()+1];
    dp[0]=1;
    return canBreak(s,  0,dp,dictSet);
}

public boolean canBreak(String s, int startIndex,Boolean[] dp,Set<String> dictSet)
{
    if(startIndex == s.length()) return true;
    if(dp[startIndex] != null) return dp[start];
    
    for(int index = startIndex; index< s.length();index++){
        String word = s.substring(startIndex, index);
        if(dictSet.contains(word) && canBreak(s, index+1, dp, dictSet)){
            dp[startIndex] = true;
            return true;
        }
    }

    dp[startIndex] = false;
    return false;
    
}


public boolean canBreakv1(String s, int startIndex,Boolean[] dp,Set<String> dictSet)
{
    if(startIndex == -1) return true;
    if(dp[startIndex] != null) return dp[startIndex];
    
    for(int index = startIndex; index>=0;index--){
        String word = s.substring(index, startIndex+1);
        if(dictSet.contains(word) && canBreakv1(s, index-1, dp, dictSet)){
            dp[startIndex] = true;
            return true;
        }
    }

    dp[startIndex] = false;
    return false;
    
}
}
