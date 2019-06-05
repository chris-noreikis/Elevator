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
        partTwo();
        printReport();
        ElevatorDisplay.getInstance().shutdown();
    }

    private void printReport() throws InvalidValueException {
        SimulationReport report = new SimulationReport(people);
        report.printMaxMinWaitTimes();
        report.printRideLogs();
        report.printBuildingReport();
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
