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


public class GreenhouseControls extends Controller implements Serializable, Runnable {
  private boolean light = false;
  private boolean water = false;
  private boolean fans = false;
  private boolean windowok = true;
  private boolean poweron = true;
  private String thermostat = "Day";
  private String eventsFile = "examples1.txt";
  private int errorcode;

  // TODO: make a constructor for GreenhouseController

  GreenhouseControls(){

  }

  public Controller c = new Controller();



  public Controller getC() {
    return c;
  }



  // List of TwoTuples storing variables
  List<TwoTuple> variables = new ArrayList<>();


  // Synchornized lock to prevent multiple events from accessing TwoTuple at the same time

  ReentrantLock lock = new ReentrantLock();



  public void setVariable(TwoTuple toSet){

    lock.lock(); // locks for synchonirzation

    // lock requires try/catch (?)
    try {
      boolean found = false;  // checks if the variable is already stored in the TwoTuple, as we don't want multiple LightOns

      for (TwoTuple t: variables) //for loop that goes through every TwoTuple inside variables
              {
                if (t.first == toSet.first){
                  t.second = toSet.second;
                  found = true;
                  break;
                }
        }

      if (!found){
        variables.add(toSet); //adds to the TwoTuple
      }

    } catch (Exception e) {
      e.printStackTrace();
    }


    lock.unlock(); // unlocks when finished

    }






  @Override
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
  int getError(){
    return errorcode;
  }

  // An example of an action() that inserts a
  // new one of itself into the event list:


  // Create inner classes PowerOn and FixWindow that implement Fixable

  public class PowerOn implements Fixable {
    public void fix() {
      poweron = true; // turns power on
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
      windowok = true; // fixes window problem
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

  Fixable getFixable(int errorcode){
    if (errorcode == 1){
      errorcode = 0;
      return (Fixable) new FixWindow();
    }
    else {
      errorcode = 0;
      return (Fixable) new PowerOn();
    }
  }

  public static class Restore {


    GreenhouseControls gc1;

     void deserialize(){  // Deserializes based on Serializeable interface

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


    void systemStatus(){  // Prints the status of every variable that was deserialized from dump.out
      System.out.println("Light Status: " + gc1.light);
      System.out.println("Water Status: " + gc1.water);
      System.out.println("Fans Status: " + gc1.fans);
      System.out.println("Thermostat Status: " + gc1.thermostat);
      System.out.println("Power Status: " + gc1.poweron);
      System.out.println("Window Status: " + gc1.windowok);
      System.out.println("Errorcode: " + gc1.errorcode);
    }

    void repairSystem(){
       gc1.getFixable(gc1.getError()); // repairs the system depending on the errorcode
    }

    void restoreEvents(){
      Restart r = gc1.new Restart(0, gc1.eventsFile); // new events file for gc1
      r.action(); // adds events to gc1
      // System.out.println("gc1 event list size: " + gc1.eventList.size());  // checks if events were inserted correctly
    }

    void systemContinue(){
    //System.out.println(gc1.eventsFile); // checks for proper event file
      gc1.run(); // runs the events that were added to gc1.eventsFile
    }


    // the gc1 deserialized object is only supposed to run from after the malfunction
    // this method checks for where the malfunction was, and only adds events from after that
    public void cutOldEvents(){
      List<Event> restoreList = new ArrayList<>();
      Boolean restore = false;


      for (int i = 0; i < gc1.eventList.size(); i++){
        if (gc1.eventList.get(i) instanceof WindowMalfunction || gc1.eventList.get(i) instanceof PowerOut){
          restore = true;
         // System.out.println("Before Continue");
          continue;
        }
        if(restore){
          restoreList.add(gc1.eventList.get(i));
        }
      }
      gc1.eventList.clear();
      gc1.eventList.addAll(restoreList);
     // System.out.println("REstore list size " + restoreList.size());
    }


  }




  public class Terminate extends Event {
    public Terminate(long delayTime) { super(delayTime); }
    public void action() { System.exit(0); }
    public String toString() { return "Terminating";  }
  }


  public static void printUsage() {
    System.out.println("Correct format: ");
    System.out.println("  java tme3.GreenhouseControls -f <filename>, or");
    System.out.println("  java tme3.GreenhouseControls -d dump.out");
  }

//---------------------------------------------------------
    public static void main(String[] args) {
	try {
	    String option = args[0];
	    String filename = args[1];

	    if ( !(option.equals("-f")) && !(option.equals("-d")) ) {
		System.out.println("Invalid option");
		printUsage();
	    }

	    GreenhouseControls gc = new GreenhouseControls();

	    if (option.equals("-f"))  {
		gc.addEvent(gc.new Restart(0,filename));
	    }

	    gc.run();

        EventClasses e1 = new EventClasses(1000);

      System.out.println("Eventlist Size: " + gc.eventList.size());



	    Thread t1 = new Thread(e1) {
          public void run() {
            e1.new LightOn(1000).run();
            System.out.println(gc.light);
            e1.new LightOff(1000).run();
            System.out.println(gc.light);
            System.out.println("Running From t1");

          }
        };



	    t1.start();



	    if (option.equals("-d")){

        Restore r = new Restore();
          r.deserialize(); // Deserializes the dump.out back into a tme3.GreenhouseControls object
          r.systemStatus(); // Prints to the user the status of the GC variables
          r.repairSystem(); // Repairs the windows or power depending on error code
          r.restoreEvents();
          r.cutOldEvents();
          r.systemContinue();







        }
	}
	catch (ArrayIndexOutOfBoundsException e) {
	    System.out.println("Invalid number of parameters");
	    printUsage();
	}
    }

} ///:~
