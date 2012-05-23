package Barramundi;

/**
	Database for reading the actual patient records.
		
	@author Andrew Turpin (aturpin@discoveriesinsight.org)
	@version 0.1 May 1999
*/
class PatientData extends Database {
	static final String DELIMITER = " \t";
	PatientData(String filename) throws DatabaseException {	
		super(filename, "Barramundi.PatientDataRecord", DELIMITER);
	}
} // PatientData class