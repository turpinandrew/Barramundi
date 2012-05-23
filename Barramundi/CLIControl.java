package Barramundi;

import java.util.Vector;
import java.io.File;

/**
   Control class for the command line interface version of Barramundi.

   Modified by Alex Pooley 10/5/04.


	This class contains the stimulus, patients, and test 
	procedures to run, and the methods to effect the 
	simmulation.
    Note that this is a subclass of thread, so an instance 
    should be initliased with the loadSimulator method, and 
    then the start() method should be called, not the run() 
    method directly.
	
	@author Andrew Turpin (aturpin@discoveriesinsight.org)
	@version 0.1 April 1999
*/
class CLIControl extends Thread {
	public boolean debugMode;				   // set in Simulator()
	public boolean summaryOnly;				   // set in Simulator()
	
	private StimulusRecord        stimulus;
	private Patient               patient[][];   // the patients in sets
	private AbstractTestProcedure testProcedure[];
	private File				  outputFile;
	private File				  logFile;
	
	/** Results for each procedure, patient set, and patient */ 
	private PatientResults results;

	/**
		Load the array of the Patient records, and testProcedures.  
		
		@param patient 2D array of patient arrays- 1st dim is set, 2nd dim is patients.
		@param testProcedures Array of test procedures to run.
		@param stimulus The physical characteristics of the stimulus.
		@param analyser The object to perform analysis.
	*/	
	public void loadSimulator(Patient patient[][], AbstractTestProcedure testProcedure[], 
							  StimulusRecord stimulus, File outputFile, File logFile) {
		this.stimulus      = stimulus;
		this.patient       = patient;
		this.testProcedure = testProcedure;
		this.outputFile    = outputFile;
		this.logFile       = logFile;
	}

	/**
		Check to see if any input thresholds are outside the limits of the stimulus.
		
		Apply all test procedures to all patients, maintaining progress bars, 
		and storing the results in the output file.
	*/
	public void run() {
			// error checking
		if ((testProcedure == null) || (testProcedure.length == 0) ) {
			System.err.println("You did not specify any test procedures.");
			return;
		}
			
		if ((patient == null) || (patient.length == 0)) {
			System.err.println("You did not specify any patient sets.");
			return;
		}
			
		for(int i = 0 ; i < patient.length ; i++) {
			if (patient[i].length == 0) {
				System.err.println("Patient set "+i+" does not contain any patients.");
				return;
			}
		}
		
		for (int i = 0 ; i < patient.length ; i++)
			for (int j = 0 ; j < patient[i].length ; j++) {
				if (patient[i][j].threshold.length != stimulus.numLocations)
					System.err.print("Warning: Patient set "+i+" has "+patient[i][j].threshold.length+" thresholds, which is not the required "+stimulus.numLocations+" input thresholds.\n");
				if (!patient[i][j].thresholdsOK(stimulus)) {
					System.err.print("Patient set "+i+", patient "+j+" has a threshold of that is out of range.\n");
				}
			}
		
			// set up results recording object
		String testProcTitles[]   = new String[testProcedure.length];
		String patientSetTitles[] = new String[patient.length];
		for(int i = 0 ; i < testProcedure.length ; i++)
			testProcTitles[i] = testProcedure[i].name;
		for(int i = 0 ; i < patientSetTitles.length ; i++)
			patientSetTitles[i] = patient[i][0].set;
			
		results = new PatientResults(testProcTitles, patientSetTitles, stimulus, outputFile);

			// Now do the simulation runs
		
		for(int i = 0 ; i < testProcedure.length ; i++) {
		    System.out.println("Processing test procedure "+i);

			final AbstractTestProcedure tp = testProcedure[i];
			
			tp.setup(stimulus, debugMode, logFile);
			
			for(int j = 0 ; j < patient.length ; j++) {

				for(int k = 0 ; k < patient[j].length ; k++) {

					tp.debugWrite("Test Procedure "+i+" patient set "+j+" patient "+k+"\n");

					tp.run(patient[j][k]);
					results.write(i, j, k, tp.stats, summaryOnly);
					yield();
				}// end all patients in set j
				
				results.memorySavingPercentilesCalc(i,j);  // calc percentiles here so memory can be freed
				
			}// end all patient sets
			
			tp.finish();
			
			testProcedure[i] = null; // help out the garbage collector
		}// end for all test procedures
		
		results.writeSummary();
		results.close();
	}// end run()
}// end Control class
