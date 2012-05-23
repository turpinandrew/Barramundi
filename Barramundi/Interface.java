package Barramundi;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.Border;

/**
	Driver of Barramundi
	Contains main()
	
   	@author Andrew Turpin (aturpin@discoveriesinsight.org)
	@version 0.3 May 1999
*/
public class Interface extends JPanel {
	static int simCount = 0;

    public Interface() {
        JPanel panel = new JPanel();
        JButton buttonNewSimulator = new JButton("New Simulator");
       
		buttonNewSimulator.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Simulator s = new Simulator("Simulator " + simCount++);
        		s.start();
        	}
        });
        
        add(buttonNewSimulator);
    }

	/**
		main()
	*/
    public static void main(String[] args) {
        JFrame frame = new JFrame("Barramundi");

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { ; }
		
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });

        frame.getContentPane().add(new Interface(), BorderLayout.CENTER);
        
        frame.pack();
        frame.setVisible(true);
     }
}
