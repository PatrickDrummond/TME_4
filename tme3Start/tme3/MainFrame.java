package tme3;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.border.Border;

public class MainFrame extends JFrame {
    public MainFrame(String title) {
        super(title);


        // Set layout manager
        setLayout(new BorderLayout());

        // Create Swing components
        JTextArea textArea = new JTextArea();

        // Create set of 5 Buttons
        JButton buttonStart = new JButton("Start");
        JButton buttonRestart = new JButton("Restart");
        JButton buttonTerminate = new JButton("Terminate");
        JButton buttonSuspend = new JButton("Suspend");
        JButton buttonResume = new JButton("Resume");


        // Add Swing components to content pane
        Container c = getContentPane();
        c.add(buttonStart, BorderLayout.NORTH);
        c.add(buttonRestart, BorderLayout.SOUTH);
        c.add(buttonTerminate, BorderLayout.EAST);
        c.add(buttonSuspend, BorderLayout.WEST);
        c.add(buttonResume, BorderLayout.CENTER);

        c.add(textArea, BorderLayout.CENTER);

        // Add behavior
        buttonStart.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.append("Hello\n");
                // Controller.States = NotReady;
            }
        });
    }
}
