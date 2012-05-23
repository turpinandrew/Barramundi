package Barramundi;

import java.io.*;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.swing.JOptionPane;
import java.lang.reflect.Field;

import java.io.Serializable;

/**
   This class contains all the necessary information on a collection of patients
   and methods for writing that information to a file.

   @author Andrew Turpin (aturpin@discoveriesinsight.org)
   @version 0.4 June 1999
*/
class PatientResults {
    /** name of test procedure to write   */ private String testProcedure[];
    /** name of patients sets to write    */ private String patientSet[];
    /** the output stream                 */ private BufferedWriter out;
    /** the number of stimuli per patient */ private int numberOfStimuli;
										   
    private static final String startOfHeaderMarker  = "# START HEADER";
    private static final String endOfHeaderMarker    = "# END HEADER";
    private static final String startTestPMarker     = "# Test Procedure: ";
    private static final String startPatientSMarker  = "# \tPatient Set: ";
    private static final String startPatientMarker   = "# \t\tPatient: ";
    private static final String startOfNumStimMarker = "# Number of Stimuli";
    private static final String startOfSummaryMarker = "# START SUMMARY";
    private static final String endOfSummaryMarker   = "# END SUMMARY";
    private static final String titleWrapper         = "************************************************\n";

    /**
       Class to hold summary statistics for data.
    */
    class Summary implements Serializable {	
	/** maintained by calls to record() */ public double sumAbs, sum, max, min, num, sumsq, sumsqSq;
	/** calculated by a call to calc()  */ public double mean, meanAbs, meanSq,
	    sd,   sdAbs,   sdSq;
	/** calculated by a call to calc() */ Pair histogram[], histogramAbs[];
	/** store values for ranked stats  */ public Hashtable data, dataAbs;
	/** true if hash tables exist      */ boolean dataExists;

	/**
	   Constructor.
	*/
	Summary() { 
	    sumsqSq = sumsq = sum = num = 0; 
	    max = Double.MIN_VALUE; 
	    min = Double.MAX_VALUE; 
	    data    = new Hashtable();
	    dataAbs = new Hashtable();
	    dataExists = true;
	}

	/**
	   Add observation x (difference between measured and input) into the stats.
	*/		
	void record(double x) {
	    if (x > max) max = x;
	    if (x < min) min = x;
	    num++;
	    sum     += x;
	    sumsq   += x*x;
	    sumsqSq += x*x*x*x;
	    sumAbs  += Math.abs(x);
			
	    if (dataExists) {
		updateHashtable(data, new Double(x));
		updateHashtable(dataAbs, new Double(Math.abs(x)));
	    }
	}
		
	/**
	   If the key is already in the hash table increment its frequency count by one, 
	   else insert it into the table with a count of 1.
			
	   @param ht The hashtable to update.
	   @param key The key to update in ht.
	*/
	void updateHashtable(Hashtable ht, Double key) {
	    if (ht.containsKey(key)) {
		Integer count = (Integer)ht.get(key);
		ht.remove(key);
		ht.put(key, new Integer(count.intValue()+1));
	    } else {
		ht.put(key, new Integer(1));
	    }
	}
		
	/**
	   Calculate the derived stats.
	*/
	void calc() {
	    mean    = sum/num;
	    meanAbs = sumAbs/num;
            meanSq  = sumsq/num;
	    sd      = Math.sqrt((sumsq   - 2*mean*sum    + num*mean*mean)    /(num-1));
	    sdAbs   = Math.sqrt((sumsq   - 2*mean*sumAbs + num*mean*mean)    /(num-1));
            sdSq    = Math.sqrt((sumsqSq - 2*mean*sumsq  + num*meanSq*meanSq)/(num-1));

	    if (dataExists) {
		histogram    = createHistogram(data);
		histogramAbs = createHistogram(dataAbs);
		data = null;
		dataAbs = null;
		dataExists = false;
	    }
	}// calc()

	/**
	   Put all of the hash table values in a Pair array and then sort them by key.
	*/		
	Pair[] createHistogram(Hashtable data) {
	    Pair histogram[] = new Pair[data.size()];
			
	    Enumeration e = data.keys();
	    for (int i = 0; e.hasMoreElements() ; i++) {
		Double key = ((Double)e.nextElement());
		histogram[i] = new Pair(key.doubleValue(), ((Integer)data.get(key)).intValue());
	    }
			
	    QuickSort.quicksort(histogram, new Pair());
			
	    return histogram;
	}
    }//Summary class
	
