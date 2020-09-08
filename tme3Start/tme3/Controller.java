//: innerclasses/controller/Controller.java
// The reusable framework for control systems.
// From 'Thinking in Java, 4th ed.' (c) Bruce Eckel 2005
// www.BruceEckel.com. See copyright notice in CopyRight.txt.

/***********************************************************************
 * Adapated for COMP308 Java for Programmer, 
 *		SCIS, Athabasca University
 *
 * Assignment: TME3
 * @author: Steve Leung
 * @date  : Oct 21, 2006
 *
 */

package tme3;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.*;
import java.awt.*;
import java.util.List;


public class Controller implements Serializable {

  // States for a FSM
  enum States {
    StartUp,
    NotReady,
    Ready,
    Loading,
    Running,
    Suspended,
    Finished,
    Close
  }

  States state;


  //GUI that belongs to this controller



  // A class from java.util to hold Event objects:
  public List<Event> unstartedEvents = new ArrayList<Event>();
  public List<Event> startedEvents = new ArrayList<Event>();
  public List<Event> finishedEvents = new ArrayList<Event>();

  // A map to tie Event objects with their threads
  public Map<Event, Thread> map = new HashMap<>();


  // list of threads
  public List<Thread> threadList = new ArrayList<>();
  public List<Thread> toJoin = new ArrayList<>();
  private GreenhouseControls gc;
  public void addEvent(Event c) { unstartedEvents.add(c); }
  public void shutdown(){
    System.out.println("Controller Shutdown");
  }

  Controller(GreenhouseControls gc){
    this.gc = gc;
    this.gc.c = this;
     state = States.StartUp;
  }

  public void run() {

    while(true){
      switch (state) {
        case StartUp:
          //initialize GUI
          this.state = States.NotReady;
          break;

        case NotReady:
          //set gui buttons
          //wait for a button to be clicked

          // Placeholder state change for debugging
          this.state = States.Loading;
          break;

        case Loading:

          // Load the events
          try {
            //loadEvents();
          } catch (Exception e){
            state = States.NotReady;
            //make some kind of gui popup
            break;
          } finally {
            state = States.Ready;
          }


        case Ready:

          //set the gui buttons

          // Placeholder state change for debugging purposes
          state = States.Running;
          break;

        case Running:
          //set GUI

          // Sort unstartedEvents list by delayTime
          unstartedEvents.sort(Comparator.comparing(Event::getDelayTime));

          //here we put the running of the events from the list
          for (Event e : unstartedEvents){
            //start a thread
            if(e.ready()) {
              Thread t1 = new Thread(e);
              threadList.add(t1);
              t1.start();
              map.put(e, t1);
            }
            startedEvents.add(e);
           //unstartedEvents.remove(e); //do we need to remove event e? throws ConcurrentModificationException
          }

          //now try join finished events...
          //check if there are any finished events, if so join their threads
          for (Event e : startedEvents) {
              int x = 1;
            if(e.isFinished()){
              unstartedEvents.remove(e);
              // check map for Key e, add corresponded Thread to toJoin
              toJoin.add(map.get(e));
              //remove from threadList
              threadList.remove(map.get(e));
            }

            for (Thread t : toJoin) {
              try {
                t.join();
              } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
              }
            }
          }
          shutdown();
          state = States.Finished;
          break;

        case Finished:
            //set the gui and do nothing
          int x = 1;
          System.exit(3); // placeholder exit for debugging
           break;

        case Suspended:



        case Close:

          //maybe give warning
          //destroy the gui object
          //destroy everything else
      }
    }




//    while (eventList.size() > 0)
//      // Make a copy so you're not modifying the list
//      // while you're selecting the elements in it:
//
//        for (Event e : new ArrayList<Event>(eventList)) {
//          if (e.ready()) {
//            System.out.println(e);
//            try{
//              e.action(gc);
//            } catch (Exception | ControllerException z){
//
//            }
//
//            //Thread t1 = new Thread(e); // make new thread and pass a runnable into the thread
//            //t1.start(); // start the thread
//            eventList.remove(e);
//          }
//        }


