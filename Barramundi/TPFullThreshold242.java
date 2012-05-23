package Barramundi;

/**
    Staircase using steps of 4dB, 2dB.  
    Threshold is last seen value.
    A total of 2 reversals.  
    Starting point according to growth pattern.
    
    Growth pattern
    --------------
    Here is the 24-2 pattern numbered in HFA pattern.

                -21 -15  -9  -3   3   9  15  21  27 
   ST          +----------------+--------------------+       SN
            -21|         27  26 | 13  14             |-21
            -15|     25  24  23 | 10  11  12         |-15
             -9| 22  21  15  20 |  7   1   8   9     | -9
             -3| 19  18  17  16 |  2   3   4   5   6 | -3
               +----------------+--------------------+
              3| 32  31  30  29 | 42  43  44  45  46 |  3
              9| 35  34  28  33 | 47  41  48  49     |  9
   IT        15|     38  37  36 | 50  51  52         | 15    IN
             21|         40  39 | 53  54             | 21
               +----------------+--------------------+
                -21 -15  -9  -3   3   9  15  21  27 

    Primary points are (-9,-9), (-9,9), (9,9), and (9,-9).

    Here is the 24-2 pattern re-numbered to exclude two blind spots.
    This is what we use!

                -21 -15  -9  -3   3   9  15  21  27 
   ST          +----------------+--------------------+       SN
            -21|         26  25 | 13  14             |-21
            -15|     24  23  22 | 10  11  12         |-15
             -9| 21  20  15  19 |  7   1   8   9     | -9
             -3| 18      17  16 |  2   3   4   5   6 | -3
               +----------------+--------------------+
              3| 30      29  28 | 40  41  42  43  44 |  3
              9| 33  32  27  31 | 45  39  46  47     |  9
   IT        15|     36  35  34 | 48  49  50         | 15    IN
             21|         38  37 | 51  52             | 21
               +----------------+--------------------+
                -21 -15  -9  -3   3   9  15  21  27 

    So test order should be

                -21 -15  -9  -3   3   9  15  21  27 
               +----------------+-------------------+
            -21|          C   C | C   C             |-21
            -15|      B   B   B | B   B   B         |-15
             -9|  C   B   A   B | B   A   B   C     | -9
             -3|  C       B   B | B   B   B   C   D | -3
               +----------------+-------------------+
              3|  C       B   B | B   B   B   C   D |  3
              9|  C   B   A   B | B   A   B   C     |  9
             15|      B   B   B | B   B   B         | 15
             21|          C   C | C   C             | 21
               +----------------+-------------------+
                -21 -15  -9  -3   3   9  15  21  27 

         Primary points: 1, 15, 28, 41
         wave B        : 23-25, 10-12,20-21,7-8,16-18,2-4,29-31,42-44,33-34,47-48,36-38,50-52
         wave C        : 13-14, 26-27,9,5,45,49,22,19,32,35,39-40,53-54
         wave D        : 6,46
         Not tested    : 31 = blind spot in right eye


    Jan 24 2003: CAJ mentions that HFA adjust according to eccentricity by
                 0.4 dB per degree. I will try this:

                3      9      15    21     27 
           +------------------------------------+
           | -2.07  -1.94                       |-21
           | -1.03  -1.91  -3.39                |-15
           | +1.29    0    -1.91  -1.94         | -9
           | +3.39  +1.29  -1.03  -1.93   -2.05 | -3
           +------------------------------------+
               3       9     15     21     27


    d(x,y) { return sqrt(x^2+y^2) }
    Calculated for B(x,y)        { 0.4*(d(9,9) - d(x,y)) }.
    Calculated for C(21,-9) as ) { 0.4*(d(21,9) - (d(15,3)+d(15,9)+d(15,15))/3) }.
    etc

    Modified by Alex Pooley 29/4/04
      - Removed blnid spot code.
      - decrement values in NEIGHBOURS > 18 < 31
	  - decrement values by two in NEIGHBOURS > 31
	  - removed elements 18 and 31 from AGE_SLOPE, AGE_ZERO, ECC_ADJUST.
	  - removed blind spot handling code from run().

    @author Andrew Turpin (aturpin@discoveriesinsight.org)
    @version 0.3 June 2002
*/
class TPFullThreshold242 extends AbstractTestProcedure {

