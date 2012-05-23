package Barramundi;

import java.util.StringTokenizer;
import java.lang.StringBuffer;
import javax.swing.*;

/** 
	The format of an individual patient in a file.
	
	toString() and fromString() implement the parsing interface required for DBs.
*/
class PatientDataRecord implements DatabaseRecord {
	/** Patient's name           */ String name;
	/** Patient's age            */ int age;
	/** Stimulus details         */ int numThresholds;
	/** Patient's thresholds     */ double threshold[];

	/** Convert a PatientDataRecord into a String */
	public String toString() {
		return  name + " " + age + " " + threshold[0] + "...";
	}

	/**
		Convert a String into a PatientDataRecord
	*/
	public void fromString(String s, String delimiter) throws DatabaseException { 
   		StringTokenizer t = new StringTokenizer(s, delimiter);
       	name 		   = t.nextToken();
   	   	age  		   = Integer.parseInt(t.nextToken());
   	   	numThresholds  = Integer.parseInt(t.nextToken());
   	   	threshold = new double[numThresholds];
   	   	for(int i = 0 ; i < numThresholds ; i++)
   	   		if (!t.hasMoreTokens())
   	   			throw new DatabaseException("Patient "+ i + "does not have enough thresholds.");
   	   		else
   	   			threshold[i] = Double.parseDouble(t.nextToken());
    }// fromString()
    
   	/**
		Convert this record into a string.
		@param delimiter The field delimiter.
	*/
	public String toDBString(String delimiter) {
		StringBuffer s = new StringBuffer(
			name + delimiter +
   	   		Integer.toString(age) + delimiter +
			Integer.toString(numThresholds)); 
			
   	   	for(int i = 0 ; i < numThresholds ; i++)
   	   		s.append(delimiter + Double.toString(threshold[i]));
   	   		
   	   	return s.toString();
	}// toDBString()

	/**
		Construct a PatientDataRecord from a String
		@param s 		 A delimited string of fields.
		@param delimiter The field delimiter.
	*/
	PatientDataRecord(String s, String delimiter) throws DatabaseException { 
		fromString(s, delimiter);
	}
	PatientDataRecord() { ; }
	
	/**	
		Provide a GUI to edit this object's 
		name, numLocations, *Excitation and presentationTime.
		@param parentFrame The container to hold any GUI widgets.
	*/
	public boolean edit(JFrame parentFrame) {
		return false;
	}// edit()
}// PatienDataRecord class