        //shutdown();
        //System.exit(1);

  }


  public void start(){
    unstartedEvents.sort(Comparator.comparing(Event::getDelayTime));

    //here we put the running of the events from the list
    for (Event e : unstartedEvents){
      //start a thread
      if(e.ready()) {
        Thread t1 = new Thread(e);
        threadList.add(t1);
        t1.start();
        map.put(e, t1);
      }
      startedEvents.add(e);


      //unstartedEvents.remove(e); //do we need to remove event e? throws ConcurrentModificationException
    }

    //now try join finished events...
    //check if there are any finished events, if so join their threads
    for (Event e : startedEvents) {
      int x = 1;
      if (e.isFinished()) {
        unstartedEvents.remove(e);
        // check map for Key e, add corresponded Thread to toJoin
        toJoin.add(map.get(e));
        //remove from threadList
        threadList.remove(map.get(e));
      }
    }
      for (Thread t : toJoin) {
        try {
          t.join();
        } catch (InterruptedException interruptedException) {
          interruptedException.printStackTrace();
        }
      }
    }


  public void loadEvents(String path){

    // get arugment from GUI
   // String eventsFile = "/Users/patrickdrummond/Desktop/TME_4/tme3Start/examples1.txt"; // placeholder until GUI is made

    // Will eventually get File from GUI
    File myFile = new File(path);

    // New scanner to read input file
    Scanner myReader = null;
    try {
      myReader = new Scanner(myFile);
    } catch (FileNotFoundException fileNotFoundException) {
      fileNotFoundException.printStackTrace();
    }
    while (myReader.hasNextLine()) {
      String data = myReader.nextLine();
      String[] eventsArray;
      eventsArray = data.split(",");
      String eventName = eventsArray[0].split("=")[1];
      String eventTime = eventsArray[1].split("=")[1];
      Long etime = Long.parseLong(eventTime);

      // call getEvent() to get events to add to eventList array

      EventClasses ec = new EventClasses(0, this.gc); // EventClasses object to use methods on
      Event e;

      e = ec.getEvent(eventName, etime);

      // adding event to controller
      gc.c.addEvent(e);

    }
    myReader.close();

    //load events, probably take filename as argument gotten from gui and then take code from restart event
  }

  public static void main(String[] args) {

      SwingUtilities.invokeLater(new Runnable() {

        public void run() {
          JFrame frame = new JFrame("Greenhouse");
          frame.setSize(800,640);
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setVisible(true);

          // Creating the Pulldown MenuBar and adding components
          JMenuBar mb = new JMenuBar();
          JMenu m1 = new JMenu("OPTIONS");
          mb.add(m1);
          JMenuItem m11 = new JMenuItem("New Window");
          JMenuItem m22 = new JMenuItem("Close Window");
          JMenuItem m33 = new JMenuItem("Open Events");
          JMenuItem m44 = new JMenuItem("Restore");
          JMenuItem m55 = new JMenuItem("Exit");
          m1.add(m11);
          m1.add(m22);
          m1.add(m33);
          m1.add(m44);
          m1.add(m55);


          // Creating the buttons panel at bottom and adding components
          JPanel buttonPanel = new JPanel(); // the panel is not visible in output
          JButton start = new JButton("Start");
          JButton reset = new JButton("Reset");
          JButton terminate = new JButton("Terminate");
          JButton suspend = new JButton("Suspend");
          JButton resume = new JButton("Resume");
          buttonPanel.add(start);
          buttonPanel.add(reset);
          buttonPanel.add(terminate);
          buttonPanel.add(suspend);
          buttonPanel.add(resume);

          // Creating the PopUp menu that mirrors the buttonPanel
          JPopupMenu popup = new JPopupMenu();
          JMenuItem startPop = new JMenuItem("Start");
          JMenuItem resetPop = new JMenuItem("Reset");
          JMenuItem terminatePop = new JMenuItem("Terminate");
          JMenuItem suspendPop = new JMenuItem("Suspend");
          JMenuItem resumePop = new JMenuItem("Resume");
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

          // Text Area at the Center
          JTextArea ta = new JTextArea();

          //Adding Components to the frame.
          frame.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
          frame.getContentPane().add(BorderLayout.NORTH, mb);
          frame.getContentPane().add(BorderLayout.CENTER, ta);
          frame.setVisible(true);

          // Gives actions to buttonPanel






        }
      });

    }
} ///:~


