package Barramundi;

import java.awt.*;
import javax.swing.*;

/**
	Class to handle drawing bar graphs in seperate window and thread.
	The run(0 method opens a window and draws the bar graphs given 
	in fs[][].
*/
class BarGraphDrawer extends Thread {
	/** Title of the window/frame */ String windowTitle;
	/** Titles of each function   */ String titles[];
	/** The bar graphs to plot    */ double fs[][][];
	
	/**
		Create a new window to display the functions supplied.
		
		@param fs 	  Array of discrete functions to draw
		@param titles Array of titles to use for each graph
		@param title  The window title
	*/
	BarGraphDrawer(double fs[][][], String titles[], String title) {
		this.fs     = fs;
		this.titles = titles;
		windowTitle = title;
        if (titles.length != fs.length) 
            System.err.println("WARNING: not enough titles for bar graphs");
	}
	
	/**
		Create a new window to display the functions supplied.
		
		@param fs 	  Array of discrete functions, where each is an array of arrays of bar heights.
		@param titles Array of titles to use for each graph
		@param title  The window title
	*/
	public void run() {
		JFrame frame = new JFrame(windowTitle);
		frame.getContentPane().setLayout(new GridLayout(fs.length/5 + 1,5));
		for(int graph = 0 ; graph < fs.length ; graph++) {
			frame.getContentPane().add(new BarGraphPanel(new Dimension(130,130), fs[graph], titles[graph]));
			yield();
		}
		frame.pack();
		frame.show();
	}
}
