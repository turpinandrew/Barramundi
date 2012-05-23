package Barramundi;

/**
	Exception returned when some part of a database is incorrect.
		
	@author Andrew Turpin (aturpin@discoveriesinsight.org)
	@version 0.1 April 1999
*/	
class DatabaseException extends Exception {
	public DatabaseException() {super();}
	public DatabaseException(String s) {super(s);}
}