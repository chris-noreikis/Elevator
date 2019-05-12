package main;

import models.InvalidValueException;
import gui.ElevatorDisplay.Direction;
import gui.ElevatorDisplay;
import models.ElevatorController;
import models.Person;
import models.Building;
import models.ElevatorRequest;

public class ElevatorTestDriver {
    private int personCounter = 1;

    public void runTests() throws InterruptedException, InvalidValueException {
//        testOne();
//        testTwo();
//        testThree();
//        testFour();
        partTwo();

        printReport();
        ElevatorDisplay.getInstance().shutdown();
    }

    private void testOne() throws InterruptedException, InvalidValueException {
        for (int time = 0; time < 50; time++) {

            if (time == 0) {
                addPerson(1, 10, 1);
            }

            moveElevators(1000);
        }
    }

    private void testTwo() throws InterruptedException, InvalidValueException {
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

    private void testThree() throws InterruptedException, InvalidValueException {
        for (int time = 0; time < 70; time++) {

            if (time == 0) {
                addPerson(20, 1, 3);
            }

            if (time == 25) {
                addPerson(10, 1, 3);
            }

            moveElevators(1000);
        }
    }

    private void testFour() throws InterruptedException, InvalidValueException {
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

    private void partTwo() throws InterruptedException, InvalidValueException {
        for (int time = 0; time < 80; time++) {

            if (time == 0) {
                addPerson(1, 14, 1);
            }

            if (time == 4) {
                addPerson(3, 1, 1);
            }

            if (time == 5) {
                addPerson(7, 4, 1);
            }

            if (time == 6) {
                addPerson(5, 2, 1);
            }


            moveElevators(1000);
        }
    }

    private void moveElevators(int time) throws InterruptedException, InvalidValueException {
        ElevatorController.getInstance().moveElevators(time);
        Thread.sleep(time);
    }

    private void addPerson(int start, int end, int elevatorId) throws InvalidValueException {
        Direction d = end > start ? Direction.UP : Direction.DOWN;
        Person p = new Person(start, end, "P" + personCounter++);
        Building.getInstance().addPerson(p);
        ElevatorController.getInstance().addElevatorRequest(new ElevatorRequest(d, start), p);
    }

    private void printReport() throws InvalidValueException {
        System.out.println("");
        System.out.println(Building.getInstance());
        System.out.println("");
        System.out.println(ElevatorController.getInstance());
    }
}
