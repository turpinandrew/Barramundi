

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
public class TPSEMU_KS extends AbstractTestProcedure {
                                                   int sp;
                                                   int ap;
                                                                                                   int mss_ap_pass;
                                                                                                   int mss_ap_show;
                                                                                                   int mss_sp_pass;
                                                                                                   int mss_sp_show;
                                                 double stoppingParam;
                                                 int stoppingCondition;
                                                 static final int NUMBER_OF_PRESENTATIONS = 1;
                                                 static final int STANDARD_DEVIATION = 2;
                                                 static final int ENTROPY = 3;
                                                 static final int MAXP = 4;
                                                 static final int IQR = 5;
                                                 int domain;
                                                 String pdfFilename;
                                                 String lfFilename;
                                                 double pdf[];
                                                 double lf[];
                                     protected final static boolean ALIGN_YES = true;
                                     protected final static boolean ALIGN_NO = false;
                                                  static int MAX_NUM_PRESENTATIONS = 1000;
    TPSEMU_KS(Double sp, Double mss_sp_pass, Double mss_sp_show,
              Double ap, Double mss_ap_pass, Double mss_ap_show,
              String pdfFilename, String lfFilename, String stopCondition, Double stopP)
    {
        super("SEMU SP= "+ sp +
                " (" + mss_sp_pass + " of " + mss_sp_show +
                ") AP= "+ ap +
                " (" + mss_ap_pass + " of " + mss_ap_show +
                ") " + pdfFilename + " " + lfFilename+" "+stopCondition+" "+stopP);
        this.sp = (int)Math.round(sp.doubleValue());
        this.ap = (int)Math.round(ap.doubleValue());
        if (sp > ap) {
            System.err.println("SP is bigger than AP, which is not allowed.");
            JOptionPane.showMessageDialog(null,"SP is bigger than AP, which is not allowed.","Barramundi - Error",JOptionPane.ERROR_MESSAGE);
        }
        this.mss_ap_show = (int)Math.round(mss_ap_show.doubleValue());
        this.mss_ap_pass = (int)Math.round(mss_ap_pass.doubleValue());
        this.mss_sp_show = (int)Math.round(mss_sp_show.doubleValue());
        this.mss_sp_pass = (int)Math.round(mss_sp_pass.doubleValue());
        if (mss_sp_pass > mss_sp_show) {
            System.err.println("The mulit-sampling strategy for SP is not valid.");
            JOptionPane.showMessageDialog(null,"The mulit-sampling strategy for SP is not valid.","Barramundi - Error",JOptionPane.ERROR_MESSAGE);
        }
        if (mss_ap_pass > mss_ap_show) {
            System.err.println("The mulit-sampling strategy for AP is not valid.");
            JOptionPane.showMessageDialog(null,"The mulit-sampling strategy for AP is not valid.","Barramundi - Error",JOptionPane.ERROR_MESSAGE);
        }
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
        else {
            JOptionPane.showMessageDialog(null,"The stopping condition "+stopCondition+" for TPSEMU_KS is unknown","Barramundi - Error",JOptionPane.ERROR_MESSAGE);
            System.err.println("The stopping condition "+stopCondition+" for TPSEMU_KS is unknown");
        }
        stoppingParam = stopP.doubleValue();
        this.pdfFilename = pdfFilename;
        this.lfFilename = lfFilename;
    }
    public void localSetup() {
        try {
            BufferedReader pdfFile = new BufferedReader(new FileReader(pdfFilename));
            BufferedReader lfFile = new BufferedReader(new FileReader(lfFilename));
            domain = (int)((stimulus.maxExcitation - stimulus.minExcitation)/stimulus.stepExcitation) + 1;
            pdf = readDiscreteFunction(domain, pdfFile);
            lf = readDiscreteFunction(domain, lfFile);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Could not find pdf/lf/param file\n" + e,"Barramundi - Error",JOptionPane.ERROR_MESSAGE);
        }
        for(int i = 0 ; i < lf.length/2 ; i++) {
                double temp = lf[i];
                lf[i] = lf[lf.length - 1 - i];
                lf[lf.length - 1 - i] = temp;
            }
    }
    public void run(Patient p) {
        double combinedPdf[] = pdf.clone();
        stats.inputThreshold = (double [])p.threshold.clone();
        for (int i = 0; i < points.size(); i++) {
            int numPresentations = 0;
            Point point = (Point) points.get(i);
            int loc = (int)point.getNumber();
            stats.numPresentation[loc] = 0;
            int numShown = 0;
            int numSeen = 0;
            while ((numShown < mss_sp_show) && (numSeen < mss_sp_pass)) {
                numShown++;
             stats.numPresentation[loc]++;
                numPresentations++;
                boolean seen = p.seenObject.seen(sp, p.threshold[loc], numPresentations, point.getX(), point.getY());
                if (seen) {
                    numSeen++;
                    scalePdf(combinedPdf, alignLf(lf, sp, ALIGN_YES));
                } else
                    scalePdf(combinedPdf, alignLf(lf, sp, ALIGN_NO));
            }
            if (numSeen == mss_sp_pass) {
                numShown = 0;
                numSeen = 0;
                while ((numShown < mss_ap_show) && (numSeen < mss_ap_pass)) {
                    numShown++;
                 stats.numPresentation[loc]++;
                    numPresentations++;
                    boolean seen = p.seenObject.seen(ap, p.threshold[loc], numPresentations, point.getX(), point.getY());
                    if (seen) {
                        numSeen++;
                        scalePdf(combinedPdf, alignLf(lf, ap, ALIGN_YES));
                    } else
                        scalePdf(combinedPdf, alignLf(lf, ap, ALIGN_NO));
                }
            } else {
                numSeen = -1;
            }
            if (numSeen != mss_ap_pass) {
                boolean finished = false;
                while (!finished) {
                    int stimValue = (int)Math.round(mean(combinedPdf));
                 stats.numPresentation[loc]++;
                    numPresentations++;
                    boolean seen = p.seenObject.seen(stimValue, p.threshold[loc], numPresentations, point.getX(), point.getY());
                    if (seen) {
                        scalePdf(combinedPdf, alignLf(lf, stimValue, ALIGN_YES));
                    } else {
                        scalePdf(combinedPdf, alignLf(lf, stimValue, ALIGN_NO));
                    }
                    if (((stoppingCondition == NUMBER_OF_PRESENTATIONS) && (stats.numPresentation[loc] >= stoppingParam))
                    || ((stoppingCondition == ENTROPY) && (entropy(combinedPdf) <= stoppingParam))
                    || ((stoppingCondition == STANDARD_DEVIATION) && (stdev(combinedPdf) <= stoppingParam))
                    || ((stoppingCondition == MAXP) && (maxp(combinedPdf) >= stoppingParam))
                    || ((stoppingCondition == IQR) && (iqr(combinedPdf) <= stoppingParam))
                    || ((stats.numPresentation[loc] >= MAX_NUM_PRESENTATIONS))) {
                        stats.measuredThreshold[loc] = Math.round(mean(combinedPdf));
                        finished=true;
                    }
                }
                combinedPdf = null;
            } else {
                stats.measuredThreshold[loc] = ap;
            }
        }
    }
    public double[] readDiscreteFunction(int length, BufferedReader in) {
        int numPoints=0;
        final double function[] = new double[length];
        try {
            String line = in.readLine();
            if (line == null) throw(new NoSuchElementException());
            StringTokenizer t = new StringTokenizer(line, " \t");
            for (numPoints = 0; numPoints < length; numPoints++) {
                function[numPoints] = Double.parseDouble(t.nextToken());
            }
            in.close();
        } catch (IOException e) {
            System.out.println("Trouble reading a discrete function " + e);
            return null;
        } catch (NoSuchElementException e) {
            System.out.println("The discrete function file is in the wrong format:");
            System.out.println("Expecting "+length+" points");
            System.out.println("but got "+numPoints+" points");
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
            if (lf[j] == 0.5)
                alignIndex = j;
        if (alignIndex == -1) {
            System.err.println("PANIC!!! Couldn't find "+0.5+" in lf");
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
    public void localFinish() { ; }
}
