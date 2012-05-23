package Barramundi;

/**
	Class to hold the test procedures, allow changes etc...
	
	@author Andrew Turpin (aturpin@discoveriesinsight.org)
	@version 0.1 April 1999
*/
class TestProcedures extends Database {
	/**
		Read TestProcedure database into "super.list".
		@param filename The name of the file containing the patient set database
	*/
	static final String DELIMITER = "~";
	TestProcedures(String filename) throws DatabaseException { 
		super(filename, "Barramundi.TestProcedureRecord", DELIMITER); 
	}
}