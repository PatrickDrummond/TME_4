//: innerclasses/controller/Event.java
// The common methods for any control event.
// From 'Thinking in Java, 4th ed.' (c) Bruce Eckel 2005
// www.BruceEckel.com. See copyright notice in CopyRight.txt.

/***********************************************************************
 * Adapated for COMP308 Java for Programmer, 
 *		SCIS, Athabasca University
 *
 * Assignment: TME3
 * @author: Steve Leung
 * @date  : Oct. 21, 2006
 *
 * Description: Event abstract class
 *
 */

package tme3;

import java.io.*;

public interface Event extends Runnable {
  /*
  private long eventTime;
  protected final long delayTime;
  public Event(long delayTime) {
    this.delayTime = delayTime;
    start();
  }
   */

  public void start();  // Allows restarting
  //  eventTime = System.currentTimeMillis() + delayTime;

  public boolean ready();
  //  return System.currentTimeMillis() >= eventTime;

  public void action(GreenhouseControls gc);// throws ControllerException, IOException, ControllerException{};


  public void run();
    //System.out.println("Running from inside event run()");

} ///:~
