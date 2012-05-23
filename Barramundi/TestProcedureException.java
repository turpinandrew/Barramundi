package Barramundi;

/**
	Exception returned when a supplied test procedure is written incorrectly.
		
	@author Andrew Turpin (aturpin@discoveriesinsight.org)
	@version 0.1 April 1999
*/	
class TestProcedureException extends Exception {
	public TestProcedureException() {super();}
	public TestProcedureException(String s) {super(s);}
}