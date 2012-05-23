package Barramundi;

import javax.swing.JFrame;

/**
	This is the interface that data base record classes must confrom 
	to.  ie they must supply these methods.
*/
public interface DatabaseRecord {
	/**	Convert a DatabaseRecord to a String for display. */
	String toString();
	
	/**	Convert a String into a DatabaseRecord for internal use. */
	void fromString(String s, String delimiter) throws DatabaseException;
	
	/**	Convert a DatabaseRecord into a String for internal use. */
	String toDBString(String delimiter) throws DatabaseException;
	
	/**
		Read information from a GUI and alter the fields of the object.
		@param parentFrame The frame to which the GUI is attached.
	*/
	boolean edit(JFrame parentFrame);
}
