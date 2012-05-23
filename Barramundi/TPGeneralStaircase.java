

package Barramundi;

import java.io.*;
import java.util.*;
class TPGeneralStaircase extends AbstractTestProcedure {
                                                   private final int NUM_TIMES_FLOOR_OR_CEILING = 4;
                                                 int inputStartGuess;
                                             int numPresentations;
 int numUp;
 int numDown;
 int numReversals;
 int numLastReversals;
 String stairStepsString;
 Vector stairSteps;
    String fName;
    TPGeneralStaircase(Double numUp, Double numDown, Double numReversals,
        Double numLastReversals, Double startGuess, String stairStepsString) {
        super("Staircase: "+numUp+"-up "+numDown+"-down r= "+numReversals+" av= "+numLastReversals+
               " step= "+stairStepsString+" sg="+startGuess.toString());
        inputStartGuess = startGuess.intValue();
  this.numUp = numUp.intValue();
  this.numDown = numDown.intValue();
  this.numReversals = numReversals.intValue();
  this.numLastReversals = numLastReversals.intValue();
  this.stairStepsString = stairStepsString;
  stairSteps = new Vector();
  tokeniseSteps(stairSteps, stairStepsString);
  if (this.numLastReversals > this.numReversals)
  {
   System.err.println("Error: 'numLastReversals' cannot be greater than 'numReversals'. " +
                   "Please correct TestProcedures.bar. Program terminating.");
   System.exit(1);
  }
  if (this.numReversals != stairSteps.size())
  {
            System.err.println("Error: 'numReversals' "+this.numReversals+" must be the same as 'stairSteps.size()'"+stairSteps.size()+". " +
                   "Please correct TestProcedures.bar. Program terminating.");
   System.exit(1);
  }
  fName = new String("tpGeneralStaircase.out");
    }
 public void tokeniseSteps(Vector stairSteps, String stairStepsString)
 {
  StringTokenizer strtok = new StringTokenizer(stairStepsString, ",");
  while (strtok.hasMoreTokens())
  {
   Integer value = new Integer(strtok.nextToken());
   stairSteps.add(value);
  }
 }
    public void run(Patient p) {
  Vector results = new Vector(stimulus.numLocations);
  Vector finishedResults = new Vector();
  int stopReversals = numReversals;
  for (int i = 0; i < points.size(); i++)
  {
   Point point = (Point) points.get(i);
   int x = (int) point.getX();
   int y = (int) point.getY();
   int number = (int)point.getNumber();
   int stepSize = ((Integer) stairSteps.get(0)).intValue();
   results.add(new Stair(x, y, number, inputStartGuess, stepSize, numUp, numDown));
         stats.numPresentation[number] = 0;
  }
        numPresentations = 0;
        stats.inputThreshold = (double [])p.threshold.clone();
            while (results.size() > 0)
            {
    int loc = (int) Math.floor(Math.random() * results.size());
                Stair stair = (Stair) results.get(loc);
                int x = stair.locx;
                int y = stair.locy;
                int intensity = stair.currentStim;
                if (intensity < stimulus.minExcitation)
                    intensity = (int)stimulus.minExcitation;
                if (intensity > stimulus.maxExcitation)
                    intensity = (int)stimulus.maxExcitation;
    stair.numPresentations++;
          stats.numPresentation[stair.locationNumber]++;
    boolean seen = p.seenObject.seen(intensity, p.threshold[stair.locationNumber], numPresentations, x, y);
                if ((!seen) && (intensity == stimulus.minExcitation))
     stair.numTimesMissedZero++;
                if ( (seen) && (intensity == stimulus.maxExcitation))
     stair.numTimesSeenForty++;
    if (stair.numTimesMissedZero >= NUM_TIMES_FLOOR_OR_CEILING ) {
     stair.done = true;
     finishedResults.add(results.remove(loc));
     stats.measuredThreshold[stair.locationNumber] = (int)stimulus.minExcitation;
    }
    else if (stair.numTimesSeenForty >= NUM_TIMES_FLOOR_OR_CEILING ) {
     stair.done = true;
     finishedResults.add(results.remove(loc));
     stats.measuredThreshold[stair.locationNumber] = (int)stimulus.maxExcitation;
    } else {
     if (seen) {
      stair.upCounter++;
      stair.downCounter = 0;
     } else {
      stair.downCounter++;
      stair.upCounter = 0;
     }
                    boolean reversal = false;
                    boolean changeStim = false;
     if (stair.upCounter == numUp) {
         reversal = stair.direction == Stair.DOWN;
                        stair.direction = Stair.UP;
      stair.upCounter = 0;
                        changeStim = true;
                    } else if (stair.downCounter == numDown) {
         reversal = stair.direction == Stair.UP;
                        stair.direction = Stair.DOWN;
      stair.downCounter = 0;
                        changeStim = true;
                    }
        if (reversal) {
      stair.numReversals++;
      if ((numLastReversals) > (numReversals - stair.numReversals))
       stair.averageReversalStim += (double)stair.currentStim / (double)numLastReversals;
      if (stair.numReversals < numReversals)
         stair.step = ((Integer) stairSteps.get(stair.numReversals)).intValue();
                        else {
       stair.done = true;
       stats.measuredThreshold[stair.locationNumber] = stair.averageReversalStim;
       finishedResults.add(results.remove(loc));
      }
     }
                    if (changeStim) {
                        if (stair.direction == Stair.UP)
          stair.currentStim += stair.step;
                        else
          stair.currentStim -= stair.step;
                    }
                }
   }
    }
    private void printResults(BufferedWriter bufWr, Vector results) throws IOException
    {
        System.out.print("\n");
        bufWr.write("\n");
        for(int i = 0 ; i < results.size() ; i++)
        {
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
    }
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
