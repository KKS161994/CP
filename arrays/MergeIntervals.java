import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeIntervals {
    public int[][] merge(int[][] intervals) {
        int n = intervals.length;

        int[] openInterval = new int[n];
        int[] closeInterval = new int[n];
        int index = 0;
        List<int[]> result = new ArrayList<>();
        for (int[] interval : intervals) {
            openInterval[index] = interval[0];
            closeInterval[index] = interval[1];
            index++;
        }
        Arrays.sort(openInterval);
        Arrays.sort(closeInterval);

        int openIndex = 0, closeIndex = 0, lastOpen = openIndex;

        while (closeIndex < n) {
            if (openIndex < n && openInterval[openIndex] <= closeInterval[closeIndex]) {
                openIndex++;
            } else {
                closeIndex++;

                if (openIndex == closeIndex) {
                    result.add(new int[] { openInterval[lastOpen], closeInterval[closeIndex] });
                    lastOpen = openIndex;
                }

            }
        }
        return result.toArray(new int[0][]);
    }
}
