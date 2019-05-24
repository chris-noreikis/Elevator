package main;

import configuration.SimulationConfiguration;
import models.*;
import gui.ElevatorDisplay.Direction;
import gui.ElevatorDisplay;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class ElevatorTestDriver {
    private int personCounter = 1;
    private ArrayList<Person> people = new ArrayList<>();
    private Random randomObject = new Random(1234);

    public void runTests() throws InterruptedException, InvalidValueException {
//        testOne();
//        testTwo();
//        testThree();
//        testFour();
        partTwo();

        printMaxMinWaitTimes();
        printTable();
//        printReport();
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

    private void partTwo() throws InterruptedException, InvalidValueException {
        int simulationDuration = SimulationConfiguration.getInstance().getSimulationDuration();
        int personCreationRate = SimulationConfiguration.getInstance().getPersonCreationRate();
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

    private void printMaxMinWaitTimes() {
        System.out.println();
        System.out.println("Avg | Min | Max Wait + Ride Times");
        System.out.println("---------------------------------");
        double averageWaitTime = getAverageWaitTime();
        double averageRideTime = getAverageRideTime();
        Person minWaitPerson = getMinWaitPerson();
        Person minRidePerson = getMinRideTime();
        Person maxWaitPerson = getMaxWaitTime();
        Person maxRidePerson = getMaxRideTime();

        System.out.println(String.format("Avg Wait Time: %15.1f", averageWaitTime));
        System.out.println(String.format("Avg Ride Time: %15.1f", averageRideTime));
        System.out.println();

        System.out.println(String.format("Min Wait Time: %15.1f (%s)", minWaitPerson.getWaitTime(), minWaitPerson.getId()));
        System.out.println(String.format("Min Ride Time: %15.1f (%s)", minRidePerson.getRideTime(), minRidePerson.getId()));
        System.out.println();

        System.out.println(String.format("Max Wait Time: %15.1f (%s)", maxWaitPerson.getWaitTime(), maxWaitPerson.getId()));
        System.out.println(String.format("Max Ride Time: %15.1f (%s)", maxRidePerson.getRideTime(), maxRidePerson.getId()));
        System.out.println();

    }

    private double getAverageWaitTime() {
        return people.stream().mapToDouble(Person::getWaitTime).average().orElse(0.0);
    }

    private double getAverageRideTime() {
        return people.stream().mapToDouble(Person::getRideTime).average().orElse(0.0);
    }

    private Person getMinWaitPerson() {
        return people.stream().min(Comparator.comparing(Person::getWaitTime)).orElse(null);
    }

    private Person getMaxWaitTime() {
        return people.stream().max(Comparator.comparing(Person::getWaitTime)).orElse(null);
    }

    private Person getMinRideTime() {
        return people.stream().min(Comparator.comparing(Person::getRideTime)).orElse(null);
    }

    private Person getMaxRideTime() {
        return people.stream().max(Comparator.comparing(Person::getRideTime)).orElse(null);
    }

    private void printTable() throws InvalidValueException {
        System.out.println();
        System.out.println("Person Ride Logs");
        System.out.println("----------------");

        String string = String.format("%6s %15s %15s %15s %15s %15s %15s",
                "Person", "Start Floor", "End Floor", "Direction", "Wait Time", "Ride Time", "Total Time");
        System.out.println(string);

        string = String.format("%6s %15s %15s %15s %15s %15s %15s",
                "------", "-----------", "---------", "---------", "---------", "---------", "----------");
        System.out.println(string);

        for (Person person : people) {
            Direction d = ElevatorDirection.determineDirection(person.getStartFloor(), person.getEndFloor());
            double totalTime = person.getWaitTime() + person.getRideTime();
            string = String.format("%6s %15d %15d %15s %15.1f %15.1f %15.1f\n",
                    person.getId(), person.getStartFloor(), person.getEndFloor(), d, person.getWaitTime(), person.getRideTime(), totalTime);
            System.out.print(string);
        }
    }

    private void printReport() throws InvalidValueException {
        System.out.println("");
        System.out.println(Building.getInstance());
        System.out.println("");
        System.out.println(ElevatorController.getInstance());
    }
}