    /** input startGuess: -1 for 24-2 growth  */ double inputStartGuess;

    /** 24-2 growth pattern order */
/*
    final static int ORDER[] = new int[]
         {1,15,28,41,                                                                             // primary  
         23,24,25,10,11,12,20,21,7,8,16,17,18,2,3,4,29,30,42,43,44,33,34,47,48,36,37,38,50,51,52, // wave B
         13,14,26,27,9,5,45,49,22,19,32,35,39,40,53,54,                                            // wave C
         6,46,                                                                                    // wave D
         31}; // blind spot
*/
    //Modified by Alex Pooley 29/4/04.
    //  - decrement values in ORDER > 18 < 31
    //  - decrement values by two in ORDER > 31
    final static int ORDER[] = new int[]
         {1,15,27,39,                                                                             // primary  
         22,23,24,10,11,12,19,20,7,8,16,17,2,3,4,28,29,40,41,42,31,32,45,46,34,35,36,48,49,50, // wave B
         13,14,25,26,9,5,43,47,21,18,30,33,37,38,51,52,                                            // wave C
         6,44,                                                                                    // wave D
	 }; // blind spot



    /** The list of neighbours to average to get starting value. 
    ** Note that horizontal midline is not crossed, but vertical is.
    ** Indexed by HFA labelling of 24-2 pattern above.
    */
    final static int NEIGHBOURS[][] = new int[][] 
    { null,
     {},           //  1 - primary
     {1},          //  2
     {1},          //  3
     {1},          //  4
     {4,8},        //  5 - don't cross horizontal midline
     {5,9},        //  6 - don't speak
     {1},          //  7
     {1},          //  8
     {4,8,12},     //  9
     {1},          // 10
     {1},          // 11
     {1},          // 12
     {10,11,22},   // 13 - do cross vert
     {10,11,12},   // 14
     {},           // 15 - primary
     {15},         // 16
     {15},         // 17
//     null,         // 18 - blind spot
     {20},         // 19 - don't cross
     {15},         // 20
     {15},         // 21
     {20,24},      // 22
     {15},         // 23
     {15},         // 24
     {15},         // 25
     {10,22,23},   // 26 - do cross vert
     {22,23,24},   // 27
     {},           // 28 - primary
     {27},         // 29
     {27},         // 30
//     null,         // 31 - blind spot in right eye
     {32},         // 32 - don't cross, no blind
     {27},         // 33
     {27},         // 34
     {32,36},      // 35 - no blind
     {28},         // 36
     {28},         // 37
     {28},         // 38
     {34,35,48},   // 39 - do cross
     {34,35,36},   // 40
     {},           // 41 - primary
     {39},         // 42
     {39},         // 43
     {39},         // 44
     {42,46},      // 45 - don't speak
     {43,47},      // 46
     {39},         // 47
     {39},         // 48
     {42,46,50},   // 49
     {39},         // 50
     {39},         // 51
     {39},         // 52
     {34,48,49},   // 53 - do cross
     {48,49,50},   // 54 - yes! finished typing in these )(*&#$)@)! things.
    };

        // derived from DIS/Bristol/OHTS 500 normals in sap_data.xls.
    double AGE_SLOPE[] = new double[] {
             0,
			-0.054,	//  1
			-0.042,	//  2
			-0.041,	//  3
			-0.050,	//  4
			-0.058,	//  5
			-0.070,	//  6
			-0.049,	//  7
			-0.071,	//  8
			-0.061,	//  9
			-0.066,	// 10
			-0.071,	// 11
			-0.075,	// 12
			-0.081,	// 13
			-0.089,	// 14
			-0.059,	// 15
			-0.045,	// 16 changed from -0.052 to improve sita
			-0.058,	// 17
	     //			-0.188,	// 18
			-0.069,	// 19
			-0.057,	// 20
			-0.078,	// 21
			-0.086,	// 22
			-0.070,	// 23
			-0.084,	// 24
			-0.080,	// 25
			-0.093,	// 26
			-0.091,	// 27
			-0.052,	// 28
			-0.045,	// 29
			-0.054,	// 30
	     //			-0.022,	// 31
	                -0.060,	// 32
			-0.042,	// 33
			-0.055,	// 34
			-0.051,	// 35
			-0.037,	// 36
			-0.047,	// 37
			-0.071,	// 38
			-0.060,	// 39 changed from 0.050
			-0.060,	// 40 changed from 0.047
			-0.051,	// 41
			-0.041,	// 42
			-0.044,	// 43
			-0.060,	// 44
			-0.072,	// 45
			-0.082,	// 46
			-0.036,	// 47
			-0.048,	// 48
			-0.068,	// 49
			-0.046,	// 50
			-0.052,	// 51
			-0.061,	// 52
			-0.060,	// 53 changed from 0.050
			-0.067}; // 54

