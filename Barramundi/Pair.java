package Barramundi;

/**
	Class to hold a pair of values (used for rank-order stats).
*/
public class Pair implements SortableObject {
	double key; 
	int count;
	
	Pair() { }
	Pair(double x, int y) {key = x ; count = y;}
	
	public int compare(Object a, Object b) {
		Pair aa = (Pair)a;
		Pair bb = (Pair)b;
		if (aa.key < bb.key) return -1;
		if (aa.key == bb.key) return 0;
		return +1;
	}
} 