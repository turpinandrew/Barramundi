package Barramundi;

import java.io.*;
import java.util.*;

/**
	This is the format of the test procedure that is actually used by the 
	simulator in the Control class.

	@see Barramundi.Control
		
	@author Andrew Turpin (aturpin@discoveriesinsight.org)
	@version 0.1 April 1999
    Modified Nov 2005 to put in stuff for controlling HFA. 
    (Moved up from TPGeneralStaircase)
*/
abstract class AbstractTestProcedure {
	/** the test procedure class name  */ String name;
	/** true to print debug info       */ boolean debug;
	/** file to write debug info to    */ BufferedWriter out;
	/** the stimulus                   */ StimulusRecord stimulus;
	/** a record of the run            */ PatientResult stats;
	/** have locations been loaded?    */ boolean pointsLoaded;
	/** locations to test              */ Vector points;
	
	AbstractTestProcedure(String n) { 
		name = n; 
		points = new Vector();
		pointsLoaded = false;
	}
	
    class Point {
            private int x, y, number;
            Point(int x, int y, int num) {
                    this.x = x;
                    this.y = y;
                    number = num;
            }

            public int getX() { return x; }
            public int getY() { return y; }
            public int getNumber() { return number; }
    }

    // Added by Steven Burrows September 2005.
	/** Loads all points from stimulus file into points */
    public void loadPoints()
	{
		String line = null;
		String xToken, yToken;
		int x, y;
		StringTokenizer strtok;
		int lineNum = 1;
		int tokNum = 1;
		BufferedReader locationFile;
		int number = 0;

		try
		{
			locationFile = new BufferedReader(new FileReader(stimulus.getLocationFilename()));

			// Read each line of the locations file.
			while ((line = locationFile.readLine()) != null)
			{
				strtok = new StringTokenizer(line, "~");

				// Check that number of coordinates is even.
				if ((strtok.countTokens() % 2) != 0)
				{
					throw new NoSuchElementException();
				}

				tokNum = 1;
				
				// Tokenise the point coordinates two coordinates at a time.
				while (strtok.hasMoreTokens() == true)
				{
					// Tokenise two coordinates.
					xToken = strtok.nextToken();
					tokNum++;
					yToken = strtok.nextToken();
					tokNum++;

					// Add point to Vector.
					x = Integer.parseInt(xToken);
					y = Integer.parseInt(yToken);
					Point point = new Point(x, y, number++);
					points.add(point);
				}

				lineNum++;
			}

		}
		catch (NoSuchElementException nsee)
		{
			System.out.println("Error: Line " + line + " of file '" + stimulus.getLocationFilename() + 
				"' should have an even number of tokens.\n" + nsee.getMessage());
			System.exit(1);
		}
		catch (FileNotFoundException fnfe)
		{
			System.err.println("Error: File '" + stimulus.getLocationFilename() + "' was not found.\n" + fnfe.getMessage());
			System.exit(1);
		}
		catch (IOException ioe)
		{
			System.err.println("Error: Unknown IOException occurred.\n" + ioe.getMessage());
			System.exit(1);
		}
	}

	/** Displays all points in a human-readable format. */
	public void printPoints()
	{
		// Process all points.
		for (int i = 0; i < points.size(); i++)
		{
			// Retrieve the coordinates from the Vector.
			Point point = (Point) points.get(i);
			int x = (int) point.getX();
			int y = (int) point.getY();

			// Display the point.
			System.out.print("(" + x + ", " + y + ")  ");

			// Print linebreaks at meaningful places.
			if ((i + 1) % 4 == 0)
			{
				System.out.println();
			}
		}
	}


	public void setup(StimulusRecord s, boolean debug, File logFile) {
		stimulus   = s; 
		this.debug = debug;
		stats      = new PatientResult(stimulus.numLocations);

		try {
			if (debug) out = new BufferedWriter(new FileWriter(logFile.getName(), true));
		} catch (IOException e) {
			System.err.println("Trouble opening logFile\n"+e);
		}
		
		if (pointsLoaded == false)
		{
			loadPoints();
			pointsLoaded = true;
		}

		localSetup();
	}//setup()
	
	public abstract void localSetup(); // { /* overide with setup specific to test procedure */ ; }
	
	public abstract void run(Patient p); // { /* must override */ };
	
	public abstract void localFinish(); // { /* overide with tidy up routines */ ; }
	
	public void finish() {
		try {
			if (debug) out.close(); 
		} catch (IOException e) {
			System.err.println("Trouble closing logFile\n"+e);
		}
		localFinish();
	};
	
	/**
		Write string s to the debug file if debug flag is true.
		@param s String to write to log file.
	*/
	public void debugWrite(String s) {
		if (debug) {
			try {
				out.write(s);
				out.flush();
			} catch (IOException e) {
				System.err.println("Trouble writing to debug log file");
			}
		}
	}// debugWrite()
			
}
