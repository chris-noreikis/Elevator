package main;

import exceptions.InvalidStateException;
import exceptions.InvalidValueException;
import gui.ElevatorDisplay;
import models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElevatorTestDriver {
    private int personCounter = 1;
    private boolean SIMULATION_MODE = false;
    private boolean TEST_MODE = true;
    private ArrayList<Person> people;

    void runTests() throws InterruptedException, InvalidStateException, InvalidValueException {
        testOne();
        testTwo();
        testThree();
        testFour();
        report();
        ElevatorDisplay.getInstance().shutdown();
    }

    @BeforeEach
    void resetState() throws InvalidValueException {
        Building.getInstance().resetState();
        ElevatorController.getInstance().resetState();
        people = new ArrayList<>();
    }

    @Test
    @DisplayName("Test case 1 - Single rider request")
    void testOne() throws InterruptedException, InvalidStateException, InvalidValueException {
        for (int time = 0; time < 50; time++) {

            if (time == 0) {
                addPerson(1, 10, 1);
            }

            moveElevators(1000);
        }

        if (TEST_MODE) {
            assertEquals(people.get(0), Building.getInstance().getFloor(10).getDonePerson(0));
        }
    }

    @Test
    @DisplayName("Test case 2 - Pick up rider going in same direction, direction is UP")
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

        if (TEST_MODE) {
            assertEquals(people.get(0), Building.getInstance().getFloor(5).getDonePerson(0));
            assertEquals(people.get(1), Building.getInstance().getFloor(19).getDonePerson(0));
        }
    }

    @Test
    @DisplayName("Test case 3 - Pick up rider going in same direction, direction is DOWN")
    void testThree() throws InterruptedException, InvalidStateException, InvalidValueException {
        for (int time = 0; time < 70; time++) {

            if (time == 0) {
                addPerson(20, 1, 3);
            }

            if (time == 24) {
                addPerson(10, 1, 3);
            }

            moveElevators(1000);
        }

        if (TEST_MODE) {
            assertEquals(people.get(0), Building.getInstance().getFloor(1).getDonePerson(0));
            assertEquals(people.get(1), Building.getInstance().getFloor(1).getDonePerson(1));
        }
    }

    @Test
    @DisplayName("Test case 4 - Elevator picks up request when responding to idle timeout, handles multiple up requests")
    void testFour() throws InterruptedException, InvalidStateException, InvalidValueException {
        for (int time = 0; time < 80; time++) {

            if (time == 0) {
                addPerson(1, 10, 1);
            }

            if (time == 5) {
                addPerson(8, 17, 1);
            }

            if (time == 6) {
                addPerson(1, 9, 4);
            }

            if (time == 32) {
                addPerson(3, 1, 4);
            }

            moveElevators(1000);
        }

        if (TEST_MODE) {
            assertEquals(people.get(0), Building.getInstance().getFloor(10).getDonePerson(0));
            assertEquals(people.get(1), Building.getInstance().getFloor(17).getDonePerson(0));
            assertEquals(people.get(2), Building.getInstance().getFloor(9).getDonePerson(0));
            assertEquals(people.get(3), Building.getInstance().getFloor(1).getDonePerson(0));
        }
    }

    @Test
    @DisplayName("Test case 5 - Rider enters down request from 5th floor")
    void testFive() throws InterruptedException, InvalidStateException, InvalidValueException {
        for (int time = 0; time < 40; time++) {
            if (time == 0) {
                addPerson(5, 1, 1);
            }
            moveElevators(1000);
        }

        if (TEST_MODE) {
            assertEquals(people.get(0), Building.getInstance().getFloor(1).getDonePerson(0));
        }
    }

    @Test
    @DisplayName("Test case 6 - Rider enters down request from 5th floor")
    void testSix() throws InterruptedException, InvalidStateException, InvalidValueException {
        for (int time = 0; time < 80; time++) {
            if (time == 0) {
                addPerson(8, 4, 1);
            }

            if (time == 3) {
                addPerson(4, 9, 1);
            }

            if (time == 20) {
                addPerson(5, 15, 1);
            }

            moveElevators(1000);
        }

        if (TEST_MODE) {
            assertEquals(people.get(0), Building.getInstance().getFloor(4).getDonePerson(0));
            assertEquals(people.get(1), Building.getInstance().getFloor(9).getDonePerson(0));
            assertEquals(people.get(2), Building.getInstance().getFloor(15).getDonePerson(0));
        }
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
        people.add(p);
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
