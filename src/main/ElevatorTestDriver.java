package main;

import exceptions.InvalidStateException;
import exceptions.InvalidValueException;
import gui.ElevatorDisplay.Direction;
import gui.ElevatorDisplay;
import models.*;


public class ElevatorTestDriver {
    private int personCounter = 1;

    void runTests() throws InterruptedException, InvalidStateException, InvalidValueException {
        testOne();
        testTwo();
        testThree();
        testFour();
        report();
        ElevatorDisplay.getInstance().shutdown();
    }

    void resetState() throws InvalidValueException {
        Building.getInstance().resetState();
        ElevatorController.getInstance().resetState();
    }

    void testOne() throws InterruptedException, InvalidStateException, InvalidValueException {
        for (int time = 0; time < 50; time++) {

            if (time == 0) {
                addPerson(1, 10, 1);
            }

            moveElevators(1000);
        }
    }

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
    }

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
    }

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
    }

    void testFive() throws InterruptedException, InvalidStateException, InvalidValueException {
        for (int time = 0; time < 40; time++) {
            if (time == 0) {
                addPerson(5, 1, 1);
            }
            moveElevators(1000);
        }
    }

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
    }

    private void moveElevators(int time) throws InterruptedException, InvalidStateException {
        try {
            ElevatorController.getInstance().moveElevators(time);
        } catch (InvalidValueException ex) {
            System.out.println("Bad input data");
            ex.printStackTrace();
        }
        Thread.sleep(time);
    }

    private void addPerson(int start, int end, int elevId) throws InvalidValueException {
        Direction d = end > start ? ElevatorDisplay.Direction.UP : ElevatorDisplay.Direction.DOWN;
        Person p = new Person(start, end, "P" + personCounter++);
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
