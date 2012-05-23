package Barramundi;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.text.MessageFormat;
import java.lang.Integer;
import java.lang.Double;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
	This class contains all the necessary information on a run of a procedure
	on a single patient for full analysis.
		
	@author Andrew Turpin (aturpin@discoveriesinsight.org)
	@version 0.1 April 1999
*/
class PatientResult {
	/** all indexed by stimulus number */
	public double previousThreshold[];
	public double inputThreshold[];
	public double measuredThreshold[];
	public int    numPresentation[];
	
	/**
		Allocate memory for the data holding arrays.
		@param numStimuli The number of different stimuli (eg 17 for freq dbl).
	*/
	PatientResult(int numStimuli) {
		previousThreshold = new double[numStimuli];
		inputThreshold    = new double[numStimuli];
		measuredThreshold = new double[numStimuli];
		numPresentation   = new int[numStimuli];
	}

	/**
		@param out The BufferedWriter to write to.
		@param pad A string to prepend to every line.
	*/
	void write(BufferedWriter out, String pad) throws IOException {
		out.write(pad+" ");
		for(int i = 0 ; i < previousThreshold.length ; i++) out.write(previousThreshold[i] + " " );
		for(int i = 0 ; i < inputThreshold.length ; i++)    out.write(inputThreshold[i] + " " );
		for(int i = 0 ; i < measuredThreshold.length ; i++) out.write(measuredThreshold[i] + " " );
		for(int i = 0 ; i < numPresentation.length ; i++)   out.write(numPresentation[i] + " " );
		out.write("\n");
	}// end println()
}
