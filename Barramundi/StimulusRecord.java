package Barramundi;

import java.util.StringTokenizer;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;

/** 
	A StimulusRecord contains all details of the physcal stimulus.
*/
class StimulusRecord implements DatabaseRecord {
	String name;
	
	/** numLocations is the total number of varying
	    items of the stimulus. For example the number of 
	    locations a stimulus will appear in a VF */
	int numLocations;
	/** The min and max values a threshold can take, and the minimum
		stepSize by which a stimulus can change */
	double minExcitation, maxExcitation, stepExcitation;
	/** presentationTime is the max time for presenation of a single 
		stimulus in seconds.  It does not include time for a response, or time for 
		the decision procedure of the test procedure. */
	double presentationTime;

	/** Filename containing x,y coordinates for locations to test */
	String locationFilename;
	
	/** Return the location filename. */
	public String getLocationFilename()
	{
		return locationFilename;
	}
	
	/** Convert a Stimulus into a String for display */
	public String toString() {
		return  name + ":  " + 
				Integer.toString(numLocations) + " locations.  Display in the range [" + 
				Double.toString(minExcitation) + "," + 
				Double.toString(maxExcitation) + "] with a min step of " +
				Double.toString(stepExcitation) + ".  Presentation time is " +
				Double.toString(presentationTime) + " seconds." +
				"Reading locations from file " + locationFilename;
	}//toPrettyString()
	
	/**
		Construct a Stimulus from a String
		@param s 		 A delimited string of fields.
		@param delimiter The field delimiter.
	*/
	public void fromString(String s, String delimiter) {
   		StringTokenizer t = new StringTokenizer(s, delimiter);
       	name              = t.nextToken();
   	   	numLocations  = Integer.parseInt(t.nextToken());
   	   	minExcitation     = Double.parseDouble(t.nextToken());
   		maxExcitation     = Double.parseDouble(t.nextToken());
   		stepExcitation    = Double.parseDouble(t.nextToken());
   		presentationTime  = Double.parseDouble(t.nextToken());
       	locationFilename  = t.nextToken();
   	}
   	
   	/**
		Convert this record into a string.
		@param delimiter The field delimiter.
	*/
	public String toDBString(String delimiter) {
       	return 	name                               + delimiter +
   	   		   	Integer.toString(numLocations) + delimiter +
				Double.toString(minExcitation)     + delimiter + 
				Double.toString(maxExcitation)     + delimiter +
				Double.toString(stepExcitation)    + delimiter +
				Double.toString(presentationTime)  + delimiter + 
				locationFilename;
	}// toDBString()

	/**
		Construct a Stimulus from a String
		@param s 		 A delimited string of fields.
		@param delimiter The field delimiter.
	*/
	StimulusRecord(String s, String delimiter) { 
		fromString(s,delimiter);
	}
	StimulusRecord() { ; }
	
	/**	
		Provide a GUI to edit this object's 
		name, numLocations, *Excitation and presentationTime.
		@param parentFrame The container to hold any GUI widgets.
	*/
	public boolean edit(JFrame parentFrame) {
    	JTextField  nameField, numLocField, minExField, maxExField, stepField, presTimeField, locFileField;
    	JPanel      thePanel;

		thePanel = new JPanel(false);
		thePanel.setLayout(new BoxLayout(thePanel, BoxLayout.X_AXIS));

		JPanel namePanel = new JPanel(false);
		namePanel.setLayout(new GridLayout(0, 1));
		namePanel.add(new JLabel("Stimulus name ", JLabel.RIGHT));
		namePanel.add(new JLabel("Number of locations ", JLabel.RIGHT));
		namePanel.add(new JLabel("Min excitation ", JLabel.RIGHT));
		namePanel.add(new JLabel("Max excitation ", JLabel.RIGHT));
		namePanel.add(new JLabel("Min excitation step ", JLabel.RIGHT));
		namePanel.add(new JLabel("Presentation time ", JLabel.RIGHT));
		namePanel.add(new JLabel("Locations filename", JLabel.RIGHT));

		JPanel fieldPanel = new JPanel(false);
		fieldPanel.setLayout(new GridLayout(0, 1));
		fieldPanel.add(nameField     = new JTextField(name,10));
		fieldPanel.add(numLocField   = new JTextField(Integer.toString(numLocations)));
		fieldPanel.add(minExField    = new JTextField(Double.toString(minExcitation)));
		fieldPanel.add(maxExField    = new JTextField(Double.toString(maxExcitation)));
		fieldPanel.add(stepField     = new JTextField(Double.toString(stepExcitation)));
		fieldPanel.add(presTimeField = new JTextField(Double.toString(presentationTime)));
		fieldPanel.add(locFileField  = new JTextField(locationFilename, 30));

		thePanel.add(namePanel);
		thePanel.add(fieldPanel);

	    	/**
    	 	* Brings up a JDialog using JOptionPane. returns true if record is changed, false otherwise,
    	 	*/
    	if(JOptionPane.showOptionDialog(parentFrame, thePanel, "Add new Stimulus",
			   JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
   	                null, new String[] {"Done", "Cancel"}, "Done") == 0) {
		    name             = nameField.getText();
			numLocations = Integer.parseInt(numLocField.getText());
			minExcitation    = Double.parseDouble(minExField.getText());
			maxExcitation    = Double.parseDouble(maxExField.getText());
			stepExcitation   = Double.parseDouble(stepField.getText());
			presentationTime = Double.parseDouble(presTimeField.getText());
			locationFilename = locFileField.getText();
			return true;
		} else
			return false;
	}// edit()
}
