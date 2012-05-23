package Barramundi;
import java.util.Random;

/**
	Each Seen class must have a seen function.
	
   	@author Andrew Turpin (aturpin@discoveriesinsight.org)
	@version 0.4 Jul 1999
*/
public abstract class Seen {
	double falsePositiveRate;
	double falseNegativeRate;

	private static Random rand = new Random(); // used for all random numbers
	
	Seen(double falsePositiveRate, double falseNegativeRate) {
		this.falsePositiveRate = falsePositiveRate;
		this.falseNegativeRate = falseNegativeRate;
	}
	
    /** Randomly choose to check FP or FN, then check em!
    ** @returns  1 if false positive
    ** @returns  0 if no false response
    ** @returns -1 if false negative
    */
    public int checkFalseResponse() {
        double r = rand.nextDouble();
        double t = falseNegativeRate + falsePositiveRate;
        if (r < t)
			if (r < falsePositiveRate ) 
                return +1;			
            else 
			    return -1;
        return 0;
    }
    
	/**
		seen() must be overwritten by sub-classes of Seen.
		
		@param stimVal          The value of the stimulus presented (in dB).
		@param threshold        The value of the patient's thresholds (in dB).
		@param numPresentations The total number of presentations of all stimuli so far.
                @param x                X coordinate.
                @param y                Y coordinate.
	*/
	public abstract boolean seen(int stimulusValue, double threshold, int numPresentations, int x, int y);
}
