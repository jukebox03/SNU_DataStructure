import java.io.*;
import java.util.*;

public class SortingTestCheck_Radix
{
    public static void main(String args[]) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            int[] count = new int[3];
            count[0]=0;
            count[1]=0;
            count[2]=0;
            for (int i_1 = 10; i_1 < 1500; i_1 += 10) {


                Random random = new Random();

                int[] value = new int[10000];
                for (int i = 0; i < 10000; i++) {
                    value[i]= random.nextInt(i_1);
                }
                //System.out.println(i_1);
                //printInfo(value);

                long[] time = new long[6];

                int[] newvalue = (int[]) value.clone();
                long t = System.currentTimeMillis();
                newvalue = DoBubbleSort(newvalue);
                time[0] = (System.currentTimeMillis() - t);
                //System.out.println(time[0] + " ms");

                newvalue = (int[]) value.clone();
                t = System.currentTimeMillis();
                newvalue = DoInsertionSort(newvalue);
                time[1] = (System.currentTimeMillis() - t);
                //System.out.println(time[1] + " ms");

                newvalue = (int[]) value.clone();
                t = System.currentTimeMillis();
                newvalue = DoHeapSort(newvalue);
                time[2] = (System.currentTimeMillis() - t);
                //System.out.println(time[2] + " ms");

                newvalue = (int[]) value.clone();
                t = System.currentTimeMillis();
                newvalue = DoMergeSort(newvalue);
                time[3] = (System.currentTimeMillis() - t);
                //System.out.println(time[3] + " ms");

                newvalue = (int[]) value.clone();
                t = System.currentTimeMillis();
                newvalue = DoQuickSort(newvalue);
                time[4] = (System.currentTimeMillis() - t);
                //System.out.println(time[4] + " ms");

                newvalue = (int[]) value.clone();
                t = System.currentTimeMillis();
                newvalue = DoRadixSort(newvalue);
                time[5] = (System.currentTimeMillis() - t);
                //System.out.println(time[5] + " ms");

                int max_loc = 0;
                for (int i = 0; i < 6; i++) {
                    if (time[i] < time[max_loc])
                        max_loc = i;
                }
                switch (max_loc) {
                    case 0:
                        System.out.println("B");
                        break;
                    case 1:
                        System.out.println("I");
                        break;
                    case 2:
                        System.out.println("H");
                        break;
                    case 3:
                        System.out.println("M");
                        break;
                    case 4:
                        System.out.println("Q");
                        break;
                    case 5:
                        System.out.println("R");
                        System.out.println((int)(Math.log10(getMax(value))-1));
                        count[(int)(Math.log10(getMax(value))-1)]++;
                        break;
                }
            }
            System.out.println((float)count[0]/(float)10);
            System.out.println((float)count[1]/(float)100);
            System.out.println((float)count[2]/(float)500);
            return;
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private static void printInfo(int[] value){
        int min = getMin(value);
        if(min<0){
            for (int i=0; i<value.length; i++){
                value[i]=value[i]-min;
            }
        }
        int maxLength = (int)(Math.log10(getMax(value))+1);
        System.out.printf("%s%d%s", "maxLength:", maxLength, "\n");

        int collisionCount = 0;
        Map<Object, Integer> hashtable = new HashMap<>();
        for (Object key : value) {
            if (hashtable.containsKey(key)) {
                collisionCount++;
            } else {
                hashtable.put(key, 1);
            }
        }
        System.out.printf("%s%f%s", "collision: ", (float)collisionCount/(float) value.length, "\n");

        int setCount=0;
        for(int i=0; i<value.length-1; i++){
            if(value[i]<=value[i+1])
                setCount++;
        }
        System.out.printf("%s%f%s", "set: ", (float)setCount/(float)(value.length-1), "\n");
        if(min<0) {
            for (int i = 0; i < value.length; i++) {
                value[i] = value[i] + min;
            }
        }
    }