    double AGE_ZERO[] = new double[] {
            0,
			33.052	,//  1
			33.847	,//  2
			33.097	,//  3
			32.803	,//  4
			30.998	,//  5
			29.392	,//  6
			32.559	,//  7
			32.456	,//  8
			30.150	,//  9
			31.536	,// 10
			31.487	,// 11
			31.012	,// 12
			30.364	,// 13
			30.581	,// 14
			32.024	,// 15
			34.194	,// 16
			33.330	,// 17
	    //			33.042	,// 18
			32.417	,// 19
			32.535	,// 20
			32.248	,// 21
			32.359	,// 22
			31.177	,// 23
			31.744	,// 24
			31.239	,// 25
			30.237	,// 26
			30.299	,// 27
			33.263	,// 28
			34.228	,// 29
			33.273	,// 30
	    //			4.133	,// 31
			32.118	,// 32
			32.800	,// 33
			32.251	,// 34
			31.543	,// 35
			31.563	,// 36
			32.235	,// 37
			33.053	,// 38
			31.474	,// 39
			31.207	,// 40
			33.677	,// 41
			33.882	,// 42
			33.363	,// 43
			33.551	,// 44
			31.870	,// 45
			30.186	,// 46
			32.426	,// 47
			31.946	,// 48
			31.248	,// 49
			31.898	,// 50
			31.887	,// 51
			31.411	,// 52
			30.676	,// 53
			30.841};  // 5

    double ECC_ADJUST[] = new double[] {
        0, // dummy
        0, //  1
    +3.39, //  2
    +1.29, //  3
    -1.03, //  4
    -1.93, //  5
    -2.05, //  6
    +1.29, //  7
    -1.91, //  8
    -1.94, //  9
    -1.03, // 10
    -1.91, // 11
    -3.39, // 12
    -2.07, // 13
    -1.94, // 14
        0, // 15
    +3.39, // 16
    +1.29, // 17
	//    -1.03, // 18
    -1.93, // 19
    +1.29, // 20
    -1.91, // 21
    -1.94, // 22
    -1.03, // 23
    -1.91, // 24
    -3.39, // 25
    -2.07, // 26
    -1.94, // 27
        0, // 28
    +3.39,// 29
    +1.29,// 30
	//    -1.03,// 31
    -1.93,// 32
    +1.29,// 33
    -1.91,// 34
    -1.94,// 35
    -1.03,// 36
    -1.91,// 37
    -3.39,// 38
    -2.07,// 39
    -1.94,// 40
        0,// 41
    +3.39,// 42
    +1.29,// 43
    -1.03,// 44
    -1.93,// 45
    -2.05,// 46
    +1.29,// 47
    -1.91,// 48
    -1.94,// 49
    -1.03,// 50
    -1.91,// 51
    -3.39,// 52
    -2.07,// 53
    -1.94// 54
    };

    /** the total number of presentations */ int numPresentations;

    /**
        Constructor.
    */
    TPFullThreshold242(Double startGuess) {
            /* This call sets the name of the procedure that will be printed in the 
               results file (the first arg).
             */
        super("Full Threshold with growth pattern inc ECC_ADJUST, sg="+startGuess.toString());

        inputStartGuess = startGuess.doubleValue();
    }
        
