// *
// **
// ***
// ****
// *****

class TestPattern
{
    public static void main(String args[])
    {
        int i, j;
        for(i=1; i<=5; i++)  // row 
        {
            for(j=5; j>=i; j--)   // column
            {
                System.out.print("*");
                
                
                // j = 1 to j = 6-i
                
                // *****1 row 5(6-1) star 
                // **** 2 row 4(6-2) star
                // ***  3 row 3(6-3) star
                // **   4 row 2 star
                // *    5 row 1 star
            }
            System.out.println();
        }
    }
}