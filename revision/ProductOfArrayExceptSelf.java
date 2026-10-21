class ProductOfArrayExceptSelf{
    public static void main(String args[]){
        int a[] = new int[10];
        int res[] = new int[10];
        int mulSoFar=1, i, n = a.length;
        // Set element at 0 index as 1
        res[0]  = 1;
        
        // From 1 index keep a left mul if at index 1 it should give zero and so on
        for(i = 1; i<n;i++){
            mulSoFar*=a[i-1];
            res[i] = mulSoFar;
        }
        // assign mulSoFar
        mulSoFar = 1;

        // from right onwards start at n-2 and go till 0 this should give the answer
        for(i = n-2; i>=0;i--){
            mulSoFar*=a[i+1];
            res[i] = mulSoFar;
        }        
        //Print result array
        for(int num:res){
            System.out.println(" "+num);
        }

    }
}