    // Note that the final dimension is stimuli number, but the entry in position
    // numberOfStimuli is a summary over all stimuli.
    /** Summary of stats on differences.      */ Summary diffSummary[][][];
    /** Summary of stats on num presentations */ Summary numPresSummary[][][];
	
    /**	
       Constructor: set up variables and write a header to the file.
		
       @param numTestProcedures The number of test procedures for this object to hold.
       @param numPatientSets    The number of patient sets for this object to hold.
       @param out Output stream for results. 
    */
    PatientResults(String testProcedure[], String patientSet[], StimulusRecord stimulus, File outFile) {
	try {
	    out  = new BufferedWriter(new FileWriter(outFile));
	    this.patientSet    = patientSet;
	    this.testProcedure = testProcedure;
	    this.out           = out;
	    this.numberOfStimuli = stimulus.numLocations;
		
	    diffSummary    = new Summary[testProcedure.length][patientSet.length][numberOfStimuli+1];
	    numPresSummary = new Summary[testProcedure.length][patientSet.length][numberOfStimuli+1];
	  	
	    for(int i = 0 ; i < testProcedure.length ; i++)
		for(int j = 0 ; j < patientSet.length ; j++)
		    for(int k = 0 ; k <= numberOfStimuli ; k++) {
			diffSummary[i][j][k]    = new Summary();
			numPresSummary[i][j][k] = new Summary();
		    }
		
	    out.write(startOfHeaderMarker);
	    out.write("\n#\n");
	    out.write(startOfNumStimMarker);
	    out.write("\n# =================\n");
	    out.write("# " + numberOfStimuli + "\n");
        printPoints(out, stimulus);
	    out.write("\n#\n");

	    out.write(startTestPMarker);
	    out.write("\n# =======================\n");
	    for(int tp = 0 ; tp < testProcedure.length ; tp++)
		out.write("# " + (tp+1) + ") " + testProcedure[tp] + "\n");
	    out.write("#\n");
			
	    out.write(startPatientSMarker);
	    out.write("\n# ====================\n");
	    for(int ps = 0 ; ps < patientSet.length ; ps++)
		out.write("# " + (ps+1) + ") " + patientSet[ps] + "\n");
	    out.write("#\n");
		
	    out.write(endOfHeaderMarker);
	    out.write("\n");
	} catch (IOException e) { 
	    JOptionPane.showMessageDialog(null, "Cannot open file "+outFile.getName()+" for recording the results\n"+e,"Barramundi - Warning", JOptionPane.WARNING_MESSAGE); 
	}
    }
	
    /**
       Save the data to the out stream.  Each patient result forms a space delimited line, the 
       first 3	columns of which describe the test procedure, patient set, and patient number.
       Also accumulate the data in the "diff" arrays.
		
       @param tp Test Procedure number.
       @param ps Patient Set number.
       @param p  Patient number.
       @param stats The stats on the simulation run of this patient.
       @param summaryOnly True if only a summary is to be printed.
    */
    void write(int tp, int ps, int p, PatientResult stats, boolean summaryOnly) {
	int i=0;
	try {
	    if (!summaryOnly) 
			stats.write(out,(tp+1)+" "+(ps+1)+" "+(p+1)+" ");
	    // Accumulate the results in a summary form that can be printed.
	    for(i = 0 ; i < stats.inputThreshold.length ; i++) {
			diffSummary[tp][ps][i].record(stats.measuredThreshold[i] - stats.inputThreshold[i]);
			numPresSummary[tp][ps][i].record(stats.numPresentation[i]);
			diffSummary[tp][ps][numberOfStimuli].record(stats.measuredThreshold[i] - stats.inputThreshold[i]);
			numPresSummary[tp][ps][numberOfStimuli].record(stats.numPresentation[i]);
	    }
	} catch (IOException e) {
	    JOptionPane.showMessageDialog(null, "Trouble writing output to results file\n"+e,"Barramundi - Warning", JOptionPane.WARNING_MESSAGE);
	} catch (ArrayIndexOutOfBoundsException e) {
	    JOptionPane.showMessageDialog(null, "tp="+tp+" ps="+ps+" i="+i+"\n"+e,"Barramundi - Warning", JOptionPane.WARNING_MESSAGE);			
	} 
    }// write()
	
    /**
       Close the output stream.
    */
    void close() {
	try {
	    out.close();
	} catch (IOException e) {
	    JOptionPane.showMessageDialog(null, "Trouble closing results file\n"+e,"Barramundi - Warning", JOptionPane.WARNING_MESSAGE);
	}
    }// close()
	

