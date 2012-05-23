package Barramundi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import javax.swing.JOptionPane;
import java.lang.Math;
import java.lang.reflect.Method;

/**
    Ally's supra-threshold followed by abnormal zest procedure: EMU.
    
    If the number of locations is 54, then
        This one does 4 primary points, to determine general height of Pt,
        then bases the 95% CI on that GH.
        Primary points are 1, 15, 28 and 41.
    else if the number of locations is not 54
        There is no GH correction.

    NOTE - this is adapted from an older version of Barramundi where seen() did not
           have (x,y) as parameters, so cannot vary seen response based on 
           location.

         - returns a threshold of -1 if suprathreshold test passed.

    @author Andrew Turpin (andrew@computing.edu.au)
    @version 0.1 Nov 2002
    @version 0.2 May 2012

    Age adjustment factors 
    ----------------------
    Taken from DIS SAP spreadsheet of 540 odd normals.
    (As an aside, these were derived from thresholds collected using the 
     Full Threshold methodology, which is fundamentally flawed, so maybe 
     fiddling them a bit is kosher????)

    These are the "intercepts" of the linear regression for each location. In 
    effect this is the expected threshold of a 0 year old person.
                30    30    30    31            
            31  32    31    32    31    31        
        32  32  32    33    33    33    32    30    
        32      33    34    34    33    33    31    29
        32      33    34    34    33    34    32    30
        32      32    33    33    32    34    32    31    
            33  32    32    32    32    31        
                31    31    31    31            

    This is above less 34:

                -4  -4   -4    -3            
            -3  -2  -3   -2    -3    -3        
        -2  -2  -2  -1   -1    -1    -2    -4    
        -2      -1   0    0    -1    -1    -3    -5
        -2      -1   0    0    -1     0    -2    -4
        -2  -2  -1  -1   -2     0    -2    -3    
            -1  -2  -2   -2    -2    -3        
                -3  -3   -3    -3            

    This is the slope of the regression line *100 on same patient set:

              -9  -9  -8  -9        
           -8 -8  -7  -7  -7  -8     
       -9  -8 -6  -6  -5  -5  -7  -6    
       -7     -6  -5  -4  -4  -5  -6  -7
       -6     -5  -4  -4  -4  -6  -7  -8
       -5  -6 -5  -4  -4  -5  -5  -7    
           -7 -5  -4  -5  -5  -6     
              -5  -5  -5  -7        
        

    Altered by hand to improve SITA results around location 16
              -9  -9  -8  -9        
           -8 -8  -7  -7  -7  -8     
       -9  -8 -6  -6  -5  -5  -7  -6    
       -7     -6  -4  -4  -4  -5  -6  -7
       -6     -5  -4  -4  -4  -6  -7  -8
       -5  -6 -5  -4  -4  -5  -5  -7    
           -7 -5  -4  -5  -5  -6     
              -6  -6  -6  -7        

    @author Andrew Turpin (turpinandrew@hotmail.com)
    @version 15 Oct 2002
*/
class TPEMU extends AbstractTestProcedure {
    /** The name of this procedure to print   */ String title;
    
    /** File where pdf is stored              */ BufferedReader pdfFile;
    /** File where mlf is stored              */ BufferedReader mlfFile;
    /** pdf for each location                 */ double pdf[][];
    /** likelihood function for each stimulus */ double mlf[][];

    /** The index to align mlfs pre multiply  */ int mlfAlignmentPoint[];
    /** The number of data points in a pdf/mlf*/ int domain;
    /** Paramater that controls stopping      */ double stoppingParam;
    /** Type of stopping condition            */ int stoppingCondition;
    /** stopping condition 1                  */ static final int NUMBER_OF_PRESENTAIONS = 1;
    /** stopping condition 2                  */ static final int STANDARD_DEVIATION     = 2;

    /** 3rd argument to alignMlf() */ private final static boolean INVERT    = true;
    /** 3rd argument to alignMlf() */ private final static boolean NO_INVERT = false;

    /** max number oif presentations allowed    */ static int MAX_NUM_PRESENTATIONS = 35;

    /** the total number of presentations over all locations */ int numPresentations;

