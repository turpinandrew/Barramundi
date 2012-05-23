package Barramundi;

import java.util.Vector;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.lang.ClassNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import javax.swing.*;
import java.awt.*;

/** 
	A test procedure contains a name and a list of parameters for that 
	test procedure.  Each paramater has a value and a mandatory comment.
	package Barramundi;

	@author Andrew Turpin (aturpin@discoveriesinsight.org)
	@version 0.1 April 1999
*/
class TestProcedureRecord implements DatabaseRecord {
	String name;
	String executingClass;
	Vector parameters;	
	
	class TestProcedureParam { // Simple class to hold (value,comment) pairs	
		String value;
		String comment;
		TestProcedureParam(String v, String c) { value = v ; comment = c; }
	}
	
	/** Convert a TestProcedure into a String for display */
	public String toString() {
		String s = name + " (" + executingClass + "):  " ;
		Enumeration e = parameters.elements();
		while (e.hasMoreElements()) {
			TestProcedureParam p = (TestProcedureParam)e.nextElement();
			s +=  " " + p.comment + " " + p.value;
		}
		return s;
	}

	/**
		Construct a TestProcedure from a String
		@param s 		 A delimited string of fields.
		@param delimiter The field delimiter.
	*/
	public void fromString(String s, String delimiter) { 
   		StringTokenizer t = new StringTokenizer(s, delimiter);
       	name              = t.nextToken();
       	executingClass    = t.nextToken();       	
       	parameters        = new Vector();
       	while (t.hasMoreTokens()) {
       		final String v = t.nextToken();
       		final String c = t.nextToken();
       		parameters.addElement(new TestProcedureParam(v, c));
       	}
	}// fromString()
	
	/**
		Convert this TestProcedure to a String for writing to a database
		@param delimiter The field delimiter.
	*/
	public String toDBString(String delimiter) { 
		StringBuffer s = new StringBuffer(
       		name + delimiter +
       		executingClass);
       	
       	for( int i = 0 ; i < parameters.size() ; i++) {
       		TestProcedureParam tpp = (TestProcedureParam)parameters.elementAt(i);
       	 	s.append(delimiter + tpp.value + delimiter + tpp.comment);
       	}
       	
       	return s.toString();
	}// toString()
	
	/**
		Construct a TestProcedure from a String
		@param s 		 A delimited string of fields.
		@param delimiter The field delimiter.
	*/
	TestProcedureRecord(String s, String delimiter) { fromString(s, delimiter);}
	TestProcedureRecord() { ; }

	/**
		Create the AbstractTestProcedure object for use in the simulator
	*/	
	AbstractTestProcedure getAbstractTestProcedure() 
	throws TestProcedureException {
	    try { 
       		final Class c   = Class.forName(executingClass);
       		final Constructor conss[] = c.getDeclaredConstructors();
       		Constructor cons = null;

		//Find a matching constructor: same length, same parameters.
		//Due to technicalities, we can only infer a constructor is not the
		//constructor we want if the location of the number arguments is correct.
		//Maybe this is ok... ?
		boolean found = false;
       		for (int conIndex=0; !found && conIndex < conss.length; conIndex++)
		{
		    //Does this constructor and the input test procedure have the same number
		    //of parameters?
		    Class params[] = conss[conIndex].getParameterTypes();
		    if (params.length == parameters.size())
		    {
			//Do they have the same parameters?
			boolean sameParams = true;
			for (int param=0; param<parameters.size(); param++)
			{
			    //Check if this constructor expects a double.
			    if (params[param].getName() == (new Double(0).getClass().getName()))
			    {
				//Is the string a double.
				try {
				    Double num = new Double(((TestProcedureParam)parameters.elementAt(param)).value);
				}
				catch (NumberFormatException e) {
				    sameParams = false;
				}
			    }
			    //Ignore anything else, we can't check... can we?
			}
			if (sameParams)
			{
			    found = true;
			    cons = conss[conIndex];
			}
		    }
		}

		//Throw exception if we never found a matching length constructor.
		if (!found)
		{
		    throw new TestProcedureException("Class " + executingClass + " does not have a matching constructor.");
		}
		else
		{
		    final Class paramTypes[] = cons.getParameterTypes();
		    Object params[] = new Object[parameters.size()];
		    for(int i = 0 ; i < parameters.size() ; i++)
			if (paramTypes[i].getName() == (new String()).getClass().getName())
			    params[i] = new String(((TestProcedureParam)parameters.elementAt(i)).value);
			else if (paramTypes[i].getName() == (new Double(0)).getClass().getName())
			    params[i] = new Double(Double.parseDouble(((TestProcedureParam)parameters.elementAt(i)).value));
			else
			    throw new TestProcedureException("Class " + executingClass + "'s constructor has a parameter that is not String or Double");
		    return (AbstractTestProcedure)(cons.newInstance(params));
		}
	} catch (InvocationTargetException e) {
	    e.printStackTrace();
        	throw new TestProcedureException("Test procedure " + executingClass + " " +e+" "+e.getCause());
        } catch (InstantiationException e) {
        	throw new TestProcedureException("Test procedure " + executingClass + " " +e);
        } catch (IllegalAccessException e) {
           	throw new TestProcedureException("Test procedure " + executingClass + " " +e);
        } catch (ClassNotFoundException e) {
           	throw new TestProcedureException("Test procedure " + executingClass + " " +e);
        }
	}
	
