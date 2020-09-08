package tme3;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingGui {

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

    public SwingGui(Controller c){

        this.c = c;

        //set frame
        frame.setSize(800,640);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //set layout
        frame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);

        //pulldown menu
        mb.add(m1);
        m1.add(m11);
        m11.addActionListener(new NewGreenHouse());
        m1.add(m22);
        m1.add(m33);
        m33.addActionListener(new LoadEvents());
        m1.add(m44);
        m1.add(m55);

        //bottom panel
        buttonPanel.add(start);
        start.addActionListener(new Start());
        buttonPanel.add(reset);
        buttonPanel.add(terminate);
        buttonPanel.add(suspend);
        buttonPanel.add(resume);

        //popup
        popup.add(startPop);
        popup.add(resetPop);
        popup.add(terminatePop);
        popup.add(suspendPop);
        popup.add(resumePop);


    }

    private class NewGreenHouse implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            GreenhouseControls gc = new GreenhouseControls();
            Controller c = new Controller(gc);

            SwingGui GUI = new SwingGui(c);
        }
    }

    public class LoadEvents implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //addd the file chooser

            String fileName = "C:\\Users\\Frank\\IdeaProjects\\TME_4\\tme3Start\\examples1.txt";
            c.loadEvents(fileName);

            //if it succeeds change the state of the gui
            //change the state of the controller

            ta.setText("Events Loaded");

        }
    }

    public class Start implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            c.start();
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