        // derived from DIS/Bristol/OHTS 500 normals in sap_data.xls.
    double AGE_SLOPE[] = new double[] {
             0,
            -0.054,    //  1
            -0.042,    //  2
            -0.041,    //  3
            -0.050,    //  4
            -0.058,    //  5
            -0.070,    //  6
            -0.049,    //  7
            -0.071,    //  8
            -0.061,    //  9
            -0.066,    // 10
            -0.071,    // 11
            -0.075,    // 12
            -0.081,    // 13
            -0.089,    // 14
            -0.059,    // 15
            -0.045,    // 16 changed from -0.052 to improve sita
            -0.058,    // 17
            -0.188,    // 18
            -0.069,    // 19
            -0.057,    // 20
            -0.078,    // 21
            -0.086,    // 22
            -0.070,    // 23
            -0.084,    // 24
            -0.080,    // 25
            -0.093,    // 26
            -0.091,    // 27
            -0.052,    // 28
            -0.045,    // 29
            -0.054,    // 30
            -0.022,    // 31
            -0.060,    // 32
            -0.042,    // 33
            -0.055,    // 34
            -0.051,    // 35
            -0.037,    // 36
            -0.047,    // 37
            -0.071,    // 38
            -0.060,    // 39 changed from 0.050
            -0.060,    // 40 changed from 0.047
            -0.051,    // 41
            -0.041,    // 42
            -0.044,    // 43
            -0.060,    // 44
            -0.072,    // 45
            -0.082,    // 46
            -0.036,    // 47
            -0.048,    // 48
            -0.068,    // 49
            -0.046,    // 50
            -0.052,    // 51
            -0.061,    // 52
            -0.060,    // 53 changed from 0.050
            -0.067}; // 54

    double AGE_ZERO[] = new double[] {
            0,
            33.052    ,
            33.847    ,
            33.097    ,
            32.803    ,
            30.998    ,
            29.392    ,
            32.559    ,
            32.456    ,
            30.150    ,
            31.536    ,
            31.487    ,
            31.012    ,
            30.364    ,
            30.581    ,
            32.024    ,
            34.194    ,
            33.330    ,
            33.042    ,
            32.417    ,
            32.535    ,
            32.248    ,
            32.359    ,
            31.177    ,
            31.744    ,
            31.239    ,
            30.237    ,
            30.299    ,
            33.263    ,
            34.228    ,
            33.273    ,
            4.133    ,
            32.118    ,
            32.800    ,
            32.251    ,
            31.543    ,
            31.563    ,
            32.235    ,
            33.053    ,
            31.474    ,
            31.207    ,
            33.677    ,
            33.882    ,
            33.363    ,
            33.551    ,
            31.870    ,
            30.186    ,
            32.426    ,
            31.946    ,
            31.248    ,
            31.898    ,
            31.887    ,
            31.411    ,
            30.676    ,
            30.841};

        // derived from DIS/Bristol/OHTS 500 normals in sap_data.xls.
        // by taking the stddev of diff between actual and the value 
        // calced by AGE_ZERO + age*AGE_SLOPE.
        //
    double AGE_STDDEV[] = new double[] {
             0,
            2.7,    //  1
            2.5,    //  2
            2.5,    //  3
            2.8,    //  4
            3.3,    //  5
            4.1,    //  6
            2.8,    //  7
            2.9,    //  8
            3.5,    //  9
            3.3,    // 10
            3.3,    // 11
            3.5,    // 12
            4.0,    // 13
            4.3,    // 14
            3.0,    // 15
            2.7,    // 16 
            2.6,    // 17
            8.7,    // 18
            3.4,    // 19
            3.0,    // 20
            3.3,    // 21
            3.4,    // 22
            3.5,    // 23
            3.6,    // 24
            3.5,    // 25
            4.4,    // 26
            4.3,    // 27
            3.1,    // 28
            2.5,    // 29
            3.3,    // 30
            6.9,    // 31
            3.6,    // 32
            2.7,    // 33
            3.1,    // 34
            3.5,    // 35
            2.8,    // 36
            3.0,    // 37
            3.0,    // 38
            3.1,    // 39 
            3.1,    // 40 
            2.7,    // 41
            2.5,    // 42
            2.5,    // 43
            3.5,    // 44
            3.1,    // 45
            3.9,    // 46
            2.7,    // 47
            2.8,    // 48
            3.2,    // 49
            3.0,    // 50
            3.2,    // 51
            3.2,    // 52
            3.2,    // 53 
            3.4}; // 54

