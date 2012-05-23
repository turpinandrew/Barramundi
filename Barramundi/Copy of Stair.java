package Barramundi;

class Stair {
    public int locx;
	public int locy;
	public int locationNumber;
    public int currentStim;
    public int lastSeenStim;
    public int step;
    public int numPresentations;
    public int numReversals;
    public int numTimesMissedZero;
    public int numTimesSeenForty;
    public double averageReversalStim;
    public boolean lastSeen;
	public boolean done;

	public Stair()
	{
        locx = -3;
        locy = -3;
		locationNumber = 0;
        currentStim = 25;
		lastSeenStim = 25;
        step = 4;
		numReversals = 0;
		numPresentations = 0;
        numTimesMissedZero = 0;
		numTimesSeenForty = 0;
        averageReversalStim = 0;
        lastSeen = false;
        done = false;
	}

    public Stair(int x, int y, int locNumber, int thresholdGuess)
	{
        locx = x;
        locy = y;
		locationNumber = locNumber;
        currentStim = thresholdGuess;
		lastSeenStim = thresholdGuess;
        step = 4;
		numReversals = 0;
		numPresentations = 0;
        numTimesMissedZero = 0;
		numTimesSeenForty = 0;
        averageReversalStim = 0;
        lastSeen = false;
        done = false;
    }
}
