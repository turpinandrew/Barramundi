package Barramundi;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.Border;
import java.util.Vector;
import java.io.*;

/**
     Driver of all simulation runs creates a window in a seperate thread.
    
       @author Andrew Turpin (aturpin@discoveriesinsight.org)
    @version 0.3 May 1999
*/
class Simulator extends Thread { 
    /** stimuli database           */ Stimuli        stimuli;
    /** patient set database       */ PatientSets    patients;
    /** test procedure database    */ TestProcedures testProcedures;
    /** simulator                  */ final Control  control = new Control();

    /** the window                   */ private JFrame frame;
    /** GUI list of stimuli        */ private JList stimuliList;
    /** GUI List of patient sets   */ private JList patientSetList;
    /** GUI List of tet procedures */ private JList testProceduresList;
    
    /** Title of the window           */ private String windowTitle;
    
    /** file to receive results    */ private File   outputFile;
                                      private String defaultFilename = "results.out";
    /** file to write debug info   */ private File   logFile;
                                      private String defaultLogFilename = "barramundi.log";
    Simulator(String s) { 
        windowTitle = s;
        setName("Simulator thread "+s);
        
        frame = new JFrame(windowTitle); 
        
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
              try {        // check to see if databases need writing back to disk
                if (stimuli != null) stimuli.update();
                if (patients != null) patients.update();
                if (testProcedures != null) testProcedures.update();
               } catch (DatabaseException ex) {
                JOptionPane.showMessageDialog(frame,ex.getMessage(),"Barramundi error",JOptionPane.ERROR_MESSAGE);
               }
            }
        });

    }  
        // class to hold button label, actionlistener pairs.
    class MyButton { String text; ActionListener actList; 
                     MyButton(String t, ActionListener a) {text=t; actList=a;}
    };
        // class to hold the three buttons for each of the databases: New, Edit, and Delete
    class MyButtons {
        Database db;
        JList displayList;
        
        MyButtons(Database db, JList displayList) { this.db = db; this.displayList = displayList;}
        
        MyButton[] theButtons = new MyButton[] {
            new MyButton("New",new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    db.addRecord(frame);
                }}),
            new MyButton("Edit",new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    db.editRecord(frame, displayList.getMinSelectionIndex());
                }}),
            new MyButton("Delete",new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    db.deleteRecord(frame, displayList.getMinSelectionIndex());
                }})
        };
    };// MyButtons class
    
    /**
        Create a new window for launching simulation runs
    */
    public void run() {
        JTabbedPane tabbedPane = new JTabbedPane();
        
            // open the three databases
        try {
            stimuli         = new Stimuli("Stimuli.bar");
            patients        = new PatientSets("PatientSets.bar");
            testProcedures = new TestProcedures("TestProcedures.bar");
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(tabbedPane,e.getMessage(),"Barramundi error",JOptionPane.ERROR_MESSAGE);
            return;
        }

            // set up the three lists 
        stimuliList        = new JList(stimuli.list);
        patientSetList     = new JList(patients.list);
        testProceduresList = new JList(testProcedures.list);
        stimuliList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        stimuliList.setSelectedIndex(0);
        patientSetList.setSelectedIndex(0);
        testProceduresList.setSelectedIndex(0);
        
            // array of buttons for the stimulus panel, patients panel, and test procedures panel
        MyButtons stimuliButtons = new MyButtons(stimuli, stimuliList);
        MyButtons patientButtons = new MyButtons(patients, patientSetList);
        MyButtons testProcButtons = new MyButtons(testProcedures, testProceduresList);
                
        tabbedPane.addTab("Stimulus", makePanel(stimuliList, stimuliButtons.theButtons));
        tabbedPane.addTab("Patients", makePanel(patientSetList, patientButtons.theButtons));
        tabbedPane.addTab("Testing procedures", makePanel(testProceduresList, testProcButtons.theButtons));
        tabbedPane.addTab("Control", makeControlPanel());
        
            // Now add the tabbed panes to the frame.
        frame.getContentPane().setLayout(new GridLayout(1, 1)); 
        frame.getContentPane().add(tabbedPane);

        frame.pack(); 
        frame.setVisible(true);
    }
    
    
    /** 
        Layout all the widgets for the Stimulus, Patients, and
        Test Procedures tabbed page    which are a (scrolled) list showing the
        database contents on top of a row of controlling buttons.
        @param v The vector of items initially displayed in the list.
        @param s An array of indicies of selected list items (used when restoring GUI from a saved state).
    */
    JPanel makePanel(JList list, MyButton[] buttons) {
        JScrollPane listScrollPane = new JScrollPane(list);
        
            // set up the row of buttons
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
        for(int i = 0 ; i < buttons.length ; i++) {
            JButton button = new JButton(buttons[i].text);
            button.setHorizontalTextPosition(AbstractButton.CENTER);
            buttonPane.add(button);
            buttonPane.add(Box.createRigidArea(new Dimension(10,0)));
            
            button.addActionListener(buttons[i].actList);
        }

        JPanel thePanel = new JPanel(false);
        thePanel.setLayout(new BorderLayout());
        thePanel.add(listScrollPane, BorderLayout.CENTER);
        thePanel.add(buttonPane, BorderLayout.SOUTH);
        thePanel.setPreferredSize(new Dimension(200,600));

        return thePanel;
    }// makePanel()

    /** 
        Layout all the widgets for the Control Panel.
    */
    Component makeControlPanel() {
        final JPanel thePanel = new JPanel(false);
        
            // set up the debug checkbox
        JCheckBox debugCheckBox = new JCheckBox("Debug");
        debugCheckBox.setSelected(control.debugMode);
        debugCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    control.debugMode = false;
                } else {
                    control.debugMode = true;
                }
            }
        });
        
            // set up the summary-only checkbox
        JCheckBox summaryCheckBox = new JCheckBox("Summary only");
        summaryCheckBox.setSelected(control.summaryOnly);
        summaryCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.DESELECTED) {
                    control.summaryOnly = false;
                } else {
                    control.summaryOnly = true;
                }
        }
        });
        
               // Create the text field displaying the output file name
        final JTextField filenameTextField = new JTextField(15);
        filenameTextField.setText(defaultFilename);
        outputFile = new File(defaultFilename);
        filenameTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outputFile = new File(((JTextField)e.getSource()).getText());
            }
        });
        
               // Create the browse button
        final JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                   final JFileChooser fc = new JFileChooser();
                int returnVal         = fc.showSaveDialog(thePanel);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                       outputFile = fc.getSelectedFile();
                       filenameTextField.setText(outputFile.getName());
                } 
            }
        });
        
               // Create the text field displaying the log file name
        final JTextField logFilenameTextField = new JTextField(15);
        logFilenameTextField.setText(defaultLogFilename);
        logFile = new File(defaultLogFilename);
        logFilenameTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logFile = new File(((JTextField)e.getSource()).getText());
            }
        });
        
               // Create the browse button for the log file
        final JButton browseButton2 = new JButton("Browse");
        browseButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                   final JFileChooser fc = new JFileChooser();
                int returnVal         = fc.showSaveDialog(thePanel);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                       logFile = fc.getSelectedFile();
                       logFilenameTextField.setText(logFile.getName());
                } 
            }
        });
    
               // set up the progress bars in a little panel of their own
        final JPanel barsPanel = new JPanel(false);
          control.testProcsProgressBar  = progressBar("Test Procedures");
          control.patientSetProgressBar = progressBar("Patient Sets");
          control.patientProgressBar    = progressBar("Patients");
        barsPanel.setLayout(new GridLayout(3,1));
          barsPanel.add(control.testProcsProgressBar);
        barsPanel.add(control.patientSetProgressBar);
        barsPanel.add(control.patientProgressBar);
          
            // The Go button loads the patient sets and test procedures 
            // into the simulator, and then calls simulate().        
            // It first checks that it is OK to overwrite an output file.
        final JButton goButton  = new JButton("GO!");
        goButton.setHorizontalTextPosition(AbstractButton.CENTER);
        goButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            try { 
                boolean go = true;
                
                if (outputFile.exists())
                    go = JOptionPane.showConfirmDialog(thePanel,outputFile.getName()+" exists.  Overwrite?","Save results",
                           JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
                           
                if (logFile.exists() && control.debugMode)
                    go = JOptionPane.showConfirmDialog(thePanel,logFile.getName()+" exists.  Overwrite?","Save debug information",
                           JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

                if (go) {
                        // pass the stimulus to the objects that need it.
                    Vector selectedStimuli = stimuli.getSelected(stimuliList.getSelectedIndices());
                    StimulusRecord selectedStimulus = (StimulusRecord)selectedStimuli.elementAt(0);

                        // extract the selected patient data from the list
                    Vector selectedPatientSets = patients.getSelected(patientSetList.getSelectedIndices());
                    Patient patient[][] = new Patient[selectedPatientSets.size()][];
                    for(int i = 0 ; i < selectedPatientSets.size() ; i++)
                        patient[i] = ((PatientSetRecord)selectedPatientSets.elementAt(i)).generatePatients();
    
                        // extract the selected test procedure objects
                    Vector selectedTestProcedures = testProcedures.getSelected(testProceduresList.getSelectedIndices());
                    AbstractTestProcedure abstractTPs[] = new AbstractTestProcedure[selectedTestProcedures.size()];
                    for(int i = 0 ; i < selectedTestProcedures.size() ; i++)
                        abstractTPs[i] = ((TestProcedureRecord)selectedTestProcedures.elementAt(i)).getAbstractTestProcedure();
                    
                        // give the garbage collector a helping hand.
                    selectedStimuli = selectedPatientSets = selectedTestProcedures = null;
                
                        // now go for it...
                    control.loadSimulator(patient, abstractTPs, selectedStimulus, outputFile, logFile);
                    control.start();
                    
                        // turn off the Go button.
                    goButton.setEnabled(false);
                }
            }
            catch (PatientModifierException ex) {
                JOptionPane.showMessageDialog(thePanel,ex.getMessage(),"Barramundi error",JOptionPane.ERROR_MESSAGE);
            }
            catch (DatabaseException ex) { // caused by reading patient data
                JOptionPane.showMessageDialog(thePanel,ex.getMessage(),"Barramundi error",JOptionPane.ERROR_MESSAGE);
            }
            catch (TestProcedureException ex) {
                JOptionPane.showMessageDialog(thePanel,ex.getMessage(),"Barramundi error",JOptionPane.ERROR_MESSAGE);
            }}
        });
        
            // place the filename widgets in a panel
        final JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new GridLayout(2,3));
          optionPanel.add(filenameTextField);
          optionPanel.add(browseButton);
          optionPanel.add(summaryCheckBox);
          optionPanel.add(logFilenameTextField);
          optionPanel.add(browseButton2);
          optionPanel.add(debugCheckBox);

        thePanel.add(optionPanel);
        thePanel.setLayout(new FlowLayout());
        thePanel.add(goButton); //, BorderLayout.CENTER);
        thePanel.add(barsPanel);

        return thePanel;
    }// makeControlPanel()
    
    /**
        Make a horizontal progress bar in a border with title "title".
        @param title Title to place in border around bar.
    */
    JProgressBar progressBar(String title) {
        final Border border = BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder(title),
                                BorderFactory.createLineBorder(Color.black));
        JProgressBar bar = new JProgressBar();
        bar.setValue(0);
        bar.setBorderPainted(true);
        bar.setStringPainted(true);
          bar.setOrientation(JProgressBar.HORIZONTAL);
          bar.setBorder(border);
          
          return bar;
    }//progressBar()
 }
