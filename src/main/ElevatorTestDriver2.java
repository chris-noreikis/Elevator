package main;

import exceptions.InvalidStateException;
import exceptions.InvalidValueException;
import gui.ElevatorDisplay;
import gui.ElevatorDisplay.Direction;
import models.*;
import java.util.ArrayList;


public class ElevatorTestDriver2 {
    private int personCounter = 1;
    private ArrayList<Person> people;

    void runTests() throws InterruptedException, InvalidStateException, InvalidValueException {
        testOne();
      //  testTwo();
      //  testThree();
      //  testFour();
        report();
        ElevatorDisplay.getInstance().shutdown();
    }

   // @BeforeEach
    void resetState() throws InvalidValueException {
        Building.getInstance().resetState();
        ElevatorController.getInstance().resetState();
        people = new ArrayList<>();
    }
    void testOne() throws InterruptedException, InvalidStateException, InvalidValueException {
       for (int time = 0; time < 40; time++) { // This will run for 40 seconds
           if (time == 0) {
               	  addPerson(1, 10, 1);
           }
           
           Building.getInstance().getFloor(10).getDonePerson(0);
           ElevatorDisplay.getInstance().updateElevator(1, 10, 1, ElevatorDisplay.Direction.UP);
           Thread.sleep(1000); 
       }
    }

    private void addPerson(int start, int end, int elevId) throws InvalidValueException {
    	Person p = new Person(start, end, "P" + personCounter++);
        ElevatorDisplay.Direction d = end > start ? ElevatorDisplay.Direction.UP : ElevatorDisplay.Direction.DOWN;
        elevatorRequest(p, start, elevId, d);
    }
    
    private void elevatorRequest(Person p, int start, int elevId, Direction d) throws InvalidValueException{
    	Building.getInstance().addPerson(p, start);
        ElevatorController.getInstance().getElevator(elevId).addFloorRequest(new ElevatorRequest(d, start));
        ElevatorLogger.getInstance().logAction("Person " + p.getId() + " pressed " + d + " on Floor " + start);
    }

    private void report() throws InvalidValueException {
        System.out.println("");
        System.out.println(Building.getInstance());
        System.out.println("");
        System.out.println(ElevatorController.getInstance());
    }
}