    private static float calSet(int[] value){
        int min = getMin(value);
        if(min<0){
            for (int i=0; i<value.length; i++){
                value[i]=value[i]-min;
            }
        }
        int setCount=0;
        for(int i=0; i<value.length-1; i++){
            if(value[i]<=value[i+1])
                setCount++;
        }
        if(min<0) {
            for (int i = 0; i < value.length; i++) {
                value[i] = value[i] + min;
            }
        }
        return (float)setCount/(float)(value.length-1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoBubbleSort(int[] value)
    {
        int n = value.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (value[j] > value[j + 1]) {
                    int temp = value[j];
                    value[j] = value[j + 1];
                    value[j + 1] = temp;
                }
            }
        }
        return (value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoInsertionSort(int[] value)
    {
        int n = value.length;
        for (int i = 1; i < n; i++) {
            int key = value[i];
            int j = i - 1;
            while (j >= 0 && value[j] > key) {
                value[j + 1] = value[j];
                j = j - 1;
            }
            value[j + 1] = key;
        }
        return (value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoHeapSort(int[] value)
    {
        int n = value.length;
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(value, n, i);
        for (int i = n - 1; i >= 0; i--) {
            int temp = value[0];
            value[0] = value[i];
            value[i] = temp;

            heapify(value, i, 0);
        }
        return (value);
    }

    private static void heapify(int[] value, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        if (left < n && value[left] > value[largest])
            largest = left;
        if (right < n && value[right] > value[largest])
            largest = right;
        if (largest != i) {
            int temp = value[i];
            value[i] = value[largest];
            value[largest] = temp;
            heapify(value, n, largest);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoMergeSort(int[] value)
    {
        mergeSort(value, 0, value.length-1);
        return (value);
    }

    private static void mergeSort(int[] value, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(value, left, middle);
            mergeSort(value, middle + 1, right);
            merge(value, left, middle, right);
        }
    }

    private static void merge(int[] value, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; i++)
            L[i] = value[left + i];
        for (int j = 0; j < n2; j++)
            R[j] = value[middle + 1 + j];

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                value[k] = L[i];
                i++;
            } else {
                value[k] = R[j];
                j++;
            }
            k++;
        }
        while (i < n1) {
            value[k] = L[i];
            i++;
            k++;
        }
        while (j < n2) {
            value[k] = R[j];
            j++;
            k++;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoQuickSort(int[] value)
    {
        quickSort(value, 0, value.length - 1);
        return (value);
    }

    private static void quickSort(int[] value, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(value, low, high);
            quickSort(value, low, pivotIndex - 1);
            quickSort(value, pivotIndex + 1, high);
        }
    }

    private static int partition(int[] value, int low, int high) {
        int pivot = value[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (value[j] <= pivot) {
                i++;
                int temp = value[i];
                value[i] = value[j];
                value[j] = temp;
            }
        }
        int temp = value[i + 1];
        value[i + 1] = value[high];
        value[high] = temp;
        return i + 1;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoRadixSort(int[] value)
    {
        int max = getMax(value);
        int min = getMin(value);
        if(min<0){
            for (int i=0; i<value.length; i++){
                value[i]=value[i]-min;
            }
            max=max-min;
        }
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSort(value, exp);
        }
        if(min<0){
            for (int i=0; i<value.length; i++){
                value[i]=value[i]+min;
            }
            max=max+min;
        }
        return (value);
    }

    private static void countingSort(int[] value, int exp) {
        int n = value.length;
        int[] output = new int[n];
        int[] count = new int[100];
        for (int i = 0; i < n; i++) {
            int digit = (value[i] / exp) % 10;
            count[digit]++;
        }
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }
        for (int i = n - 1; i >= 0; i--) {
            int digit = (value[i] / exp) % 10;
            output[count[digit] - 1] = value[i];
            count[digit]--;
        }
        for (int i = 0; i < n; i++) {
            value[i] = output[i];
        }
    }

    private static int getMax(int[] value) {
        int max = value[0];
        for (int i = 1; i < value.length; i++) {
            if (value[i] > max) {
                max = value[i];
            }
        }
        return max;
    }

    private static int getMin(int[] value) {
        int min = value[0];
        for (int i = 1; i < value.length; i++) {
            if (value[i] < min) {
                min = value[i];
            }
        }
        return min;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static char DoSearch(int[] value)
    {
        int k_1=1;
        float k_2= (float) 0.2;
        float k_3= (float) 0.3;
        int min = getMin(value);
        if(min<0){
            for (int i=0; i<value.length; i++){
                value[i]=value[i]-min;
            }
        }
        int maxLength = (int)(Math.log10(getMax(value))+1);
        if(maxLength<=k_1)
            return ('R');

        int collisionCount = 0;
        Map<Object, Integer> hashtable = new HashMap<>();
        for (Object key : value) {
            if (hashtable.containsKey(key)) {
                collisionCount++;
            } else {
                hashtable.put(key, 1);
            }
        }
        if((float)(collisionCount/value.length)>=k_2){
            return ('R');
        }

        int setCount=0;
        for(int i=0; i<value.length-1; i++){
            if(value[i]<value[i+1])
                setCount++;
        }
        if((float)(setCount/(value.length-1))>=k_3)
            return ('R');

        return ('Q');
    }
}