    /** 
        Show each stimulus, recording response by calling the seen() function.
        Record information in "stats" for this patient.
    */
    public void run(Patient p) {
        if ((inputStartGuess == -1) && (stimulus.numLocations != 52)) {
            System.err.println("Not 24-2 pattern. Aborting.");
            return;
        }

        numPresentations = 0;
        
            /* set the inputThresholds for this patient */
        stats.inputThreshold = (double [])p.threshold.clone();

        /*
            This loop "displays" stimuli in order, you might want to more closely
            simulate a different presentation order, especially if your p.seen() function
            depends on number of presentations (eg fatigue).
        */
        for(int i = 0 ; i < stimulus.numLocations ; i++) {
            int startStimValue, loc;

            if (inputStartGuess == -1) {
                loc = ORDER[i] - 1; // location numbering starts at 1, so take one off
                                    // but when indexing NEIGHBOURS need to add it back!
            } else {
                loc = i;
            }

	    //Modified by Alex Pooley 29/4/04
	    //  - Removed blind spots from code.
	    //            if ((inputStartGuess == -1) && ((loc + 1 == 31) || (loc + 1 == 18)))  // skip blind spots
	    //            {
	    //                stats.measuredThreshold[loc] = p.threshold[loc];
	    //                continue;
	    //            }

            stats.numPresentation[loc] = 0;

            debugWrite("TPFullThreshGrowth: location (HFA order) "+ (int)(loc+1) +". ");
            debugWrite("Real threshold = "+p.threshold[loc]+"\n===================================\n");
            
            if (inputStartGuess == -1)
            {
                if (NEIGHBOURS[loc+1].length == 0) 
                {
                    //startStimValue = (int)Math.round((stimulus.maxExcitation-stimulus.minExcitation)/2);
                    startStimValue = (int)Math.round(AGE_ZERO[loc+1] + AGE_SLOPE[loc+1] * p.age);
                    debugWrite("Guess: primary point - " + startStimValue+"\n");
                } else {
                    double startGuess = 0;
                    for(int k = 0 ; k < NEIGHBOURS[loc+1].length ; k++)
                        startGuess += (stats.measuredThreshold[NEIGHBOURS[loc+1][k] - 1] 
                                   - ECC_ADJUST[NEIGHBOURS[loc+1][k]]);
                    startGuess /= (double)NEIGHBOURS[loc+1].length;

                    startGuess += ECC_ADJUST[loc+1];
                
                    startStimValue = (int)Math.round(startGuess);
                    debugWrite("Guess: "+startStimValue+"\n");
                }
            } else {
                startStimValue = (int)Math.round(inputStartGuess);
            }
            
            int secondThreshold, firstThreshold = doStair(p, loc, startStimValue);
            debugWrite("Threshold = "+ firstThreshold + "\n");
            if (Math.abs(firstThreshold - startStimValue) > 4)
            {
                debugWrite("Second staircase!\n");
                secondThreshold = doStair(p, loc, firstThreshold);
                debugWrite("Second staircase! second thresh="+secondThreshold+"\n");
                //stats.measuredThreshold[loc] = (firstThreshold + secondThreshold) / 2;
                //stats.measuredThreshold[loc] = firstThreshold; // clinical trials ignore second
                stats.measuredThreshold[loc] = secondThreshold;
            }
            else
                stats.measuredThreshold[loc] = firstThreshold;
        }
    }// end run()
    

    private int doStair(Patient p, int loc, int stimValue) 
    {
        boolean thisWasSeen, lastWasSeen;
        double step, lastSeenStim = 0;
        
            // present the first stimulus
        stats.numPresentation[loc]++;
        numPresentations++;
        step = 4;
        debugWrite("Present "+stimValue);
        lastWasSeen = p.seenObject.seen(stimValue, p.threshold[loc], numPresentations, 0,0);
        debugWrite(lastWasSeen?" Seen\n":" Not seen\n");
        
           // now present the rest            
        for(int numReversals = 0; numReversals < 2 ; ) {
            if (lastWasSeen)
                stimValue += step;
            else
                stimValue -= step;
                
            if (stimValue < 0)
                stimValue = 0;

            stats.numPresentation[loc]++;
            numPresentations++;
            debugWrite("Present "+stimValue);
            thisWasSeen = p.seenObject.seen(stimValue, p.threshold[loc], numPresentations,0,0);
            debugWrite(lastWasSeen?" Seen\n":" Not seen\n");
          
            if (thisWasSeen) 
                lastSeenStim = stimValue;
          
            if (thisWasSeen != lastWasSeen) {
                if (numReversals == 0)
                    step = 2;
                numReversals++;
            }
            
            lastWasSeen = thisWasSeen;
        }
        
        return (int)Math.round(lastSeenStim);
    }// doStair()

    public void localFinish() { ; }
    public void localSetup() { ; }
}
