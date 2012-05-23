package Barramundi;

/**
	The Gaussian function f(x) = 1/sqrt(2*pi*sd)*(x - m)/sd
*/
class GaussianNoise implements StaticModifier {
	double mean, stdDev;
	
	GaussianNoise(Double m, Double sd) {
		mean=m.doubleValue();stdDev=sd.doubleValue();
	}
	
	public void massage(Patient p) { ;}  // fill this in!!
}