package Barramundi;

/**
	Exception returned when a supplied patient modifier is written incorrectly.
		
	@author Andrew Turpin (aturpin@discoveriesinsight.org)
	@version 0.1 May 1999
*/	
class PatientModifierException extends Exception {
	public PatientModifierException() {super();}
	public PatientModifierException(String s) {super(s);}
}
