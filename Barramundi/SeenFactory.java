

package Barramundi;

import java.lang.Math;
import java.util.Random;
public class SeenFactory
{
    private static Random rand = new Random();
    public static Seen_100 getSeen_100(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_100(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue());}
    public static Seen_90u getSeen_90u(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_90u(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue());}
    public static Seen_G_H getSeen_G_HNSize3(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_H(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), -0.066, 2.81);}
    public static Seen_G_H getSeen_G_HGSize3(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_H(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), -0.098, 3.62);}
    public static Seen_G_H getSeen_G_HCSize3(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_H(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), -0.081, 3.27);}
    public static Seen_G_H getSeen_G_HNSize5(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_H(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 0.00, 1.1);}
    public static Seen_G_H getSeen_G_HGSize5(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_H(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), -0.05776, 1.915903);}
    public static Seen_E1 getSeen_E1(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_E1(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue());}
    public static Seen_E2 getSeen_E2(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_E2(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue());}
    public static Seen_E12 getSeen_E12(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_E12(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue());}
public static Seen_G_x getSeen_G_0p0(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 0.0);}
public static Seen_G_x getSeen_G_0p1(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 0.1);}
public static Seen_G_x getSeen_G_0p2(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 0.2);}
public static Seen_G_x getSeen_G_0p3(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 0.3);}
public static Seen_G_x getSeen_G_0p4(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 0.4);}
public static Seen_G_x getSeen_G_0p5(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 0.5);}
public static Seen_G_x getSeen_G_0p6(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 0.6);}
public static Seen_G_x getSeen_G_0p7(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 0.7);}
public static Seen_G_x getSeen_G_0p8(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 0.8);}
public static Seen_G_x getSeen_G_0p9(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 0.9);}
public static Seen_G_x getSeen_G_1p0(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 1.0);}
public static Seen_G_x getSeen_G_1p1(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 1.1);}
public static Seen_G_x getSeen_G_1p2(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 1.2);}
public static Seen_G_x getSeen_G_1p3(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 1.3);}
public static Seen_G_x getSeen_G_1p4(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 1.4);}
public static Seen_G_x getSeen_G_1p5(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 1.5);}
public static Seen_G_x getSeen_G_1p6(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 1.6);}
public static Seen_G_x getSeen_G_1p7(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 1.7);}
public static Seen_G_x getSeen_G_1p8(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 1.8);}
public static Seen_G_x getSeen_G_1p9(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 1.9);}
public static Seen_G_x getSeen_G_2p0(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 2.0);}
public static Seen_G_x getSeen_G_2p1(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 2.1);}
public static Seen_G_x getSeen_G_2p2(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 2.2);}
public static Seen_G_x getSeen_G_2p3(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 2.3);}
public static Seen_G_x getSeen_G_2p4(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 2.4);}
public static Seen_G_x getSeen_G_2p5(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 2.5);}
public static Seen_G_x getSeen_G_2p6(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 2.6);}
public static Seen_G_x getSeen_G_2p7(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 2.7);}
public static Seen_G_x getSeen_G_2p8(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 2.8);}
public static Seen_G_x getSeen_G_2p9(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 2.9);}
public static Seen_G_x getSeen_G_3p0(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 3.0);}
public static Seen_G_x getSeen_G_3p1(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 3.1);}
public static Seen_G_x getSeen_G_3p2(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 3.2);}
public static Seen_G_x getSeen_G_3p3(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 3.3);}
public static Seen_G_x getSeen_G_3p4(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 3.4);}
public static Seen_G_x getSeen_G_3p5(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 3.5);}
public static Seen_G_x getSeen_G_3p6(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 3.6);}
public static Seen_G_x getSeen_G_3p7(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 3.7);}
public static Seen_G_x getSeen_G_3p8(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 3.8);}
public static Seen_G_x getSeen_G_3p9(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 3.9);}
public static Seen_G_x getSeen_G_4p0(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 4.0);}
public static Seen_G_x getSeen_G_4p1(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 4.1);}
public static Seen_G_x getSeen_G_4p2(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 4.2);}
public static Seen_G_x getSeen_G_4p3(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 4.3);}
public static Seen_G_x getSeen_G_4p4(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 4.4);}
public static Seen_G_x getSeen_G_4p5(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 4.5);}
public static Seen_G_x getSeen_G_4p6(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 4.6);}
public static Seen_G_x getSeen_G_4p7(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 4.7);}
public static Seen_G_x getSeen_G_4p8(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 4.8);}
public static Seen_G_x getSeen_G_4p9(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 4.9);}
public static Seen_G_x getSeen_G_5p0(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 5.0);}
public static Seen_G_x getSeen_G_5p1(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 5.1);}
public static Seen_G_x getSeen_G_5p2(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 5.2);}
public static Seen_G_x getSeen_G_5p3(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 5.3);}
public static Seen_G_x getSeen_G_5p4(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 5.4);}
public static Seen_G_x getSeen_G_5p5(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 5.5);}
public static Seen_G_x getSeen_G_5p6(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 5.6);}
public static Seen_G_x getSeen_G_5p7(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 5.7);}
public static Seen_G_x getSeen_G_5p8(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 5.8);}
public static Seen_G_x getSeen_G_5p9(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 5.9);}
public static Seen_G_x getSeen_G_6p0(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 6.0);}
public static Seen_G_x getSeen_G_6p1(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 6.1);}
public static Seen_G_x getSeen_G_6p2(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 6.2);}
public static Seen_G_x getSeen_G_6p3(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 6.3);}
public static Seen_G_x getSeen_G_6p4(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 6.4);}
public static Seen_G_x getSeen_G_6p5(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 6.5);}
public static Seen_G_x getSeen_G_6p6(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 6.6);}
public static Seen_G_x getSeen_G_6p7(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 6.7);}
public static Seen_G_x getSeen_G_6p8(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 6.8);}
public static Seen_G_x getSeen_G_6p9(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 6.9);}
public static Seen_G_x getSeen_G_7p0(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 7.0);}
public static Seen_G_x getSeen_G_7p1(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 7.1);}
public static Seen_G_x getSeen_G_7p2(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 7.2);}
public static Seen_G_x getSeen_G_7p3(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 7.3);}
public static Seen_G_x getSeen_G_7p4(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 7.4);}
public static Seen_G_x getSeen_G_7p5(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 7.5);}
public static Seen_G_x getSeen_G_7p6(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 7.6);}
public static Seen_G_x getSeen_G_7p7(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 7.7);}
public static Seen_G_x getSeen_G_7p8(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 7.8);}
public static Seen_G_x getSeen_G_7p9(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 7.9);}
public static Seen_G_x getSeen_G_8p0(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 8.0);}
public static Seen_G_x getSeen_G_8p1(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 8.1);}
public static Seen_G_x getSeen_G_8p2(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 8.2);}
public static Seen_G_x getSeen_G_8p3(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 8.3);}
public static Seen_G_x getSeen_G_8p4(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 8.4);}
public static Seen_G_x getSeen_G_8p5(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 8.5);}
public static Seen_G_x getSeen_G_8p6(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 8.6);}
public static Seen_G_x getSeen_G_8p7(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 8.7);}
public static Seen_G_x getSeen_G_8p8(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 8.8);}
public static Seen_G_x getSeen_G_8p9(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 8.9);}
public static Seen_G_x getSeen_G_9p0(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 9.0);}
public static Seen_G_x getSeen_G_9p1(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 9.1);}
public static Seen_G_x getSeen_G_9p2(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 9.2);}
public static Seen_G_x getSeen_G_9p3(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 9.3);}
public static Seen_G_x getSeen_G_9p4(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 9.4);}
public static Seen_G_x getSeen_G_9p5(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 9.5);}
public static Seen_G_x getSeen_G_9p6(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 9.6);}
public static Seen_G_x getSeen_G_9p7(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 9.7);}
public static Seen_G_x getSeen_G_9p8(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 9.8);}
public static Seen_G_x getSeen_G_9p9(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_G_x(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue(), 9.9);}
public static Seen_Rus getSeen_Rus(Double falsePositiveRate, Double falseNegativeRate) { return new Seen_Rus(falsePositiveRate.doubleValue(), falseNegativeRate.doubleValue());}
 static class Seen_100 extends Seen {
  Seen_100(double fpr, double fnr) {super(fpr, fnr); }
  public boolean seen(int stimVal, double threshold, int numPresentations, int x, int y) {
            int falseResponse = checkFalseResponse();
   if (falseResponse == +1) return true;
   if (falseResponse == -1) return false;
   return (stimVal < threshold);
  }
 }
 static class Seen_90u extends Seen {
  Seen_90u(double fpr, double fnr) {super(fpr, fnr); }
  public boolean seen(int stimVal, double threshold, int numPresentations, int x, int y) {
            int falseResponse = checkFalseResponse();
   if (falseResponse == +1) return true;
   if (falseResponse == -1) return false;
   return ((rand.nextDouble() < 0.9) && (stimVal < threshold));
  }
 }
        static class Seen_G_x extends Seen
 {
     double m_stDev;
     Seen_G_x(double fpr, double fnr, double stdev)
     {
  super(fpr, fnr);
  m_stDev = stdev;
     }
     public boolean seen(int stimVal, double threshold, int numPresentations, int x, int y)
     {
  int falseResponse = checkFalseResponse();
  if (falseResponse == +1) return true;
  if (falseResponse == -1) return false;
  return GaussianSeen(stimVal, threshold, numPresentations, m_stDev);
     }
 }
        static class Seen_G_H extends Seen
 {
     double m_a;
     double m_b;
     Seen_G_H(double fpr, double fnr, double a, double b)
     {
  super(fpr, fnr);
  m_a = a;
  m_b = b;
     }
     public boolean seen(int stimVal, double threshold, int numPresentations, int x, int y)
     {
  int falseResponse = checkFalseResponse();
  if (falseResponse == +1) return true;
  if (falseResponse == -1) return false;
  double hensonStdDev = Math.min(Math.exp(m_a*threshold + m_b), 5.0);
  return GaussianSeen(stimVal, threshold, numPresentations, hensonStdDev);
     }
 }
 static boolean GaussianSeen(int stimVal, double threshold, int numPresentations, double stdDev) {
  int thisThreshold = (int)Math.round(threshold + stdDev * rand.nextGaussian());
  if (stimVal == thisThreshold)
   return (rand.nextDouble() < 0.5);
  else
   return (stimVal < thisThreshold);
 }
 static class Seen_E1 extends Seen {
  Seen_E1(double fpr, double fnr) {super(fpr, fnr); }
  public boolean seen(int stimVal, double threshold, int numPresentations, int x, int y) {
   boolean s;
   if (stimVal == threshold)
    s = rand.nextDouble() < 0.5;
   else
    s = stimVal < threshold;
   if (numPresentations == 1)
    return !s;
   else
    return s;
  }
 }
 static class Seen_E12 extends Seen {
  Seen_E12(double fpr, double fnr) {super(fpr, fnr); }
  public boolean seen(int stimVal, double threshold, int numPresentations, int x, int y) {
   boolean s;
   if (stimVal == threshold)
    s = rand.nextDouble() < 0.5;
   else
    s = stimVal < threshold;
   if (numPresentations <= 2)
    return !s;
   else
    return s;
  }
 }
 static class Seen_E2 extends Seen {
  Seen_E2(double fpr, double fnr) {super(fpr, fnr); }
  public boolean seen(int stimVal, double threshold, int numPresentations, int x, int y) {
   boolean s;
   if (stimVal == threshold)
    s = rand.nextDouble() < 0.5;
   else
    s = stimVal < threshold;
   if (numPresentations == 2)
    return !s;
   else
    return s;
  }
 }
        static class Seen_Rus extends Seen
 {
     double stDevs[] = { 0.339405253, 1.658132034, 2.694512995, 3.631417067, 4.298012016, 4.832162296,
                            5.231631542, 5.503306132, 5.761346639, 5.970332073, 6.075312276, 5.995712683,
                            6.067060415, 6.05959009, 5.90644053, 5.732019091, 5.429520493, 5.074065784,
                            4.689000989, 4.332392529, 3.888510168, 3.412838557, 2.988729801, 2.598228922,
                            2.237560667, 1.965518368, 1.748990644, 1.559720163, 1.423316166, 1.317721957,
                            1.216192107, 1.116243672, 1.038129913, 1.039963873, 1.107563206, 1.381632837,
                            2.069762167, 2.879488142, 4.259872165, 4.731919864, 4.793199966 };
     Seen_Rus(double fpr, double fnr) { super(fpr, fnr); }
     public boolean seen(int stimVal, double threshold, int numPresentations, int x, int y)
     {
  int falseResponse = checkFalseResponse();
  if (falseResponse == +1) return true;
  if (falseResponse == -1) return false;
        int t = (int)Math.round(threshold);
        if (t < 0)
      return GaussianSeen(stimVal, threshold, numPresentations, stDevs[0]);
        if (t > 40)
      return GaussianSeen(stimVal, threshold, numPresentations, stDevs[40]);
  return GaussianSeen(stimVal, threshold, numPresentations, stDevs[t]);
     }
 }
}
