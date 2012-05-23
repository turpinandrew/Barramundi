package Barramundi;

import java.io.*;
import java.util.*;

/**
    Staircase using steps of 4dB, 2dB.  
    Threshold is last seen value.
    A total of 2 reversals.  
    Starting point according to growth pattern.
    
    @author Andrew Turpin (aht@cs.rmit.edu.au)
    @version 0.1 Sep 2005

	Modified by Steven Burrows September 2005.
*/
class TPStaircase extends AbstractTestProcedure {

    /** input startGuess: -1 for 24-2 growth  */ double inputStartGuess;

    /** the total number of presentations */ int numPresentations;

    // Added by Steven Burrows September 2005.
	/** X and Y coordinates from the locations.txt file. */
	Vector points;
	boolean pointsLoaded;
	String fName;

	/** Class to hold location number and (x,y) position */
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
	}//class Point
	
    /**
        Constructor.
    */
    TPStaircase(Double startGuess) {
            /* This call sets the name of the procedure that will be printed in the 
               results file (the first arg).
             */
        super("Staircase reading points from file, sg="+startGuess.toString());

        inputStartGuess = startGuess.doubleValue();

		// Modified by Steven Burrows September 2005.
		points = new Vector();
		pointsLoaded = false;

		fName = new String("tpfullthreshold242.out");
    }

    // Added by Steven Burrows September 2005.
	/** Loads all points from locations.txt into the Vector. */
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

	/** Displays all points from locations.txt in a human-readable format. */
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

    /** 
        Show each stimulus, recording response by calling the seen() function.
        Record information in "stats" for this patient.
    */
    public void run(Patient p) {
        
		// Added by Steven Burrows September 2005.
		// Load points.
		if (pointsLoaded == false)
		{
			loadPoints();
			pointsLoaded = true;
		}

		Vector results = new Vector(stimulus.numLocations);
		Vector finishedResults = new Vector();

		int stopReversals = 2;

		for (int i = 0; i < points.size(); i++)
		{
			Point point = (Point) points.get(i);
			int x = (int) point.getX();
			int y = (int) point.getY();
			int number = (int)point.getNumber();
			results.add(new Stair(x, y, number, 25, 4, 1, 1));
        	stats.numPresentation[number] = 0;
		}

        numPresentations = 0;
        
        // Set the inputThresholds for this patient.
        stats.inputThreshold = (double [])p.threshold.clone();

		try 
		{
            BufferedWriter bufWr = new BufferedWriter(new FileWriter(fName));

            bufWr.write("START:");
            bufWr.write(new Date(System.currentTimeMillis()).toString() + "\n\n");

            while (results.size() > 0)
            {
                Stair stair;
				int loc;

				loc   = (int) Math.floor(Math.random() * results.size());
                stair = (Stair) results.get(loc);

                int x         = stair.locx;
                int y         = stair.locy;
                int intensity = stair.currentStim;
                if (intensity < 0) 
				{
                    intensity = 0;
				}
                if (intensity > 40) 
				{
                    intensity = 40;
				}
                
				bufWr.write("Location "+stair.locationNumber+" ("+x+","+y+") intensity = "+intensity+" ");
                System.out.print("Location "+stair.locationNumber+" ("+x+","+y+
						") intensity = "+intensity+
						" ");

				stair.numPresentations++;
        		stats.numPresentation[stair.locationNumber]++;

				boolean seen = p.seenObject.seen(intensity, p.threshold[stair.locationNumber], numPresentations, x, y);

                if (seen)
				{ 
					bufWr.write("Seen ");
					System.out.print("Seen ");
                    stair.lastSeenStim = stair.currentStim;
				}
				else 
				{
					bufWr.write("Not Seen ");
					System.out.print("Not Seen ");
				}

                if ((!seen) && (intensity == 0))  
				{
					stair.numTimesMissedZero++;
				}
                if ( (seen) && (intensity == 40)) 
				{
					stair.numTimesSeenForty++;
				}

                if (stair.numPresentations > 1)
                {
                    if ((seen && !stair.lastSeen) || (!seen && stair.lastSeen))
                    {
                        stair.numReversals++;
                        stair.averageReversalStim += stair.currentStim / stopReversals;
                        stair.step = 2;
                        System.out.print("Reversal: "+stair.currentStim);
                        bufWr.write("Reversal: "+stair.currentStim);
                        if (stair.numReversals == stopReversals)
                        {
                            stair.done = true;
                			stats.measuredThreshold[stair.locationNumber] = stair.lastSeenStim;
							finishedResults.add(results.remove(loc));
                        }
                    } 
					else if (stair.numTimesMissedZero > 1)
					{
                        stair.done = true;
						finishedResults.add(results.remove(loc));
                		stats.measuredThreshold[stair.locationNumber] = 0;
                    }
					else if (stair.numTimesSeenForty > 1)
					{
                        stair.done = true;
						finishedResults.add(results.remove(loc));
                		stats.measuredThreshold[stair.locationNumber] = 40;
                    }
                }
                stair.lastSeen = seen;
                if (seen)
                {
                    stair.currentStim += stair.step;
                }
				else
				{
                    stair.currentStim -= stair.step;
				}
				
                bufWr.flush();
				bufWr.write("\n");
				System.out.print("\n");
			}

            printResults(bufWr, finishedResults);
    
            bufWr.write("END:");
            bufWr.write(new Date(System.currentTimeMillis()).toString() + "\n");
            bufWr.flush();
            bufWr.close();
        }
		catch (IOException e)
		{
            System.err.println("Couldn't write to the output file!");
        }

		for (int i = 0; i < points.size(); i++)
		{
			System.out.println(
						"lastSS = "+stats.measuredThreshold[i]+
						"numP = "+stats.numPresentation[i]
			);
		}
    } // End run().
    


    /**
    * Assumes results[i][j] is num seen at location i for point j.
    */
    private void printResults(BufferedWriter bufWr, Vector results) throws IOException
    {
        System.out.print("\n");
        bufWr.write("\n");
        for(int i = 0 ; i < results.size() ; i++)
        {
            /*
			System.out.print("\t\t(");
            nicePrint(System.out, ((Stair) results.get(i)).locx);
            System.out.print(",");
            nicePrint(System.out, ((Stair) results.get(i)).locy);
            System.out.print(") ");
            nicePrint(System.out, ((Stair) results.get(i)).lastSeenStim);
            System.out.print("\n");
			*/

            bufWr.write("(");
            nicePrint(bufWr, ((Stair) results.get(i)).locx);
            bufWr.write(",");
            nicePrint(bufWr, ((Stair) results.get(i)).locy);
            bufWr.write(") ");
            nicePrint(bufWr, ((Stair) results.get(i)).lastSeenStim);
            bufWr.write("\n");
        }

		System.out.print("\n");
        bufWr.write("\n");
    }// printResults()

    private void nicePrint(PrintStream os, int i) throws IOException
    {
        if ((i > 0) && (i < 10)) os.print(" "+i);
        else os.print(i);
    }
    private void nicePrint(BufferedWriter os, int i) throws IOException
    {
        if ((i > 0) && (i < 10)) { os.write(" "); os.write(Integer.toString(i));}
        else os.write(Integer.toString(i));
    }

    public void localFinish() { ; }
    public void localSetup() { ; }
}