    void writeSummary() {
	try {
	    out.write(startOfSummaryMarker);
	    out.write("\n");

	    for(int i = 0 ; i < testProcedure.length ; i++)
		for(int j = 0 ; j < patientSet.length ; j++) {
		    for(int k = 0 ; k <= numberOfStimuli ; k++) {
			diffSummary[i][j][k].calc();
			numPresSummary[i][j][k].calc();
		    }
		}
		
	    Class s = diffSummary[0][0][0].getClass();
		
	    printSummaryStat("Average absolute difference\n", diffSummary, s.getDeclaredField("meanAbs"));
	    printSummaryStat("Std dev absolute difference\n", diffSummary, s.getDeclaredField("sdAbs"));
	    printSummaryHistogram("Histogram of the absolute difference\n", diffSummary, s.getDeclaredField("histogramAbs"));

	    printSummaryStat("Mean number of presentations\n",    numPresSummary, s.getDeclaredField("mean"));
	    printSummaryStat("Std dev number of presentations\n", numPresSummary, s.getDeclaredField("sd"));
		
	    printSummaryStat("Average difference\n",          diffSummary,    s.getDeclaredField("mean"));
	    printSummaryStat("Std dev difference\n",          diffSummary,    s.getDeclaredField("sd"));
	    printSummaryHistogram("Histogram of difference\n", diffSummary, s.getDeclaredField("histogram"));
		
	    printSummaryStat("Mean difference squared\n",  diffSummary,    s.getDeclaredField("meanSq"));
	    printSummaryStat("Stdev difference squared\n",  diffSummary,    s.getDeclaredField("sdSq"));

	    printSummaryStat("Min difference\n",              diffSummary,    s.getDeclaredField("min"));
	    printSummaryStat("Max difference\n",              diffSummary,    s.getDeclaredField("max"));
	    printSummaryStat("Max number of presentations\n",     numPresSummary, s.getDeclaredField("max"));
	    printSummaryStat("Min number of presentations\n",     numPresSummary, s.getDeclaredField("min"));
		
		
	    out.write(endOfSummaryMarker);
	} catch (IOException e) {
	    JOptionPane.showMessageDialog(null, "Trouble writing summary information to file\n"+e,"Barramundi - Warning", JOptionPane.WARNING_MESSAGE);
	} catch (NoSuchFieldException e) {
	    JOptionPane.showMessageDialog(null, "No such field\n"+e,"Barramundi - Warning", JOptionPane.WARNING_MESSAGE);
	}
    }// writeSummary()
	
    /**
       Loop through the test procedures and patient sets writing the member of the summary.
		
       @param title   The title to print.
       @param summary The summary object to get the member from.
       @param field   The member of summary to write to file.
    */
    void printSummaryStat(String title, Summary summary[][][], Field field) {
	try {
	    out.write(titleWrapper);
	    out.write(title);
	    out.write(titleWrapper);
		
	    for(int i = 0 ; i < testProcedure.length ; i++) {
		out.write("\nTest Procedure: "+testProcedure[i]+"\n");

		for(int j = 0 ; j < patientSet.length ; j++) {
		    out.write("Patient Set: "+patientSet[j]+" ");
		    for(int k = 0 ; k <= numberOfStimuli ; k++)
			out.write(((Double)field.get(summary[i][j][k])).doubleValue()+" ");
		    out.write("\n");
		}
	    }
	} catch (IOException e) {
	    JOptionPane.showMessageDialog(null, "Trouble writing summary information to file\n"+e,"Barramundi - Warning", JOptionPane.WARNING_MESSAGE);
	} catch (IllegalAccessException e) {
	    JOptionPane.showMessageDialog(null, "Cannot access field "+field.getName()+"\n"+e,"Barramundi - Warning", JOptionPane.WARNING_MESSAGE);
	}
    }// printSummaryStat()
	
