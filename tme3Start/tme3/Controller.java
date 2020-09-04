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
import java.util.*;


public class Controller {

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

          break;

        case Running:

          //set GUI

          //here we put the running of the events from the list

          for (Event e : unstartedEvents){
            //start a thread
            if(e.ready()) {
              Thread t1 = new Thread(e);
              threadList.add(t1);
              t1.start();

              //TODO: if we have a list of threads and a list of events, how do we know which event maps to what thread?
              //use a map or even a tuple

              startedEvents.add(e);
              unstartedEvents.remove(e);
            }
          }

          //now try join finished events...
          //check if there are any finished events, if so join their threads


          //TODO: find a way to give every event a finished attribute/boolean
          for (Event e : startedEvents) {
            //if the event is finished
            //find its corresponding thread

//            if(finished){
//              //find corresponding thread in threadList
//              //add it to toJoin
//              //remove it from threadList
//            }

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
    //load events, probably take filename as argument gotten from gui and then take code from restart event
  }


} ///:~


