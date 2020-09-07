package tme3;//: innerclasses/tme3.GreenhouseControls.java
// This produces a specific application of the
// control system, all in a single class. Inner
// classes allow you to encapsulate different
// functionality for each type of event.
// From 'Thinking in Java, 4th ed.' (c) Bruce Eckel 2005
// www.BruceEckel.com. See copyright notice in CopyRight.txt.

/***********************************************************************
 * Adapated for COMP308 Java for Programmer, 
 *		SCIS, Athabasca University
 *
 * Assignment: TME3
 * @author: Steve Leung
 * @date  : Oct 21, 2005
 *
 */

//package tme3;


import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;


public class GreenhouseControls implements Serializable {

  //  private boolean light = false;
//  private boolean water = false;
//  private boolean fans = false;
//  private boolean windowok = true;
//  private boolean poweron = true;
//  private String thermostat = "Day";
  private String eventsFile = "examples1.txt";
  private int errorcode;

  public GreenhouseControls() {

  }

  public Controller c = new Controller(this);


  public Controller getC() {
    return c;
  }


  // List of TwoTuples storing variables
  static List<TwoTuple> variables = new ArrayList<>(); // made variables static


  // Synchornized lock to prevent multiple events from accessing TwoTuple at the same time

  ReentrantLock lock = new ReentrantLock();


  public void setVariable(TwoTuple toSet) {

    lock.lock(); // locks for synchonirzation

    // lock requires try/catch (?)
    try {
      boolean found = false;  // checks if the variable is already stored in the TwoTuple, as we don't want multiple LightOns

      for (TwoTuple t : variables) //for loop that goes through every TwoTuple inside variables
      {
        if (t.first == toSet.first) {
          t.second = toSet.second;
          found = true;
          break;
        }
      }

      if (!found) {
        variables.add(toSet); //adds to the TwoTuple
      }

    } catch (Exception e) {
      e.printStackTrace();
    }


    lock.unlock(); // unlocks when finished

  }


  public void shutdown() {

    if (errorcode == 1) {
      System.out.println("Window Malfunction, Greenhouse Shutting Down");
    }
    if (errorcode == 2) {
      System.out.println("Power Out, Greenhouse Shutting Down");

    }

    // the shutdown method must also serialize the current GreenhouseObject
    serialize(this);
  }

  public void serialize(GreenhouseControls gc) {
    // Serialize and save entire tme3.GreenhouseControls object into file dump.out
    try {
      FileOutputStream f = new FileOutputStream(new File("dump.out"));
      ObjectOutputStream o = new ObjectOutputStream(f);

      // Write Objects to File
      o.writeObject(gc);

      o.close();  // must close the file or nothing gets written
      f.close();
      System.out.println("Serialized Data Saved in dump.out file");

    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    } catch (IOException e) {
      System.out.println("Error initializing stream");
    }

  }


  // Method to return the saved errorCode
  int getError() {
    return errorcode;
  }

  // An example of an action() that inserts a
  // new one of itself into the event list:


  // Create inner classes PowerOn and FixWindow that implement Fixable

  public class PowerOn implements Fixable {
    public void fix() {
      //poweron = true; // turns power on
      TwoTuple<String, Boolean> tt = new TwoTuple<>("PowerOn", true);
      setVariable(tt);
      errorcode = 0; //clears error code
    }

    public void log() throws IOException {
      // logs to a text file called fix.log: time and nature of fix
      File file = new File("fix.log");  // File object to link to fix.log
      FileWriter fw = new FileWriter(file, false);   //FileWriter object fw
      PrintWriter pw = new PrintWriter(fw); //PrintWriter object pw
      pw.println("New Fix: Greenhouse Power Turned Back On " + "time: " + System.currentTimeMillis());
      System.out.println("New Fix: Greenhouse Power Turned Back On " + "time: " + System.currentTimeMillis());
      pw.close();
    }
  }

  public class FixWindow implements Fixable {
    public void fix() {
      // windowok = true; // fixes window problem
      TwoTuple<String, Boolean> tt = new TwoTuple<>("windowok", true);
      setVariable(tt);
      errorcode = 0; //clears error code
    }

    public void log() throws IOException {
      // logs to a text file called fix.log: time and nature of fix
      File file = new File("fix.log");  // File object to link to fix.log
      FileWriter fw = new FileWriter(file, false);   //FileWriter object fw
      PrintWriter pw = new PrintWriter(fw); //PrintWriter object pw
      pw.println("New Fix: Greenhouse Window Repaired " + "time: " + System.currentTimeMillis());
      System.out.println("New Fix: Greenhouse Window Repaired " + "time: " + System.currentTimeMillis());
      pw.close();
    }
  }

