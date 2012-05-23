

package Barramundi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.NoSuchElementException;
import javax.swing.JOptionPane;
import java.lang.Double;
import java.lang.Math;
import java.lang.reflect.Method;
public class TPZest extends AbstractTestProcedure {
                                                 String title;
                                                 BufferedReader npdfFile;
                                                 BufferedReader gpdfFile;
                                                BufferedReader lfFile;
                                                 BufferedReader paramFile;
                                                 double npdf[][];
                                                 double gpdf[][];
                                                 double lf[][];
                                                 double pdfWeight;
                                                 double ageAdjSlope;
                                                 double lfAlignmentPt;
                                                int domain;
                                                 double stoppingParam;
                                                 int stoppingCondition;
                                                 static final int NUMBER_OF_PRESENTATIONS = 1;
                                                 static final int STANDARD_DEVIATION = 2;
                                                 static final int ENTROPY = 3;
                                                 static final int MAXP = 4;
                                                 static final int IQR = 5;
                                     protected final static boolean ALIGN_YES = true;
                                     protected final static boolean ALIGN_NO = false;
                                                   static double PDF_FLOOR = 0.001;
                                                   static int MAX_NUM_PRESENTATIONS = 1000;
    TPZest(String npdfFilename, String gpdfFilename, String lfFilename,
       Double weight, Double slope,
       Double lfAlignmentPt,
           String stopCondition, Double stopP)
    {
        super("ZESTAgeAdj "+npdfFilename+" "+gpdfFilename+" "+
                weight.toString()+" "+slope.toString()+" "+
                lfAlignmentPt+" "+
                lfFilename+" "+stopCondition+" "+stopP);
        title = "ZESTAgeAdj "+npdfFilename+" "+gpdfFilename+" "+
                weight.toString()+" "+slope.toString()+" "+
                lfAlignmentPt.toString()+" "+
            lfFilename+" "+stopCondition+" "+stopP;
    stoppingParam = stopP.doubleValue();
    commonConstructor(npdfFilename,
              gpdfFilename,
              lfFilename,
              weight,
              slope,
              lfAlignmentPt,
              stopCondition);
    }
    public void commonConstructor(String npdfFilename, String gpdfFilename, String lfFilename,
                  Double weight, Double slope,
                  Double lfAlignmentPt,
                  String stopCondition)
    {
        try {
            npdfFile = new BufferedReader(new FileReader(npdfFilename));
            gpdfFile = new BufferedReader(new FileReader(gpdfFilename));
            lfFile = new BufferedReader(new FileReader(lfFilename));
            if (stopCondition.compareTo("NUMBER_OF_PRESENTATIONS") == 0)
                stoppingCondition = NUMBER_OF_PRESENTATIONS;
            else if (stopCondition.compareTo("STANDARD_DEVIATION") == 0)
                stoppingCondition = STANDARD_DEVIATION;
            else if (stopCondition.compareTo("ENTROPY") == 0)
                stoppingCondition = ENTROPY;
            else if (stopCondition.compareTo("MAXP") == 0)
                stoppingCondition = MAXP;
            else if (stopCondition.compareTo("IQR") == 0)
                stoppingCondition = IQR;
            else
                JOptionPane.showMessageDialog(null,"The stopping condition "+stopCondition+" for TPZest is unknown","Barramundi - Error",JOptionPane.ERROR_MESSAGE);
            pdfWeight = weight.doubleValue();
            ageAdjSlope = slope.doubleValue();
            this.lfAlignmentPt = lfAlignmentPt.doubleValue();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Could not find pdf/lf/param file\n" + e,"Barramundi - Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    public void localSetup() {
        domain = (int)((stimulus.maxExcitation - stimulus.minExcitation)/stimulus.stepExcitation) + 1;
        npdf = readDiscreteFunctions(stimulus.numLocations, domain, npdfFile);
        gpdf = readDiscreteFunctions(stimulus.numLocations, domain, gpdfFile);
        lf = readDiscreteFunctions(stimulus.numLocations, domain, lfFile);
        for(int i = 0 ; i < lf.length ; i++)
            for(int j = 0 ; j < lf[i].length / 2 ; j++) {
                double temp = lf[i][j];
                lf[i][j] = lf[i][lf[i].length - 1 - j];
                lf[i][lf[i].length - 1 - j] = temp;
            }
    }
    public void run(Patient p) {
        Vector results = new Vector(stimulus.numLocations);
        Vector finishedResults = new Vector();
        double combinedPdf[][] = makeCombinedPdf(p.age);
        for (int i = 0; i < points.size(); i++) {
            Point point = (Point) points.get(i);
            int x = (int) point.getX();
            int y = (int) point.getY();
            int number = (int)point.getNumber();
            results.add(new Zest(x, y, number, combinedPdf[number], lf[number]));
            stats.numPresentation[number] = 0;
        }
                                                            int stimValue;
                                                            int numPresentations = 0;
        stats.inputThreshold = (double [])p.threshold.clone();
        while (results.size() > 0) {
            int loc = (int) Math.floor(Math.random() * results.size());
            Zest zest = (Zest) results.get(loc);
            int x = zest.locx;
            int y = zest.locy;
            stimValue = (int)Math.round(mean(zest.pdf));
         stats.numPresentation[zest.locationNumber]++;
            numPresentations++;
            if (p.seenObject.seen(stimValue, p.threshold[zest.locationNumber], numPresentations, x, y)){
                scalePdf(zest.pdf, alignLf(zest.lf, stimValue, ALIGN_YES));
            } else {
                scalePdf(zest.pdf, alignLf(zest.lf, stimValue, ALIGN_NO));
            }
            if (((stoppingCondition == NUMBER_OF_PRESENTATIONS) && (stats.numPresentation[zest.locationNumber] >= stoppingParam))
            || ((stoppingCondition == ENTROPY) && (entropy(zest.pdf) <= stoppingParam))
            || ((stoppingCondition == STANDARD_DEVIATION) && (stdev(zest.pdf) <= stoppingParam))
            || ((stoppingCondition == MAXP) && (maxp(zest.pdf) >= stoppingParam))
            || ((stoppingCondition == IQR) && (iqr(zest.pdf) <= stoppingParam))
            || ((stats.numPresentation[zest.locationNumber] >= MAX_NUM_PRESENTATIONS))) {
                stats.measuredThreshold[zest.locationNumber] = Math.round(mean(zest.pdf));
                if (stats.measuredThreshold[zest.locationNumber] < 0)
                    stats.measuredThreshold[zest.locationNumber] = 0;
                finishedResults.add(results.remove(loc));
            }
        }
    }
    public double[][] readDiscreteFunctions(int num, int length, BufferedReader in) {
        int numRead=0, numPoints=0;
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
    }
    private double mean(double vals[]) {
        double sum = 0;
        double weightedSum = 0;
        for(int i = 0 ; i < vals.length ; i++) {
            weightedSum += (i + stimulus.minExcitation) * vals[i];
            sum += vals[i];
        }
        return weightedSum/sum;
    }
    private double stdev(double vals[]) {
        double sum = 0;
        double weightedSum = 0;
        double weightedSumSqr = 0;
        for(int i = 0 ; i < vals.length ; i++) {
            weightedSum += (i + stimulus.minExcitation) * vals[i];
            weightedSumSqr += (i + stimulus.minExcitation) * (i + stimulus.minExcitation) * vals[i];
            sum += vals[i];
        }
        weightedSum /= sum;
        weightedSumSqr /= sum;
        return Math.sqrt(weightedSumSqr - weightedSum*weightedSum);
    }
    private double entropy(double vals[]) {
        double sum = 0;
        double entropy = 0;
        for(int i = 0 ; i < vals.length ; i++)
            sum += vals[i];
        for(int i = 0 ; i < vals.length ; i++) {
            double p = vals[i]/sum;
            entropy -= p * Math.log(p);
        }
        return entropy / Math.log(2.0);
    }
    protected double maxp(double vals[]) {
    double max = 0.0;
    for (int i=0; i< vals.length; i++)
    {
        if (max < vals[i])
        {
        max = vals[i];
        }
    }
    return max;
    }
    protected double iqr(double vals[]) {
    double lo = 0, hi = 0;
    double sum = 0, accum = 0;
        for(int i = 0 ; i < vals.length ; i++)
            sum += vals[i];
        for(int i = 0 ; (i < vals.length ) && (accum < 0.25) ; i++)
    {
        accum += vals[i]/sum;
        lo = i - 1;
    }
    accum = 0;
        for(int i = 0 ; (i < vals.length ) && (accum < 0.75) ; i++)
    {
        accum += vals[i]/sum;
        hi = i - 1;
    }
        return hi - lo;
    }
    private void scalePdf(double a[], double b[]) {
        int n;
        n = (a.length <= b.length) ? a.length : b.length;
        if (a.length != b.length)
            System.err.println("Warning: "+this.getClass().getName()+".scalePdf is multiplying two arrays that are not the same length");
        for(int i = 0 ; i < n ; i++)
            a[i] *= b[i];
    }
    private double []alignLf(double lf[], int value, boolean yes) {
        value -= stimulus.minExcitation;
        double newLf[] = (double [])lf.clone();
        if (!yes) {
            for(int i = 0 ; i < newLf.length ; i++)
                newLf[i] = 1 - newLf[i];
        }
        int alignIndex = -1;
        for(int j = 0 ; (j < newLf.length) && (alignIndex == -1 ); j++)
            if (lf[j] == lfAlignmentPt)
                alignIndex = j;
        if (alignIndex == -1) {
            System.err.println("PANIC!!! Couldn't find "+lfAlignmentPt+" in lf");
            return null;
        }
        if (value > alignIndex) {
            for(int i = newLf.length - 1 ; i >= value - alignIndex ; i--)
                newLf[i] = newLf[i - (value - alignIndex)];
            for(int i = value - alignIndex - 1; i > 0 ; i--)
                newLf[i] = newLf[0];
        } else if (value < alignIndex) {
            for(int i = 0 ; i < newLf.length - 1 - (alignIndex - value) ; i++)
                newLf[i] = newLf[i + (alignIndex - value)];
            for(int i = newLf.length - 1 - (alignIndex - value) ; i < newLf.length ; i++)
                newLf[i] = newLf[newLf.length - 1];
        }
        return newLf;
    }
    double [][]makeCombinedPdf(int age) {
        double cpdf[][] = new double[stimulus.numLocations][domain];
        int slide = (int)Math.round((age-45) * ageAdjSlope);
        for(int loc = 0 ; loc < stimulus.numLocations ; loc++)
            for(int thresh = 0 ; thresh < domain ; thresh++) {
                cpdf[loc][thresh] = gpdf[loc][thresh];
                if ((thresh+slide >= 0) && (thresh+slide < domain))
                    cpdf[loc][thresh] += pdfWeight * npdf[loc][thresh+slide];
            }
        int zeroCount[] = new int[stimulus.numLocations];
        double sum[] = new double[stimulus.numLocations];
        double newSum[] = new double[stimulus.numLocations];
        for(int loc = 0 ; loc < stimulus.numLocations ; loc++) {
            sum[loc] = 0;
            zeroCount[loc] = 0;
            newSum[loc] = 0;
        }
        for(int loc = 0 ; loc < stimulus.numLocations ; loc++)
            for(int thresh = 0 ; thresh < domain ; thresh++)
                sum[loc] += cpdf[loc][thresh];
        for(int loc = 0 ; loc < stimulus.numLocations ; loc++)
            for(int thresh = 0 ; thresh < domain ; thresh++) {
                cpdf[loc][thresh] /= sum[loc];
                if (cpdf[loc][thresh] < PDF_FLOOR) {
                    cpdf[loc][thresh] = 0;
                    zeroCount[loc]++;
                }
            }
        for(int loc = 0 ; loc < stimulus.numLocations ; loc++) {
            for(int thresh = 0 ; thresh < domain ; thresh++) {
                if (cpdf[loc][thresh] == 0)
                    cpdf[loc][thresh] = 1 / (1/PDF_FLOOR - zeroCount[loc]);
            }
            newSum[loc] = 1/(1-zeroCount[loc] * PDF_FLOOR);
        }
        for(int loc = 0 ; loc < stimulus.numLocations ; loc++)
            for(int thresh = 0 ; thresh < domain ; thresh++) {
                cpdf[loc][thresh] /= newSum[loc];
            }
        return cpdf;
    }
    public void localFinish() { ; }
}
