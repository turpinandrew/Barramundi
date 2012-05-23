package Barramundi;

class Stair {
    public int locx;
	public int locy;
	public int locationNumber;
    public int currentStim;
    public int lastSeenStim;   // used for Full Threshold
    public int step;
    public int numPresentations;
    public int numReversals;
    public int numTimesMissedZero;
    public int numTimesSeenForty;
    public double averageReversalStim;
    public boolean lastSeen;
	public boolean done;
	public int direction;    // direction of previous run that reached numUp or numDown
	public int upCounter;    // number of times up in a row
	public int downCounter;  // number of times down in a row
	public int numUp;        // stop condition
	public int numDown;      // stop condition
	public int numLastReversals;  // number of reversals to average
	public final static char UP      = 0;
	public final static char DOWN    = 1;
	public final static char INITIAL = 2;  // neither up nor down (ie start)

	public Stair()
	{
		direction = INITIAL;
		upCounter = 0;
		downCounter = 0;
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
		this.numUp = 1;
		this.numDown = numDown;
	}

    public Stair(int x, int y, int locNumber, int thresholdGuess, int stepSize, int numUp, int numDown)
	{
		direction = INITIAL;
		upCounter = 0;
		downCounter = 0;
        locx = x;
        locy = y;
		locationNumber = locNumber;
        currentStim = thresholdGuess;
		lastSeenStim = thresholdGuess;
        step = stepSize;
		numReversals = 0;
		numPresentations = 0;
        numTimesMissedZero = 0;
		numTimesSeenForty = 0;
        averageReversalStim = 0;
        lastSeen = false;
        done = false;
		this.numUp = numUp;
		this.numDown = numDown;
    }
}
