package Barramundi;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
	Class for drawing a line graph in a JPanel.  The graph is specified as 
	an array of double, in any units, and is scaled according to the dimension
	supplied.
	
	@author Andrew Turpin (aturpin@discoveriesinsight.org)
	@version 0.1 May 1999
*/
class LineGraphPanel extends JPanel {
	Dimension preferredDim;
    double points[];
	
	/**
		Constructor.
		@param d      The preferred dimension of the bar graph panel.
		@param points Array of points to connect to make a line sorted by x co-ord.
		@param title  Title of the graph.
	*/
	LineGraphPanel(Dimension d, double points[], String title) {
		preferredDim = d;
		this.points = points;

        setBackground(Color.white);
        setForeground(Color.black);
		setBorder(BorderFactory.createTitledBorder(title));
		setVisible(true);
    }

	/**
		Overide the JPanel paint to draw the line graph described by points[].
	*/
    public void paintComponent(Graphics g) {
    	double horiScale, vertScale;
    	
        super.paintComponent(g);  		//paint background
        
        if (points == null) return;
        
        Graphics2D g2 = (Graphics2D)g;

        Insets insets = getInsets();
        int currentWidth  = getWidth()  - insets.left - insets.right;
        int currentHeight = getHeight() - insets.top  - insets.bottom;
        int firstX = insets.left + 3;
        int firstY = insets.top + 3;

      	g2.setPaint(Color.red);        

			// determine verticle scale
		double maxPoint = points[0];
        for(int i = 1 ; i < points.length ; i++)
        	if (maxPoint < points[i])
        		maxPoint = points[i];
        vertScale = (currentHeight)/maxPoint;
        horiScale = (currentWidth)/points.length;
        
        int barWidth = 5;
        for(int i = 1 ; i < points.length ; i++) {
/*		   	
			g2.drawLine(firstX + (int)((i-1)*horiScale), firstY + currentHeight - (int)(points[(int)i-1]*vertScale), 
		   			    firstX + (int)(i*horiScale),     firstY + currentHeight - (int)(points[(int)i]*vertScale));
*/		   	
			g2.fill(new Rectangle2D.Double(
        		firstX + i*barWidth, 
        		firstY + currentHeight - points[i]*vertScale, 
        		barWidth - 1,
	       		points[i]*vertScale));
		  
		}
    }//paintComponent()
    
    /** 
    	Overide JPanel size 
    */
  	public Dimension getPreferredSize() { return preferredDim; }
}
