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
    }

    private void testOne() throws InterruptedException {
        for (int i = 0; i < 50; i++) {

            if (i == 0) {
                addPerson(1, 10, 1);
            }

            if(i == 6) {
                addPerson(5, 6, 1);
            }

            ElevatorController.getInstance().moveElevators(1000);
            Thread.sleep(1000);
        }
        report();
        ElevatorDisplay.getInstance().shutdown();
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
