package main;

import exceptions.InvalidStateException;
import exceptions.InvalidValueException;
import gui.ElevatorDisplay;
import gui.ElevatorDisplay.Direction;
import models.*;
import java.util.ArrayList;


public class ElevatorTestDriver2 {
    private int personCounter = 1;
    private boolean SIMULATION_MODE = false;
    private boolean TEST_MODE = true;
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

    //@Test
    //@DisplayName("Test case 1 - Single rider request")
    void testOne() throws InterruptedException, InvalidStateException, InvalidValueException {
       for (int time = 0; time < 50; time++) {
           if (time == 0) {
               	  addPerson(1, 10, 1);
               	  Building.getInstance().getFloor(10).getDonePerson(0);
               	  ElevatorDisplay.getInstance().updateElevator(1, 10, 1, ElevatorDisplay.Direction.UP);
               	  Thread.sleep(80);
                  
           }
           
       }
    }

   // @Test
   // @DisplayName("Test case 2 - Pick up rider going in same direction, direction is UP")
    void testTwo() throws InterruptedException, InvalidStateException, InvalidValueException {
        for (int time = 0; time < 70; time++) {

            if (time == 0) {
                addPerson(20, 5, 2);
            }

            if (time == 5) {
                addPerson(15, 19, 2);
            }

            moveElevators(1000);
        }

      //  if (TEST_MODE) {
      //      assertEquals(people.get(0), Building.getInstance().getFloor(5).getDonePerson(0));
      //      assertEquals(people.get(1), Building.getInstance().getFloor(19).getDonePerson(0));
      //  }
    }

    private void moveElevators(int time) throws InterruptedException, InvalidStateException {
        try {
            ElevatorController.getInstance().moveElevators(time);
        } catch (InvalidValueException ex) {
            System.out.println("Bad input data");
            ex.printStackTrace();
        }
        if (SIMULATION_MODE) {
            Thread.sleep(time);
        }
    }

    private void addPerson(int start, int end, int elevId) throws InvalidValueException {
        ElevatorDisplay.Direction d = end > start ? ElevatorDisplay.Direction.UP : ElevatorDisplay.Direction.DOWN;
        Person p = new Person(start, end, "P" + personCounter++);
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
