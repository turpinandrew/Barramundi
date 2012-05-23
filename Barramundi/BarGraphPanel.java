package Barramundi;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.lang.Math;

/**
	Class for drawing a bar graph in a JPanel.  
	The graph is specified as an array of arrays of double, in any units, 
	and is scaled according to the dimension supplied.
	Each sub-array is a list of bar heights, and elements are stacked next
	to each other in the graph.  eg A[][] = { {1,2,3},{4,5,6}} will be  a graph
	of three pairs of bars, the first two of height 1 and 4, the next two of 
	height 2 and 5, and the final pair 3 and 6.
	
	@author Andrew Turpin (aturpin@discoveriesinsight.org)
	@version 0.3 June 1999
*/
class BarGraphPanel extends JPanel {
	Dimension preferredDim;
    double points[][];
    double maxPoint;					// highest value in points[][]
    int    maxBars;						// longest array in points[]
   	static final int gapWidth = 2;		// pixel width between bars
	static final Color[] barColor = new Color[] {
		 Color.black , 
		 Color.blue , 
 		 Color.red ,	
		 Color.green , 
		 Color.lightGray , 
		 Color.magenta , 
		 Color.orange ,	
		 Color.pink ,
		 Color.darkGray , 
		 Color.gray ,
		 Color.cyan , 
 		 Color.yellow };
 
	/**
		Constructor.
		@param d      The preferred dimension of the bar graph panel.
		@param points Array of arrays of bar heights.
		@param title  Title of the graph.
	*/
	BarGraphPanel(Dimension d, double points[][], String title) {
		preferredDim = d;
		this.points = points;

        setBackground(Color.white);
        setForeground(Color.black);
		setBorder(BorderFactory.createTitledBorder(title));
		setVisible(true);

		maxPoint = points[0][0];
		maxBars  = 0;
        for(int i = 0 ; i < points.length ; i++) {
        	maxBars = Math.max(maxBars, points[i].length);
        	for(int j = 0 ; j < points[i].length ; j++)
	        	maxPoint = Math.max(maxPoint, points[i][j]);
	    }
    }

	/**
		Overide the JPanel paint to draw the line graph described by points[].
	*/
    public void paintComponent(Graphics g) {
    	double vertScale;
    	double barWidth;
    	
        super.paintComponent(g);  		//paint background
        
        if (points == null) return;
        
        Graphics2D g2 = (Graphics2D)g;

        Insets insets = getInsets();
        int currentWidth  = getWidth()  - insets.left - insets.right;
        int currentHeight = getHeight() - insets.top  - insets.bottom;
        int firstX = insets.left + 3;
        int firstY = insets.top + 3;

			// determine verticle scale
        vertScale = ((double)currentHeight)/maxPoint;
        
        	// determine bar width
	    barWidth = ((currentWidth/maxBars) - gapWidth)/points.length;
        
        for(int i = 0 ; i < points.length ; i++) 
        	for(int j = 0 ; j < points[i].length ; j++) {
	     		g2.setPaint(barColor[i]);

				g2.fill(new Rectangle2D.Double(
    	    		firstX + j*(barWidth*points.length + gapWidth) + i*barWidth, 
        			firstY + currentHeight - points[i][j]*vertScale, 
        			barWidth - 1,
	       			points[i][j]*vertScale));
	       			

/*System.err.println(
points[i][j]+"   "+
vertScale+"   "+
(    	    		firstX + j*(barWidth*points.length + gapWidth) + i*barWidth)
+"  "+
(        			firstY + currentHeight - points[i][j]*vertScale));
*/
			}
    }//paintComponent()
    
    /** 
    	Overide JPanel size 
    */
  	public Dimension getPreferredSize() { return preferredDim; }
}
