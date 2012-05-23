package Barramundi;

/**
** class to hold all parts of a zest procedure for a single location
*/
class Zest {
    public int locx;
	public int locy;
	public int locationNumber;
    public double pdf[];
    public double lf[];

    Zest(int x, int y, int locationNumber, double pdf[], double lf[]) {
        locx = x;
        locy = x;
        this.locationNumber = locationNumber;
        this.pdf = (double [])pdf.clone();
        this.lf  = (double [])lf.clone();
    }
}
