package Barramundi;

/**
	All patient modifiers that are applied before simulation starts 
	should sub-class this class.
*/
interface StaticModifier extends AbstractPatientModifier { 
	public void massage(Patient p);
}
