package Barramundi;

/* 
	Copied without permission from http://www.utdallas.edu/~pervin/quicksort.html 
	Changed from ints to Pairs
	
	Andrew Turpin
	23 June 1999
*/

/* Test of the famous "quicksort" routine
 * Originated by Tony Hoare - O(n log n) average case
 * We will choose pivot to be middle element
 * Not generic -- just for 100 integers 
 */

import java.util.*;


class QuickSort{    

	/** An object containing the compare function */ static SortableObject cmp;
	
	public static void quicksort(Object[] input, SortableObject compare)    {
		cmp = compare;
        quicksortAux(input, 0, input.length-1);
        return; 
    }
    
    private static void quicksortAux(Object[] input, int low, int high)    {
        if (low >= high) return; // Zero or one element only ... done
        int pivot = (low + high)/2;    // Use middle element as pivot
        
        pivot = partition(input,low,high,pivot);
        
        if (low < pivot) 
        	quicksortAux(input, low, pivot-1);
        if (pivot < high) 
        	quicksortAux(input, pivot+1, high);        
        	
       	return;
    }   
    
    private static int partition(Object[] input, int L, int H, int P)    {
        if (P != L) 
        	swap(input, P, L);        
        P = L;        
        L++;
        while(L <= H) {
        	if (cmp.compare(input[L], input[P]) < 1) 
        		L++;
        	else if(cmp.compare(input[H],input[P]) == 1)
                H--;
            else
                swap(input, L, H);
        }
        if (H != P) 
        	swap(input, P, H);        
        return H;
	}
        
    private static void swap(Object[] input, int low, int high)    {
        Object temp = input[low];        
        input[low] = input[high];
        input[high] = temp;
        return;    
    }
        
}