  Fixable getFixable(int errorcode) {
    if (errorcode == 1) {
      errorcode = 0;
      return (Fixable) new FixWindow();
    } else {
      errorcode = 0;
      return (Fixable) new PowerOn();
    }
  }

  public static class Restore {


    GreenhouseControls gc1;


    void deserialize() {  // Deserializes based on Serializeable interface

      try {
        FileInputStream fileIn = new FileInputStream("dump.out");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        gc1 = (GreenhouseControls) in.readObject();
        in.close();
        fileIn.close();
      } catch (IOException i) {
        i.printStackTrace();
        return;
      } catch (ClassNotFoundException c) {
        System.out.println("tme3.GreenhouseControls not found");
        c.printStackTrace();
        return;
      }
    }


    void systemStatus() {  // Prints the status of every variable that was deserialized from dump.out
      for (int i = 0; i < variables.size(); i++) {
        Arrays.toString(variables.toArray());   // from Stack Overflow, will it work?
        System.out.println("Inside systemStatus()");
      }
    }

    void repairSystem() {
      gc1.getFixable(gc1.getError()); // repairs the system depending on the errorcode
    }

    void restoreEvents() {

      EventClasses ec = new EventClasses(0, gc1);
      EventClasses.Restart r = ec.new Restart(0, gc1.eventsFile, gc1); // new events file for gc1
      r.action(gc1); // adds events to gc1
      // System.out.println("gc1 event list size: " + gc1.eventList.size());  // checks if events were inserted correctly
    }

    void systemContinue() {
      //System.out.println(gc1.eventsFile); // checks for proper event file
      gc1.c.run(); // runs the events that were added to gc1.eventsFile
    }


    // the gc1 deserialized object is only supposed to run from after the malfunction
    // this method checks for where the malfunction was, and only adds events from after that
    public void cutOldEvents() {

      EventClasses ec = new EventClasses(0, gc1);

      List<Event> restoreList = new ArrayList<>();
      Boolean restore = false;


      // interates through controller eventList looking for malfunction events
      for (int i = 0; i < gc1.c.unstartedEvents.size(); i++) {
        if (gc1.c.unstartedEvents.get(i) instanceof EventClasses.WindowMalfunction || gc1.c.unstartedEvents.get(i) instanceof EventClasses.PowerOut) {
          restore = true;
          // System.out.println("Before Continue");
          continue;
        }
        if (restore) {
          restoreList.add(gc1.c.unstartedEvents.get(i));
        }
      }
      gc1.c.unstartedEvents.clear();
      gc1.c.unstartedEvents.addAll(restoreList);
      // System.out.println("REstore list size " + restoreList.size());
    }


  }


  public static void printUsage() {
    System.out.println("Correct format: ");
    System.out.println("  java tme3.GreenhouseControls -f <filename>, or");
    System.out.println("  java tme3.GreenhouseControls -d dump.out");
  }

  //---------------------------------------------------------
  public static void main(String[] args) {
    //try {

    //String option = args[0];
    //String filename = args[1];

//      String option = "-f";
//     String filename = "/Users/patrickdrummond/Desktop/TME_4/tme3Start/examples1.txt";
//
//	    if ( !(option.equals("-f")) && !(option.equals("-d")) ) {
//		System.out.println("Invalid option");
//		printUsage();
//	    }
//
//
    GreenhouseControls gc = new GreenhouseControls();
//        EventClasses ec = new EventClasses(0, gc);
//
//	    if (option.equals("-f"))  {
//		gc.c.addEvent(ec.new Restart(0,filename, gc));
//	    }


    gc.c.run();


    // System.out.println("Eventlist Size: " + gc.eventList.size());


//	    if (option.equals("-d")){
//
//        Restore r = new Restore();
//          r.deserialize(); // Deserializes the dump.out back into a tme3.GreenhouseControls object
//          r.systemStatus(); // Prints to the user the status of the GC variables
//          r.repairSystem(); // Repairs the windows or power depending on error code
//          r.restoreEvents();
//          r.cutOldEvents();
//          r.systemContinue();


//        }
//	}
//	catch (ArrayIndexOutOfBoundsException e) {
//	    System.out.println("Invalid number of parameters");
//	    printUsage();
//	}
//    }

  } ///:~
}
