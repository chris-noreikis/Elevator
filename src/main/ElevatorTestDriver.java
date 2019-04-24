package main;

import gui.ElevatorDisplay;
import models.Building;
import models.ElevatorController;
import models.ElevatorRequest;
import models.Person;

public class ElevatorTestDriver {
    int personCounter = 1;

    void runTests() throws InterruptedException {
        Building.getInstance();
        ElevatorController.getInstance();
        testOne();
        testTwo();
        testThree();
        testFour();
        report();
        ElevatorDisplay.getInstance().shutdown();
    }

    private void testOne() throws InterruptedException {
        for (int i = 0; i < 50; i++) {

            if (i == 0) {
                addPerson(1, 10, 1);
            }

            ElevatorController.getInstance().moveElevators(1000);
            Thread.sleep(1000);
        }
    }

    private void testTwo() throws InterruptedException {
        for (int i = 0; i < 70; i++) {

            if (i == 0) {
                addPerson(20, 5, 2);
            }

            if (i == 5) {
                addPerson(15, 19, 2);
            }

            ElevatorController.getInstance().moveElevators(1000);
            Thread.sleep(1000);
        }
    }

    private void testThree() throws InterruptedException {
        for (int i = 0; i < 70; i++) {

            if (i == 0) {
                addPerson(20, 1, 3);
            }

            if (i == 24) {
                addPerson(10, 1, 3);
            }

            ElevatorController.getInstance().moveElevators(1000);
            Thread.sleep(1000);
        }
    }


    private void testFour() throws InterruptedException {
        for (int i = 0; i < 40; i++) {

            if (i == 0) {
                addPerson(1, 6, 1);
            }

            if (i == 22) {
                addPerson(3, 1, 1);
            }

            ElevatorController.getInstance().moveElevators(1000);
            Thread.sleep(1000);
        }
    }

    private void addPerson(int start, int end, int elevId) {
        ElevatorDisplay.Direction d = end > start ? ElevatorDisplay.Direction.UP : ElevatorDisplay.Direction.DOWN;
        Person p = new Person(start, end,"P" + personCounter++);
        Building.getInstance().addPerson(p, start);
        ElevatorController.getInstance().getElevator(elevId).addFloorRequest(new ElevatorRequest(d, start));
        System.out.println("Person " + p.getId() + " pressed " + d + " on Floor " + start);
    }

    private void report() {
        System.out.println("");
        System.out.println(Building.getInstance());
        System.out.println("");
        System.out.println(ElevatorController.getInstance());
    }
}
