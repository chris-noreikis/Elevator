package main;

import configuration.ConfigurationException;
import configuration.SimulationConfiguration;
import models.*;
import gui.ElevatorDisplay.Direction;
import gui.ElevatorDisplay;

import java.util.ArrayList;
import java.util.Random;

public class ElevatorTestDriver {
    private int personCounter = 1;
    private ArrayList<Person> people = new ArrayList<>();
    private Random randomObject = new Random(1234);

    public void runTests() throws InterruptedException, InvalidValueException, ConfigurationException {
//        testOne();
//        testTwo();
//        testThree();
//        testFour();
        partTwo();

        SimulationResultsPrinter printer = new SimulationResultsPrinter(people);
        printer.printMaxMinWaitTimes();
        printer.printRideLogs();
        ElevatorDisplay.getInstance().shutdown();
    }

    private void testOne() throws InterruptedException, InvalidValueException {
        for (int time = 0; time < 25; time++) {

            if (time == 0) {
                addPerson(1, 10);
            }

            moveElevators(1000);
        }
    }

    private void testTwo() throws InterruptedException, InvalidValueException {
        for (int time = 0; time < 70; time++) {

            if (time == 0) {
                addPerson(20, 5);
            }

            if (time == 5) {
                addPerson(15, 19);
            }

            moveElevators(1000);
        }
    }

    private void testThree() throws InterruptedException, InvalidValueException {
        for (int time = 0; time < 70; time++) {

            if (time == 0) {
                addPerson(3, 6);
                addPerson(3, 6);
                addPerson(3, 6);
            }

//            if (time == 1) {
//                addPerson(3, 16);
//            }
//
//            if (time == 12) {
//                addPerson(12, 16);
//            }

            moveElevators(1000);
        }
    }

    private void testFour() throws InterruptedException, InvalidValueException {
        for (int time = 0; time < 80; time++) {

            if (time == 0) {
                addPerson(1, 10);
            }

            if (time == 5) {
                addPerson(8, 17);
            }

            if (time == 6) {
                addPerson(1, 9);
            }

            if (time == 32) {
                addPerson(3, 1);
            }

            moveElevators(1000);
        }
    }

    private void partTwo() throws InterruptedException, InvalidValueException, ConfigurationException {
        int simulationDuration = SimulationConfiguration.getInstance().getConfigurationField("simulationDuration");
        int personCreationRate = SimulationConfiguration.getInstance().getConfigurationField("personCreationRate");
        for (int time = 0; time < simulationDuration; time++) {

            if (time % personCreationRate == 0) {
                int startFloor = (int) (randomObject.nextDouble() * Building.getInstance().getNumberOfFloors() + 1);
                int endFloor = (int) (randomObject.nextDouble() * Building.getInstance().getNumberOfFloors() + 1);
                while (endFloor == startFloor) {
                    endFloor = (int) (randomObject.nextDouble() * Building.getInstance().getNumberOfFloors() + 1);
                }
                addPerson(startFloor, endFloor);
            }

            moveElevators(1000);
        }

        while (ElevatorController.getInstance().isOperating()) {
            System.out.println("** Still Operating Elevators **");
            moveElevators(1000);
        }
    }

    private void moveElevators(int time) throws InterruptedException, InvalidValueException {
        ElevatorController.getInstance().moveElevators(time);
        Thread.sleep(time);
    }

    private void addPerson(int start, int end) throws InvalidValueException {
        Direction d = ElevatorDirection.determineDirection(start, end);
        Person p = new Person(start, end, "P" + personCounter++);
        Building.getInstance().addPerson(p);
        people.add(p);
        ElevatorController.getInstance().addElevatorRequest(new ElevatorRequest(d, start), p);
    }
}
