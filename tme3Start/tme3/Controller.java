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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Controller {

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

          // move straight to loading for debugging
          this.state = States.Loading;
          break;

        case Loading:
          //load the events

          try {
            loadEvents();
          } catch (Exception e){
            state = States.NotReady;
            //make some kind of gui popup
            break;
          } finally {
            //make sure we dont get here from the catch
            state = States.Ready;
          }


        case Ready:

          //set the gui buttons

          // Placeholder state change for debugging purposes
         // System.out.println("Inside Case Ready; Unstarted EventsList size: " + unstartedEvents.size());
          state = States.Running;
          break;

        case Running:
          //System.out.println("Inside Case Running");
          //set GUI

          //here we put the running of the events from the list
          for (Event e : unstartedEvents){
            //start a thread
            if(e.ready()) {
              Thread t1 = new Thread(e);
              threadList.add(t1);
              t1.start();
              map.put(e, t1);
            }
            //TODO: if we have a list of threads and a list of events, how do we know which event maps to what thread?
            // Use HashMap to link events to their threads
            startedEvents.add(e);
           //unstartedEvents.remove(e); //do we need to remove event e? throws exception
          }

          //now try join finished events...
          //check if there are any finished events, if so join their threads


          //TODO: find a way to give every event a finished attribute/boolean
          for (Event e : startedEvents) {

            if(e.isFinished()){
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

          break;

        case Finished:
            //set the gui and do nothing
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


  private void loadEvents(){

    // get arugment from GUI
   // String eventsFile = "/Users/patrickdrummond/Desktop/TME_4/tme3Start/examples1.txt"; // placeholder until GUI is made

    File myFile = new File("/Users/patrickdrummond/Desktop/TME_4/tme3Start/examples1.txt");

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
      //System.out.println("Name" + eventName);
      //System.out.println("Time" + eventTime);
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
          JFrame frame = new MainFrame("Greenhouse");
          frame.setSize(500,400);
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setVisible(true);
        }
      });

    }
} ///:~


