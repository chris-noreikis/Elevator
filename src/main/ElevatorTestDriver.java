package main;

import gui.ElevatorDisplay;
import models.Building;
import models.ElevatorController;
import models.Person;

public class ElevatorTestDriver {
    int personCounter = 1;

    void runTests() throws InterruptedException {
        Building.getInstance();
        ElevatorController.getInstance();
        testOne();
    }

    private void testOne() throws InterruptedException {
        for (int i = 0; i < 40; i++) {

            if (i == 0) {
                addPerson(1, 10, 1);
            }

            ElevatorController.getInstance().moveElevators(1000);
            Thread.sleep(1000);
        }

        ElevatorDisplay.getInstance().shutdown();
    }

    private void addPerson(int start, int end, int elevId) {
        ElevatorDisplay.Direction d = end > start ? ElevatorDisplay.Direction.UP : ElevatorDisplay.Direction.DOWN;
        Person p = new Person(start, end,"P" + personCounter++);
        Building.getInstance().addPerson(p, start);
    }
}
