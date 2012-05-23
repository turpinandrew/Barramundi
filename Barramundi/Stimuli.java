package Barramundi;

/**
	This class manages the database of physical attributes of the 
	stimulus to be displayed.  It does not describe the testing procedures 
	that will be employed.
	
	@author Andrew Turpin (aturpin@discoveriesinsight.org)
	@version 0.1 April 1999
*/
class Stimuli extends Database {
	/**
		Read Stimuli database into "super.list".
		@param filename The name of the file containing the patient set database
	*/
	static final String DELIMITER = "~";
	Stimuli(String filename) throws DatabaseException { 
		super(filename, "Barramundi.StimulusRecord", DELIMITER); 
	}
}