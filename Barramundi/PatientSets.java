package Barramundi;

/**
	Class for reading/writing patient sets to a file, generating 
	new patient sets. ie handling the patient sets database.
	
	@author Andrew Turpin (aturpin@discoveriesinsight.org)
	@version 0.1 April 1999
*/
class PatientSets extends Database {
	/**
		Read Patient database into "super.list".
		@param filename The name of the file containing the patient set database
	*/
	PatientSets(String filename) throws DatabaseException {	
		super(filename, "Barramundi.PatientSetRecord", "~"); 
	}
}