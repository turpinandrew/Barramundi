package Barramundi;
import java.io.*;

/**
** Pump "patients" with threhold==location number through test procedures
*/

class Thrash
{
    public static void main (String args[])
    {
        int numStim = 41;
        StimulusRecord stim = new StimulusRecord();
        stim.fromString("sitm~"+numStim+"~0~40~1~0", "~");

            // test procedures
        int numberOfStartingGuesses = 1;
        int startingGuesses[]       = new int[] {32};

        int numberOfTestProcedures    = 2;
        String tpNames[][]            = new String[numberOfTestProcedures][numberOfStartingGuesses];
        AbstractTestProcedure tps[][] = new AbstractTestProcedure[numberOfTestProcedures][numberOfStartingGuesses];

            // TPAlly
        for(int j = 0 ; j < numberOfStartingGuesses ; j++)
           tpNames[0][j] = new String("TPAlly1 Uni sd=1.5 sg=" + startingGuesses[j]);

        for(int j = 0 ; j < numberOfStartingGuesses ; j ++)
           tps[0][j] = new TPAlly1("PDFUniform.txt", "MLF95_3.txt", 
                                   "STANDARD_DEVIATION", new Double(1.5));

            // Full threshold
       for(int j = 0 ; j < numberOfStartingGuesses ; j++)
           tpNames[1][j] = new String("TPFullThreshold242 sg=" + startingGuesses[j]);
       for(int j = 0 ; j < numberOfStartingGuesses ; j ++)
           tps[1][j]     = new TPFullThreshold242(new Double(startingGuesses[j]));

            // patient sets
        int numberOfPatientSets = 3;
        int numberOfPatients = 100; // per set
        Seen pset[]        = new Seen[numberOfPatientSets];
        String psetNames[] = new String[numberOfPatientSets];
        pset     [0] = SeenFactory.getSeen_G_0(new Double(0.00),new Double(0.00));
        pset     [1] = SeenFactory.getSeen_G_1(new Double(0.15),new Double(0.15));
        pset     [2] = SeenFactory.getSeen_G_2(new Double(0.30),new Double(0.30));
        psetNames[0] = new String (numberOfPatients + " inc sd=0 fp/fn rates=0%");
        psetNames[1] = new String (numberOfPatients + " inc sd=1 fp/fn rates=15%");
        psetNames[2] = new String (numberOfPatients + " inc sd=2 fp/fn rates=30%");


            // the results object for all tps and psets.
        File f = new File("thrash.out");
        String tpHeader[] = new String[numberOfStartingGuesses * numberOfTestProcedures];
        int x = 0;
        for(int t = 0 ; t < numberOfTestProcedures ; t++)
            for(int sg = 0 ; sg < numberOfStartingGuesses ; sg++)
                tpHeader[x++] = tpNames[t][sg];
        PatientResults results = new PatientResults(tpHeader, psetNames, stim, f);

            // loop through each patient set for this test procedure
        x = 0;
        for(int t = 0 ; t < numberOfTestProcedures ; t++)
            for(int sg = 0 ; sg < numberOfStartingGuesses ; sg++)
            {
                AbstractTestProcedure tp = tps[t][sg];
              
                //tp.setup(stim, true, new File("thrash.log")); // true = debug
                tp.setup(stim, false, null);
              
                System.out.println("Start "+tpNames[t][sg]);
              
                for(int p = 0 ; p < numberOfPatientSets ; p++)
                {
                    System.out.println("\t"+psetNames[p]);
                    
                    Patient pat = new Patient("x", "1", 0, pset[p]);
                    pat.threshold = new double[numStim];

                    for(int i = 0 ; i < numberOfPatients ; i++)
                    {
                        for(int j = 0 ; j < numStim ; j++)
                            pat.threshold[j] = j;
                    
                        tp.run(pat);    
                    
                        results.write(x, p, i, tp.stats, false);  // true = summary only
                    }
                }//patient sets
              
                tp.finish();
                x++;
                tps[t][sg] = null; // help garbage collector?
            }//test procedures

        results.writeSummary();
        results.close();
    }
}