	/**	
		Provide a GUI to edit this object's variables.
		Only allows for "numParamBoxes" parameters to the method.
		If parameters == null, then assume new procedure is being added.
		
		@param parentFrame The container to hold any GUI widgets.
	*/
	public boolean edit(JFrame parentFrame) {
		final int numParamBoxes = 10;
    	JTextField  nameField, executingClassField;
    	JTextField  paramNames[] = new JTextField[numParamBoxes];
    	JTextField  paramFields[] = new JTextField[numParamBoxes];
    	
    	JPanel      thePanel;

		thePanel = new JPanel(false);
		thePanel.setLayout(new BoxLayout(thePanel, BoxLayout.X_AXIS));

		JPanel namePanel = new JPanel(false);
		namePanel.setLayout(new GridLayout(0, 1));
		namePanel.add(new JLabel("Test Procedure name ", JLabel.RIGHT));
		namePanel.add(new JLabel("Name of the class implementing this method ", JLabel.RIGHT));
		
		JPanel fieldPanel = new JPanel(false);
		fieldPanel.setLayout(new GridLayout(0, 1));
		fieldPanel.add(nameField             = new JTextField(name,10));
		fieldPanel.add(executingClassField   = new JTextField(executingClass));

		if ((parameters == null) || (parameters.size() > 0)) {
			namePanel.add(new JLabel("Parameter description ", JLabel.RIGHT));
			fieldPanel.add(new JLabel("Parameter value ", JLabel.RIGHT));
		}
		
		if (parameters == null)
			for (int i = 0 ; i < numParamBoxes ; i++) {
				namePanel.add(paramNames[i] = new JTextField(10));
				fieldPanel.add(paramFields[i] = new JTextField(10));
			}
		else
			for (int i = 0 ; i < parameters.size() ; i++) {
				TestProcedureParam tpp = (TestProcedureParam)parameters.elementAt(i);
				namePanel.add(new JLabel(tpp.comment+" ", JLabel.RIGHT));
				fieldPanel.add(paramFields[i] = new JTextField(tpp.value));
			}

		thePanel.add(namePanel);
		thePanel.add(fieldPanel);

	    	/**
    	 	* Brings up a JDialog using JOptionPane containing the connectionPanel.
     		* If the user clicks on the 'Connect' button the connection is reset.
	     	*/
    	if(JOptionPane.showOptionDialog(parentFrame, thePanel, "Add new Stimulus",
			   JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
   	                null, new String[] {"Done","Cancel"}, "Done") == 0) {
		    name             = nameField.getText();
			executingClass   = executingClassField.getText();
			if (parameters == null) {
				parameters = new Vector();
				for (int i = 0 ; i < numParamBoxes ; i++) {
					if (paramNames[i].getText().length() > 0)
						parameters.addElement(new TestProcedureParam(paramFields[i].getText(), paramNames[i].getText()));
				}
			}else
				for (int i = 0 ; i < parameters.size() ; i++) {
					TestProcedureParam tpp = (TestProcedureParam)parameters.elementAt(i);
					parameters.set(i, new TestProcedureParam(paramFields[i].getText(), tpp.comment));
				}
			return true;
		} else 
			return false;
	}// edit()
}// end TestProcedure
