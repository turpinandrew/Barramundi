package Barramundi;

/**
	A skeleton test procedure.
	
   	@author Andrew Turpin (aturpin@discoveriesinsight.org)
	@version 0.3 June 1999
*/
class TPSkeleton extends AbstractTestProcedure {
	/** Put any variables local to the procedure here */ 

	/**
		The constructor should take the same number of arguments as the database entry has parameters.
		Parameters can only be of type String or Double.
		
		@param param1 The ...
		@param param2 The ...
	*/
	TPSkeleton(String param1, Double param2) {
			/* This call sets the name of the procedure that will be printed in the 
			   results file (the first arg).
			 */
		super("Skeleton "+param1+" "+param2.toString());
		
			/* put any other code here to intialise your procedure (eg read data from a file) */
	}
	
	/** 
		Perform any test procedure specific initialisations here.
	*/
	public void localSetup() { 
	}// localSetup()
	
	/** 
		Show each stimulus, recording response by calling the seen() function.
		Record information in "stats" for this patient.
	*/
	public void run(Patient p) {
		/** value of stimulus to display each iteration  */ int stimValue;
		/** the total number of presentations            */ int numPresentations = 0;
		
			/* set the inputThresholds for this patient */
		stats.inputThreshold = (double [])p.threshold.clone();

		/*
			This loop "displays" stimuli in order, you might want to more closely
			simulate a different presentation order, especially if your p.seen() function
			depends on number of presentations (eg fatigue).
		*/
		for(int i = 0 ; i < stimulus.numLocations ; i++) {
			stats.numPresentation[i] = 0;

			debugWrite("Some information you want to see");
			
			do {
				stimValue = 0; /* choose a value for your stimulus */
				
				stats.numPresentation[i]++;
				numPresentations++;

				if (p.seenObject.seen(stimValue, p.threshold[i], numPresentations)){
					/* do something here to change stimValue for the next iteration */
				} else {
					/* do something here to change stimValue for the next iteration */
				}
				
			} while (false); /* replace false with stopping condition */
			
			stats.measuredThreshold[i] = 0; /* set the value of the threshold you measured */
		}
	}// end run()
	
	public void localFinish() {
		/* any tidying up you want to do */
		/* eg set any array variables to null so the garbage collector can get em easily */
	}
	
	/* put any function definitions you want to use here */
	
}// end TPSkeleton class