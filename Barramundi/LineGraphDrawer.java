package Barramundi;

import java.awt.*;
import javax.swing.*;

/**
	Class to handle drawing line graphs in  seperate window and thread
*/
class LineGraphDrawer extends Thread {
	/** Title of the window/frame */ String windowTitle;
	/** Titles of each function   */ String titles[];
	/** The functions to plot     */ double fs[][];
	
	/**
		Create a new window to display the functions supplied.
		
		@param fs 	  Array of discrete functions to draw
		@param titles Array of titles to use for each graph
		@param title  The window title
	*/
	LineGraphDrawer(double fs[][], String titles[], String title) {
		this.fs     = fs;
		this.titles = titles;
		windowTitle = title;
	}
	
	/**
		Create a new window to display the functions supplied.
		
		@param fs 	  Array of discrete functions to draw
		@param titles Array of titles to use for each graph
		@param title  The window title
	*/
	public void run() {
		JFrame frame = new JFrame(windowTitle);
		frame.getContentPane().setLayout(new GridLayout(fs.length/5 + 1,5));
		for(int graph = 0 ; graph < fs.length ; graph++) {
			frame.getContentPane().add(new LineGraphPanel(new Dimension(130,130), fs[graph], titles[graph]));
			yield();
		}
		frame.pack();
		frame.show();
	}
}