    /**
       Loop through the test procedures and patient sets writing the combined histogram 
       member of the summary.
		
       @param title   The title to print.
       @param summary The summary object to get the histogram member from.
       @param field   The histogram member of summary to write to file.
    */
    void printSummaryHistogram(String title, Summary summary[][][], Field field) {
	try {
	    // first work out the x-axis of the histogram by finding
	    // all occuring values over all histograms 
	    // (put in hash table, then into a sorted array)
	    Hashtable xvals = new Hashtable();
		
	    for(int i = 0 ; i < testProcedure.length ; i++) {
		for(int j = 0 ; j < patientSet.length ; j++) {
		    Pair histogram[] = (Pair [])field.get(summary[i][j][numberOfStimuli]);

		    for(int k = 0 ; k < histogram.length ; k++) {
			Double key = new Double(histogram[k].key);
			if (!xvals.containsKey(key))
			    xvals.put(key, new Double(0));
		    }
		}
	    }
		
	    Double xs[] = new Double[xvals.size()];
	    Enumeration e = xvals.keys();
	    for (int i = 0; e.hasMoreElements() ; i++)
		xs[i] = (Double)e.nextElement();
	    xvals = null;

	    class MyDouble implements SortableObject {
		public int compare(Object a, Object b) {
		    double aa = ((Double)a).doubleValue();
		    double bb = ((Double)b).doubleValue();
		    if (aa  < bb) return -1;
		    if (aa == bb) return 0;
		    return +1;
		}
	    }
	    QuickSort.quicksort(xs, new MyDouble());

	    // now write out the values, filling in 0 for those xvals that don't appear
	    out.write(titleWrapper);
	    out.write(title);
	    out.write(titleWrapper);
		
	    for(int i = 0 ; i < testProcedure.length ; i++) {
		out.write("\nTest Procedure: "+testProcedure[i]+"\n");
			
		// write out the x values as a header line
		out.write("Patient Set ");
		for(int k = 0 ; k < xs.length ; k++) 
		    out.write(xs[k] + " ");
		out.write("\n");
			
		//now write out a line for each patient set
		for(int j = 0 ; j < patientSet.length ; j++) {
		    out.write(""+patientSet[j]+" ");
				
		    Pair histogram[] = (Pair [])field.get(summary[i][j][numberOfStimuli]);
		    QuickSort.quicksort(histogram, new Pair());

		    int x = 0;
		    for(int k = 0 ; k < histogram.length ; k++) {
			while (xs[x].doubleValue() < histogram[k].key) { out.write(" 0 "); x++; }
			out.write(histogram[k].count + " ");
			x++;
		    }
		    while (x < xs.length) { out.write(" 0 "); x++; }

		    out.write("\n");
		}
	    }
	} catch (IOException e) {
	    JOptionPane.showMessageDialog(null, "Trouble writing summary information to file\n"+e,"Barramundi - Warning", JOptionPane.WARNING_MESSAGE);
	} catch (IllegalAccessException e) {
	    JOptionPane.showMessageDialog(null, "Cannot access field "+field.getName()+"\n"+e,"Barramundi - Warning", JOptionPane.WARNING_MESSAGE);
	}
    }// printSummaryStat()

    /**
       Force calculation so that the data vector can be freed for garbage collection.
    */
    void memorySavingPercentilesCalc(int testProcedure, int patientSet) {
	for(int i = 0 ; i <= numberOfStimuli ; i++) {
	    diffSummary[testProcedure][patientSet][i].calc();
	    numPresSummary[testProcedure][patientSet][i].calc();
	}
    }

        // written by Steven Burrows Oct 2005
        // modified andrew turpin
    public void printPoints(BufferedWriter out, StimulusRecord stimulus)
        {
                String line = null;
                String xToken, yToken;
                int x, y;
                StringTokenizer strtok;
                int lineNum = 1;
                int tokNum = 1;
                BufferedReader locationFile;
                int number = 0;

                try
                {
                        locationFile = new BufferedReader(new FileReader(stimulus.getLocationFilename()));


                        while ((line = locationFile.readLine()) != null)
                        {
                                strtok = new StringTokenizer(line, "~");


                                if ((strtok.countTokens() % 2) != 0)
                                {
                                        throw new NoSuchElementException();
                                }

                                tokNum = 1;


                                while (strtok.hasMoreTokens() == true)
                                {

                                        xToken = strtok.nextToken();
                                        tokNum++;
                                        yToken = strtok.nextToken();
                                        tokNum++;

                                        out.write("# ("+Integer.parseInt(xToken)+", "+
                                                      Integer.parseInt(yToken)+")");
                                }

                                lineNum++;
                        }

                }
                catch (NoSuchElementException nsee)
                {
                        System.out.println("Error: Line " + line + " of file '" + stimulus.getLocationFilename() +
                                "' should have an even number of tokens.\n" + nsee.getMessage());
                        System.exit(1);
                }
                catch (FileNotFoundException fnfe)
                {
                        System.err.println("Error: File '" + stimulus.getLocationFilename() + "' was not found.\n" + fnfe.getMessage());
                        System.exit(1);
                }
                catch (IOException ioe)
                {
                        System.err.println("Error: Unknown IOException occurred.\n" + ioe.getMessage());
                        System.exit(1);
                }
        }
}
