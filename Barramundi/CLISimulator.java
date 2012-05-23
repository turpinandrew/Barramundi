package Barramundi;

import java.util.Vector;
import java.io.*;

/**
   Command line interface to simulator.
   Read *.bar files up to first line with first char of line as '-'.
   You may also "tag" lines by inserting a '!' char as the first char of the line.
   If you don't tag lines, all lines before '-' will be used.

   May also select lines via command line by using flags multiple times.
   E.g.
   Usage: cliBarramundi -s 1 -s 2 -p 1 -p 3 -t 2 -t 4

   Modified by Alex Pooley 7/5/04.
   - epoch.

   @author Andrew Turpin (aturpin@discoveriesinsight.org)
   @version 0.3 May 1999
*/
class CLISimulator
{ 
    /** Tag character              */ static char TAG_CHAR = '!';
    /** stimuli database           */ static Stimuli        stimuli;
    /** patient set database       */ static PatientSets    patients;
    /** test procedure database    */ static TestProcedures testProcedures;
    /** simulator                  */ static final CLIControl  control = new CLIControl();

    /** file to receive results    */ static private File   outputFile;
    private static String defaultFilename = "results.out";
    /** file to write debug info   */ private static File   logFile;
    private static String defaultLogFilename = "barramundi.log";

    /**
       Create a new window for launching simulation runs
    */
    public static void main(String[] args)
    {
		Vector clStim = new Vector();
		Vector clPat = new Vector();
		Vector clTest = new Vector();

		//Process arguments.
		//Create a list of stimuli, patients, and test proc's to use
		// in the processing.
		if (args.length > 0)
		{
			int arg=0;
			for (; arg<args.length; arg++)
			{
				if (args[arg].compareTo("-s") == 0)
				{
					arg++;
					if (arg<args.length)
					{
					clStim.add(new Integer(args[arg]));
					}
				}
				if (args[arg].compareTo("-p") == 0)
				{
					arg++;
					if (arg<args.length)
					{
					clPat.add(new Integer(args[arg]));
					}
				}
				if (args[arg].compareTo("-t") == 0)
				{
					arg++;
					if (arg<args.length)
					{
						clTest.add(new Integer(args[arg]));
					}
				}
			}
		}

		// we only want summary data. otherwise the program is slower and will chew disk space.
		control.summaryOnly = false;
		//Debug info also.
		control.debugMode = false;

		// open the three databases
		try {
			stimuli         = new Stimuli("Stimuli.bar");
			patients        = new PatientSets("PatientSets.bar");
			testProcedures  = new TestProcedures("TestProcedures.bar");
		} catch (DatabaseException e) {
			System.err.println("Barramundi error: "+e.getMessage());
			return;
		}

		//Output file.
		outputFile = new File(defaultFilename);

		//Log file.
		logFile = new File(defaultLogFilename);

		//Select the indexes to use.

		//Stimuli.
		int[] stimuliList;
		if (clStim.isEmpty())
		{
			stimuliList = makeIndex("Stimuli.bar");
		}
		else
		{
			stimuliList = new int[clStim.size()];
			for (int i=0; i<clStim.size(); i++)
			{
				stimuliList[i] = ((Integer)(clStim.elementAt(i))).intValue();
			}
		}
		Vector selectedStimuli = stimuli.getSelected(stimuliList);
	/*
		System.out.println("Stimuli:");
		for (int i=0; i<stimuliList.length; i++)
		{
			System.out.println("\t"+stimuliList[i]);
		}
	*/
		StimulusRecord selectedStimulus = (StimulusRecord)selectedStimuli.elementAt(0);



		// extract the selected patient data from the list
		int[] patientSetList;
		if (clPat.isEmpty())
		{
			patientSetList = makeIndex("PatientSets.bar");
		}
		else
		{
			patientSetList = new int[clPat.size()];
			for (int i=0; i<clPat.size(); i++)
			{
				patientSetList[i] = ((Integer)(clPat.elementAt(i))).intValue();
			}
		}
		Vector selectedPatientSets = patients.getSelected(patientSetList);
		Patient patient[][] = new Patient[selectedPatientSets.size()][];
	//	System.out.println("Patients:");
		for(int i = 0 ; i < selectedPatientSets.size() ; i++)
		{
			try {
				patient[i] = ((PatientSetRecord)selectedPatientSets.elementAt(i)).generatePatients();
	//			System.out.println("\t"+selectedPatientSets.elementAt(i));
			} catch (PatientModifierException e) {
				System.err.println(e.getMessage());
			} catch(DatabaseException e) {
				System.err.println(e.getMessage());
			}
		}
		


		// extract the selected test procedure objects
		int[] testProceduresList;
		if (clTest.isEmpty())
		{
			testProceduresList = makeIndex("TestProcedures.bar");
		}
		else
		{
			testProceduresList = new int[clTest.size()];
			for (int i=0; i<clTest.size(); i++)
			{
				testProceduresList[i] = ((Integer)(clTest.elementAt(i))).intValue();
			}
		}
		Vector selectedTestProcedures = testProcedures.getSelected(testProceduresList);
		AbstractTestProcedure abstractTPs[] = new AbstractTestProcedure[selectedTestProcedures.size()];
	//	System.out.println("Test Procedures:");
		for(int i = 0 ; i < selectedTestProcedures.size() ; i++)
		{
			try {
				abstractTPs[i] = ((TestProcedureRecord)selectedTestProcedures.elementAt(i)).getAbstractTestProcedure();
	//			System.out.println("\t"+selectedTestProcedures.elementAt(i));
			} catch(TestProcedureException e) {
				System.err.println(e.getMessage());
			}
		}
						
		// give the garbage collector a helping hand.
		selectedStimuli = selectedPatientSets = selectedTestProcedures = null;
					
		// now go for it...
		System.out.println("Loading simulator..");
		control.loadSimulator(patient, abstractTPs, selectedStimulus, outputFile, logFile);
		System.out.println("Starting simulation. Coffee break.");
		control.start();
    }

    //Read file up to first comment.
    //Create array from 0 to < num_lines_read.
    public static int[] makeIndex(String filename)
    {
		BufferedReader input;
		String line;
		int count=0;
		Vector tags = new Vector();

		try {
			input = new BufferedReader(new FileReader(filename));
			//Count the lines.
			line = input.readLine();
			while ( (line != null) &&
				(line.charAt(0) != Database.COMMENT_CHAR) )
			{
				//Is this line tagged?
				if (line.charAt(0) == TAG_CHAR)
				{
					tags.add(new Integer(count));
				}
				count++;
				line = input.readLine();
			}
			input.close();
		
		} catch(java.io.FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch(java.io.IOException e) {
			System.err.println(e.getMessage());
		}

		//Create the array.
		int[] index;
		//If we found tags, then just copy them into an array.
		if (tags.size() > 0)
		{
			index = new int[tags.size()];
			for (int i=0; tags.size()<i; i++)
			{
			index[i] = ((Integer)(tags.elementAt(i))).intValue();
			}
		}
		//If we didn't find any tags, just create an array where each element
			//in the array is the same as the location of the element.
		else
		{
			index = new int[count];
			for (int i=0; i<count; i++)
			{
				index[i] = i;
			}
		}
		return index;
    }
}