    /**
        @param pdfFilename  The name of the file from which to read pdf.
        @param mlfFilename   The name of the file from which to read mlfs.
        @param stopCondition Either NUMBER_OF_PRESENTAIONS or STANDARD_DEVIATION.
    */
    TPEMU(String pdfFilename, String mlfFilename, 
            String stopCondition, Double stopParam) {
        super("EMU"+pdfFilename+" "+
                mlfFilename+" "+stopCondition+" "+stopParam.toString());
        title = "EMU"+pdfFilename+
                mlfFilename+" "+stopCondition+" "+stopParam.toString();

        try {
            pdfFile = new BufferedReader(new FileReader(pdfFilename));
            mlfFile = new BufferedReader(new FileReader(mlfFilename));
            
            if (stopCondition.compareTo("NUMBER_OF_PRESENTATIONS") == 0)
                stoppingCondition = NUMBER_OF_PRESENTAIONS;
            else if (stopCondition.compareTo("STANDARD_DEVIATION") == 0)
                stoppingCondition = STANDARD_DEVIATION;
            else
                JOptionPane.showMessageDialog(null,"The stopping condition "+stopCondition+" for TPMaxLike is unknown","Barramundi - Error",JOptionPane.ERROR_MESSAGE);
            stoppingParam   = stopParam.doubleValue();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Could not find pdf/mlf file\n" + e,"Barramundi - Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /** 
        intialise domain
         read pdfs and mlfs, intialise mlfAlignmentPoint.
    */
    public void localSetup() { 
        domain = (int)((stimulus.maxExcitation - stimulus.minExcitation)/stimulus.stepExcitation) + 1;

        pdf = readDiscreteFunctions(stimulus.numLocations, domain, pdfFile);
        mlf = readDiscreteFunctions(stimulus.numLocations, domain, mlfFile);

        mlfAlignmentPoint = new int[stimulus.numLocations];
        for(int i = 0 ; i < mlfAlignmentPoint.length ; i++) {
            for(int j = 0 ; j < domain ; j++)
                if (mlf[i][j] == 0.5)
                    mlfAlignmentPoint[i] = j;
        }
    
    }// localSetup()
    

    /** 
        Show two supra-threshold targets. As soon as one is missed, 
        start ZEST. If both seen, done that location.
        Record information in "stats" for this patient.
    */
    public void run(Patient p) 
    {
        double heightDelta = 0;

        numPresentations = 0;
        stats.inputThreshold = (double [])p.threshold.clone();

        if (stimulus.numLocations == 54) 
        {
            stats.numPresentation[00] = 0;
            stats.numPresentation[14] = 0;
            stats.numPresentation[27] = 0;
            stats.numPresentation[40] = 0;
            stats.measuredThreshold[00] = doZest(p, 00);
            stats.measuredThreshold[14] = doZest(p, 14);
            stats.measuredThreshold[27] = doZest(p, 27);
            stats.measuredThreshold[40] = doZest(p, 40);
            debugWrite("TPEMU: 4 primaries ");
            debugWrite("Real ="+p.threshold[00]+" Measured="+stats.measuredThreshold[00]+"\n");
            debugWrite("Real ="+p.threshold[14]+" Measured="+stats.measuredThreshold[14]+"\n");
            debugWrite("Real ="+p.threshold[27]+" Measured="+stats.measuredThreshold[27]+"\n");
            debugWrite("Real ="+p.threshold[40]+" Measured="+stats.measuredThreshold[40]+"\n");

            double ageHeight = AGE_ZERO[01] + AGE_SLOPE[01] * p.age +
                               AGE_ZERO[15] + AGE_SLOPE[15] * p.age +
                               AGE_ZERO[28] + AGE_SLOPE[28] * p.age +
                               AGE_ZERO[41] + AGE_SLOPE[41] * p.age;
            ageHeight /= 4.0;

            double thisHeight = stats.measuredThreshold[00] + 
                                stats.measuredThreshold[14] + 
                                stats.measuredThreshold[27] + 
                                stats.measuredThreshold[40];
            thisHeight /= 4.0;

            heightDelta = thisHeight - ageHeight;
            if (heightDelta < 0)
                heightDelta = 0;
        }

        for(int i = 0 ; i < stimulus.numLocations ; i++) {
            int loc = i;

            if (stimulus.numLocations == 54) 
                if ((loc + 1 == 31) || (loc + 1 == 18)  // skip blind spots
                ||  (loc == 00) || (loc == 14)
                ||  (loc == 27) || (loc == 40)) // skip primary points
                    continue;

            debugWrite("TPEMU: location "+ (int)(loc+1) +". ");
            debugWrite("Real threshold = "+p.threshold[loc]+"\n===================================\n");

            stats.numPresentation[loc] = 0;

            int stimValue;
            if (stimulus.numLocations == 54)
                stimValue = (int)Math.round(AGE_ZERO[loc+1] 
                                      + AGE_SLOPE[loc+1] * p.age
                                      - 1.96 * AGE_STDDEV[loc+1]
                                      + heightDelta );
            else
                stimValue = (int)Math.round(AGE_ZERO[1] 
                                      + AGE_SLOPE[1] * p.age
                                      - 1.96 * AGE_STDDEV[1]);

            debugWrite("Guess: " + stimValue+"\n");

                // present the first suprathreshold stim
            boolean firstSeen, secondSeen = true;
            stats.numPresentation[loc]++;
            numPresentations++;
            firstSeen = p.seenObject.seen(stimValue, p.threshold[loc], numPresentations, 0, 0);

            if (firstSeen)
            {
                stats.numPresentation[loc]++;
                numPresentations++;
                secondSeen = p.seenObject.seen(stimValue, p.threshold[loc], numPresentations, 0, 0);
            }

            if (!firstSeen || !secondSeen)
            {
                debugWrite("SupraThreshold: did not get both.\n");

                stats.measuredThreshold[loc] = doZest(p, loc);

                if (stats.measuredThreshold[loc] < 0)
                    stats.measuredThreshold[loc] = 0;
            } else {
                //stats.measuredThreshold[loc] = stimValue;
                if (stimulus.numLocations == 41)
                    stats.measuredThreshold[loc] = p.threshold[loc];  // for thrash graphs
                else
                    stats.measuredThreshold[loc] = -1; 

            }
        }
    }// end run()
    
    private int doZest(Patient p, int loc)
    {
       double localPdf[] = (double [])pdf[loc].clone();

       debugWrite("Pdf = ");
       for(int j = 0 ; j < localPdf.length; j++)
           debugWrite(localPdf[j]+" ");
       debugWrite("\n");

       debugWrite("EMU: mean = " + mean(localPdf)+"\n");
       debugWrite("      stdev = " + stdev(localPdf)+"\n");
    
       do {
           int stimValue = (int)Math.round(mean(localPdf));
           stats.numPresentation[loc]++;
           numPresentations++;
        
           if (p.seenObject.seen(stimValue, p.threshold[loc], numPresentations, 0, 0)){
               scalePdf(localPdf, alignMlf(loc, stimValue, INVERT));
               debugWrite("Seen\n");                    
           } else {
               scalePdf(localPdf, alignMlf(loc, stimValue, NO_INVERT));
               debugWrite("Not Seen\n");                    
           }
       
           debugWrite("EMU: next to present mean = " + mean(localPdf));
           debugWrite("                      stdev = " + stdev(localPdf)+"\n");
       
       } while ((((stoppingCondition == NUMBER_OF_PRESENTAIONS) && (stats.numPresentation[loc] < stoppingParam))
             ||  ((stoppingCondition == STANDARD_DEVIATION)     && (stdev(localPdf) > stoppingParam)))
             &&  (stats.numPresentation[loc] < MAX_NUM_PRESENTATIONS));

       return (int)Math.round(mean(localPdf));
    }//doZest()

    /**
        Read in a list of function points from the given file.
        This assumes one function per line, space or tab delimited points.
        Returns the function read as a double[][] (null on error).
        
        @param num    The nunmber of functions.
        @param length The number of points in the domain of the function.
        @param in     The BufferedReader to read from.
    */
    public double[][] readDiscreteFunctions(int num, int length, BufferedReader in) {
        int numRead=0, numPoints=0;    // loop variables
        final double function[][] = new double[num][length];
        try {
            for(numRead = 0 ; numRead < num ; numRead++) {
                String line = in.readLine();
if (line == null) System.err.println("Shit, where's my line "+numRead);
                StringTokenizer t = new StringTokenizer(line, " \t");
                for (numPoints = 0; numPoints < length; numPoints++) {
                    function[numRead][numPoints] = Double.parseDouble(t.nextToken());
                }
            }
            in.close();
        } catch (IOException e) {
            System.out.println("Trouble reading a discrete function " + e);
            return null;
        } catch (NoSuchElementException e) {
            System.out.println("The discrete function file is in the wrong format:");
            System.out.println("Expecting "+num+" functions each of "+length+" points");
            System.out.println("but got "+numRead+" functions, the last with "+numPoints+" points");
            return null;
        }
        
        return function;
    }// end readDiscreteFunctions()
    
    /**
        Return the expectaion of the points 0..array.length
        It is not necessary for the values of vals to sum to 1, scaling is performed here.
        @param vals vals[i] contains the freqeuncy of i
    */
    private double mean(double vals[]) {
        double sum = 0;
        double weightedSum = 0;
        for(int i = 0 ; i < vals.length ; i++) {
            weightedSum += (i + stimulus.minExcitation) * vals[i];
            sum         += vals[i];
        }
        return weightedSum/sum;
    }// mean()
    
    /**
        Return the standard deviation of the points 0..array.length
        It is not necessary for the values of vals to sum to 1, scaling is performed here.
        
        @param vals vals[i] contains the freqeuncy of i
    */
    private double stdev(double vals[]) {
        double sum = 0;
        double weightedSum = 0;
        double weightedSumSqr = 0;
        
        for(int i = 0 ; i < vals.length ; i++) {
            weightedSum    += (i + stimulus.minExcitation) * vals[i];
            weightedSumSqr += (i + stimulus.minExcitation) * (i + stimulus.minExcitation) * vals[i];
            sum            += vals[i];
        }
        weightedSum    /= sum;
        weightedSumSqr /= sum;
        
        return Math.sqrt(weightedSumSqr - weightedSum*weightedSum);
    }// stdev()
    
    /**
        Overwrite a[i] with a[i]*b[i] for all i.
        
        @param a Pdf to scale
        @param b Array of scaling factors
    */
    private void scalePdf(double a[], double b[]) {
        int n;
        
        n = (a.length <= b.length) ? a.length : b.length;
        
        if (a.length != b.length)
            System.err.println("Warning: MaxLike.scalePdf is multiplying two arrays that are not the same length");
            
        for(int i = 0 ; i < n ; i++)
            a[i] *= b[i];
    }// multiply()
    
    /**
        Create an array of discrete function values equal to the mlf supplied 
        translated along the x axis so that its mid point aligns with the stimValue.
        If the third parameter says so, invert the function as well.
        
        When the alignment exceeds the domain of the mlf, the end points are extended as 
        far as necessary. ie the new mlf gets as many mlf[index][0]'s on the left and 
        mlf[index][domain-1]'s on the right to fill the new mlf array.
        
        @param index  index into the mlf and mlfAlignmentPoint arrays defining the mlf to align.
        @param value  The stim value to align with (need to take stimulus.minExcitation to get index)
        @param invert Flag to invert the mlf or not.
    */
    private double []alignMlf(int index, int value, boolean invert) {
        value -= stimulus.minExcitation;

        double newMlf[] = new double[domain];
debugWrite("To align mlf: ");for(int z = 0 ; z < mlf[index].length ; z++) debugWrite(mlf[index][z]+" ");debugWrite("\n");        
debugWrite("about "+mlfAlignmentPoint[index]+" "+(invert?"Invert":"No invert")+"\n");

        for(int i = 0 ; i < domain ; i++) {
            int j = i - (value - mlfAlignmentPoint[index]);
            if (j < 0) 
                newMlf[i] = mlf[index][0];
            else if (j >= domain)
                newMlf[i] = mlf[index][domain-1];
            else
                newMlf[i] = mlf[index][j];
        }

debugWrite("b4 invert: ");for(int z = 0 ; z < newMlf.length ; z++) debugWrite(newMlf[z]+" ");debugWrite("\n");
        if (invert)     
            for(int i = 0 ; i < domain ; i++)
                newMlf[i] = 1 - newMlf[i];
debugWrite("mlf= ");for(int z = 0 ; z < newMlf.length ; z++) debugWrite(newMlf[z]+" ");debugWrite("\n");

        return newMlf;
    }//alignMlf()

    public void localFinish() { /* overide with tidy up routines */ ; }
}// end TPMaxLike class
