package Barramundi;

/**
	The patient data stored in an instance of this class is used for the actual 
	simulation run.
	
	@author Andrew Turpin (aturpin@discoveriesinsight.org)
	@version 0.1 April 1999

*/
class Patient {
	String name;
	String set;
	int age;
	double threshold[];
	Seen seenObject;
	
	Patient(String n, String s, int a, Seen seenObj) {
		name = n; set = s; age = a; seenObject = seenObj;
	}
	
	/**
		Return true if all thresholds fall in an acceptable stimulus range.
		
		@param s The stimulus record describing the stimulus against which thresholds are checked.
	*/
	boolean thresholdsOK(StimulusRecord s) {
		boolean ok = true;
		
		for(int i = 0 ; i < threshold.length ; i++) {
			if (threshold[i] < s.minExcitation) ok = false;
			if (threshold[i] > s.maxExcitation) ok = false;
		}
		
		return ok;
	}

	public void setSeen(Seen so)
	{
		this.seenObject = so;
	}
}