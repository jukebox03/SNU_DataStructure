import java.io.*;
import java.util.*;

public class SortingTest
{
    public static void main(String args[])
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try
        {
            boolean isRandom = false;
            int[] value;
            String nums = br.readLine();
            if (nums.charAt(0) == 'r')
            {
                isRandom = true;
                String[] nums_arg = nums.split(" ");

                int numsize = Integer.parseInt(nums_arg[1]);
                int rminimum = Integer.parseInt(nums_arg[2]);
                int rmaximum = Integer.parseInt(nums_arg[3]);

                Random rand = new Random();

                value = new int[numsize];
                for (int i = 0; i < value.length; i++)
                    value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
            }
            else
            {
                int numsize = Integer.parseInt(nums);

                value = new int[numsize];
                for (int i = 0; i < value.length; i++)
                    value[i] = Integer.parseInt(br.readLine());
            }
            while (true)
            {
                int[] newvalue = (int[])value.clone();
                char algo = ' ';

                if (args.length == 4) {
                    return;
                }

                String command = args.length > 0 ? args[0] : br.readLine();

                if (args.length > 0) {
                    args = new String[4];
                }

                long t = System.currentTimeMillis();
                switch (command.charAt(0))
                {
                    case 'B':	// Bubble Sort
                        newvalue = DoBubbleSort(newvalue);
                        break;
                    case 'I':	// Insertion Sort
                        newvalue = DoInsertionSort(newvalue);
                        break;
                    case 'H':	// Heap Sort
                        newvalue = DoHeapSort(newvalue);
                        break;
                    case 'M':	// Merge Sort
                        newvalue = DoMergeSort(newvalue);
                        break;
                    case 'Q':	// Quick Sort
                        newvalue = DoQuickSort(newvalue);
                        break;
                    case 'R':	// Radix Sort
                        newvalue = DoRadixSort(newvalue);
                        break;
                    case 'S':	// Search
                        algo = DoSearch(newvalue);
                        break;
                    case 'X':
                        return;
                    default:
                        throw new IOException("Wrong Sort");
                }
                if (isRandom)
                {
                    System.out.println((System.currentTimeMillis() - t) + " ms");
                }
                else
                {
                    if (command.charAt(0) != 'S') {
                        for (int i = 0; i < newvalue.length; i++) {
                            System.out.println(newvalue[i]);
                        }
                    } else {
                        System.out.println(algo);
                    }
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Wrong Input. Error : " + e.toString());
        }
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
        int k_1= 3;
        float k_2_1= (float) 0.4;
        float k_2_2= (float) 0.75;
        float k_3_1= (float) 0.5;
        float k_3_2= (float) 0.9;
        int k_4=2500;
        int min = getMin(value);
        if(min<0){
            for (int i=0; i<value.length; i++){
                value[i]=value[i]-min;
            }
        }
        int maxLength = (int)(Math.log10(getMax(value))+1);
        int collisionCount = 0;
        Map<Object, Integer> hashtable = new HashMap<>();
        for (Object key : value) {
            if (hashtable.containsKey(key)) {
                collisionCount++;
            } else {
                hashtable.put(key, 1);
            }
        }
        int setCount=0;
        for(int i=0; i<value.length-1; i++){
            if(value[i]<value[i+1])
                setCount++;
        }

        if(maxLength<=k_1)
            return('R');
        if(value.length<=k_4)
            return ('I');
        if((float)(setCount/(value.length-1))>=k_3_2)
            return ('I');
        if((float)(setCount/(value.length-1))<=k_3_1)
            return ('H');
        if((float)(collisionCount/value.length)<=k_2_1)
            return ('H');
        if((float)(collisionCount/value.length)<=k_2_2){
            if((float)(setCount/(value.length-1))<=0.6)
                return ('R');
            if((float)(setCount/(value.length-1))<=0.8)
                return ('Q');
            return ('R');
        }
        if((float)(setCount/(value.length-1))<=0.65)
            return ('H');
        else
            return ('M');
    }
}
