package mergesort;

public class merge {
    private int c;

    public static int[] mergeArray(int[] arr1, int[] arr2){
        int[] result =  new int[arr1.length + arr2.length];
        int index1 = 0;
        int i = 0;
        int index2 = 0;

        while(index1 < arr1.length && index2 < arr2.length){
            if(arr1[index1] <= arr2[index2]){
                result[i] = arr1[index1];
                index1 += 1;
            }else if(arr1[index1] > arr2[index2]){
                result[i] = arr2[index2];
                index2 += 1;
            }
            i += 1;
        }

        int symbol = (index1 - arr1.length) - (index2 - arr2.length);
        if(symbol < 0) {
            for(; index1 < arr1.length; index1 += 1){
                result[i] = arr1[index1];
                i += 1;
            }
        }else if(symbol > 0){
            for(; index2 < arr2.length; index2 += 1){
                result[i] = arr2[index2];
                i += 1;
            }
        }
        return result;
    }

    public void count(int N){
        if(N <= 1){
            return;
        }
        count(N / 2);
        count(N / 2);
        c += 1;
    }

    public void printC(int N){
        System.out.print("it calls count for " + c +" times when N equals to " + N);
    }

}
