package tme3;
import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwingGui {

    Boolean fileLoaded = false;
    public List<Event> restartEvents = new ArrayList<Event>();

    GreenhouseControls gc;
    Controller c;

    JFrame frame = new JFrame("Greenhouse");

    JPanel buttonPanel = new JPanel(); // the panel is not visible in output
    JButton start = new JButton("Start");
    JButton reset = new JButton("Reset");
    JButton terminate = new JButton("Terminate");
    JButton suspend = new JButton("Suspend");
    JButton resume = new JButton("Resume");

    JMenuBar mb = new JMenuBar();
    JMenu m1 = new JMenu("OPTIONS");
    JMenuItem m11 = new JMenuItem("New Window");
    JMenuItem m22 = new JMenuItem("Close Window");
    JMenuItem m33 = new JMenuItem("Open Events");
    JMenuItem m44 = new JMenuItem("Restore");
    JMenuItem m55 = new JMenuItem("Exit");

    JPopupMenu popup = new JPopupMenu();
    JMenuItem startPop = new JMenuItem("Start");
    JMenuItem resetPop = new JMenuItem("Reset");
    JMenuItem terminatePop = new JMenuItem("Terminate");
    JMenuItem suspendPop = new JMenuItem("Suspend");
    JMenuItem resumePop = new JMenuItem("Resume");

    JTextArea ta = new JTextArea();

    public SwingGui(Controller c) {

        this.c = c;
        this.gc = gc;

        //set frame
        frame.setSize(500, 440);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        //set layout
        frame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);

        //pulldown menu
        mb.add(m1);
        m1.add(m11); // New Window
        m11.addActionListener(new NewGreenHouse());
        m1.add(m22); // Close Window
        m22.addActionListener(new CloseWindow());
        m1.add(m33); // Open Events
        m33.addActionListener(new LoadEvents());
        m1.add(m44); // Restore Events
        m1.add(m55); // Exit
        m55.addActionListener(new Exit());

        //bottom panel
        buttonPanel.add(start);
        start.addActionListener(new Start());
        buttonPanel.add(reset);
        reset.addActionListener(new Reset());
        buttonPanel.add(terminate);
        terminate.addActionListener(new Terminate());
        buttonPanel.add(suspend);
        buttonPanel.add(resume);

        //popup
        popup.add(startPop);
        popup.add(resetPop);
        popup.add(terminatePop);
        popup.add(suspendPop);
        popup.add(resumePop);

        // Mouse listener for PopUp
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }

    //TODO: SUSPEND; RESUME; TERMINATE; RESTORE

    private class NewGreenHouse implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            GreenhouseControls gc = new GreenhouseControls();
            Controller c = new Controller(gc);

            SwingGui GUI = new SwingGui(c);
        }
    }

    public class LoadEvents implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            //TODO: Dubug the file chooser lol
            //Create a file chooser
//            JFileChooser fc = new JFileChooser();
//            int result = fc.showOpenDialog(frame);
//            File file = fc.getSelectedFile();
            //String fileName =

            //In response to a button click:
            //int returnVal = fc.showOpenDialog(m33); // Dont know what this does

            String fileName = "/Users/patrickdrummond/Desktop/TME_4/tme3Start/examples1.txt";
            c.loadEvents(fileName);
            fileLoaded = true;
            //if it succeeds change the state of the gui
            //change the state of the controller

            ta.append("\nEvents Loaded From: " + fileName);
        }
    }

    public class Start implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if (fileLoaded) {
                c.start();
                c.unstartedEvents.clear(); // Could this solve the unstartedEvents problem?
                // Show events on GUI
                for (Event element : c.startedEvents) {
                    ta.append("\n" + element);
                }
                // Show Variable Status on GUI
                ta.append("\nGreenhouse Variable Status" + c.printVariable());

               // ta.append(gc.getVariables().toString());
            } if (!fileLoaded) {
                ta.append("\nError: No File Loaded. Please Load a New File");
            }
        }
    }

        public class Terminate implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                //TODO: what is terminate supposed to even do

                c.unstartedEvents.clear();
                fileLoaded = false;
                // Print what's going on
                ta.append("\nTerminate Action: File Cleared. Please Load A New File");
            }
        }

    public class Reset implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //TODO: Make Reset Work.. Make ta just for your file. Reset() will clear everything


        }
    }

    public class Exit implements ActionListener{
        public void actionPerformed(ActionEvent e) {

            Object[] options = { "Yes", "No" };
            int n = JOptionPane.showOptionDialog(new JFrame(),
                    "Exit Every Greenhouse?", "Exit",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    options, options[1]);
            if(n == JOptionPane.OK_OPTION){ // Afirmative
                //....
                System.exit(0);
            }
            if(n == JOptionPane.NO_OPTION){ // negative
                //....
            }
            if(n == JOptionPane.CLOSED_OPTION){ // closed the dialog
                //....
            }
        }
    }

    public class CloseWindow implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            frame.dispose();
        }
    }


//    SwingUtilities.invokeLater(new Runnable() {
//
//        public void run() {
//            JFrame frame = new MainFrame("Greenhouse SwingGUI");
//            frame.setSize(500,400);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setVisible(true);
//        }
//    });



}
