package Barramundi;

class testMaxLikeAgeAdj {

    public static void main(String argv[]) {

      for(int i = 45 ; i <= 45 ; i++) {
        TPMaxLikeAgeAdj t = new TPMaxLikeAgeAdj(
            "H:\\PSYCHOPHYSICS\\ZEST for QUAD\\PDFNormal45.txt", 
            "H:\\PSYCHOPHYSICS\\ZEST for QUAD\\PDFGlaucoma2.txt", 
            "H:\\PSYCHOPHYSICS\\ZEST for QUAD\\MLF99_3_1.txt", 
            new Double(10), 
            new Double(-0.07),
            "NUMBER_OF_PRESENTATIONS",
             new Double(4));

        t.stimulus = new StimulusRecord("FDT QUAD~54~0.0~20.0~1.0~0.2","~");
        t.debug = false;
        t.stats = null;
        t.localSetup();

        Seen s    = SeenFactory.getSeen_G_0(new Double(0),new Double(0));
        Patient p = new Patient("My tester","1",i,s);
        p.threshold = new double[t.stimulus.numLocations];
        for(int j = 0 ; j < p.threshold.length ; j++)
            p.threshold[j] = 0;

        t.run(p);
        System.out.println("End of pdf for age "+i);
      }
    }
}
