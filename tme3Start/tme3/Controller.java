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
import java.io.IOException;
import java.util.*;


public class Controller {
  // A class from java.util to hold Event objects:
  public List<Event> eventList = new ArrayList<Event>();
  // list of threads
  public List<Thread> threadList = new ArrayList<>();
  private GreenhouseControls gc;
  public void addEvent(Event c) { eventList.add(c); }
  public void shutdown(){
    System.out.println("Controller Shutdown");
  }

  Controller(GreenhouseControls gc){
    this.gc = gc;
  }

  public void run() {
    while (eventList.size() > 0)
      // Make a copy so you're not modifying the list
      // while you're selecting the elements in it:

        for (Event e : new ArrayList<Event>(eventList)) {
          if (e.ready()) {
            System.out.println(e);
            try{
              e.action(gc);
            } catch (Exception | ControllerException z){

            }

            //Thread t1 = new Thread(e); // make new thread and pass a runnable into the thread
            //t1.start(); // start the thread
            eventList.remove(e);
          }
        }


        //shutdown();
        //System.exit(1);

  }


} ///:~


