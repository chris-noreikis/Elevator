package main;

import models.*;
import gui.ElevatorDisplay.Direction;
import gui.ElevatorDisplay;
import org.omg.CORBA.DynAnyPackage.Invalid;

import java.util.ArrayList;
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
        for (int time = 0; time < 50; time++) {

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
                addPerson(20, 1);
            }

            if (time == 25) {
                addPerson(10, 1);
            }

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
        for (int time = 0; time < 30; time++) {

            if (time % 3 == 0) {
                int startFloor = (int) (randomObject.nextDouble() * Building.getInstance().getNumberOfFloors() + 1);
                int endFloor = (int) (randomObject.nextDouble() * Building.getInstance().getNumberOfFloors() + 1);
                while (endFloor == startFloor) {
                    endFloor = (int) (randomObject.nextDouble() * Building.getInstance().getNumberOfFloors() + 1);
                }
                addPerson(startFloor, endFloor);
            }

//            if (time % 3 == 0) {
//                int startFloor = (int) (randomObject.nextDouble() * 5 + 1);
//                int endFloor = (int) (randomObject.nextDouble() * 5 + 1);
//                while (endFloor == startFloor) {
//                    endFloor = (int) (randomObject.nextDouble() * 5 + 1);
//                }
//                addPerson(startFloor, endFloor);
//            }

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
        double averageWaitTime = getAverageWaitTime();
        double averageRideTime = getAverageRideTime();
        double minWaitTime = getMinWaitTime();
        double minRideTime = getMinRideTime();
        double maxWaitTime = getMaxWaitTime();
        double maxRideTime = getMaxRideTime();

        System.out.println(String.format("Avg Wait Time: %15.1f", averageWaitTime));
        System.out.println(String.format("Avg Ride Time: %15.1f", averageRideTime));
        System.out.println();

        System.out.println(String.format("Min Wait Time: %15.1f", minWaitTime));
        System.out.println(String.format("Min Ride Time: %15.1f", minRideTime));
        System.out.println();

        System.out.println(String.format("Max Wait Time: %15.1f", maxWaitTime));
        System.out.println(String.format("Max Ride Time: %15.1f", maxRideTime));
        System.out.println();

    }

    private double getAverageWaitTime() {
        return people.stream().mapToDouble(Person::getWaitTime).average().orElse(0.0);
    }

    private double getAverageRideTime() {
        return people.stream().mapToDouble(Person::getRideTime).average().orElse(0.0);
    }

    private double getMinWaitTime() {
        return people.stream().mapToDouble(Person::getWaitTime).min().orElse(0);
    }

    private double getMaxWaitTime() {
        return people.stream().mapToDouble(Person::getWaitTime).max().orElse(0);
    }

    private double getMinRideTime() {
        return people.stream().mapToDouble(Person::getRideTime).min().orElse(0);
    }

    private double getMaxRideTime() {
        return people.stream().mapToDouble(Person::getRideTime).max().orElse(0);
    }

    private void printTable() throws InvalidValueException {
        String string = String.format("%10s %15s %15s %15s %15s %15s %15s",
                "Person", "Start Floor", "End Floor", "Direction", "Wait Time", "Ride Time", "Total Time");
        System.out.println(string);

        string = String.format("%10s %15s %15s %15s %15s %15s %15s",
                "------", "-----------", "---------", "---------", "---------", "---------", "----------");
        System.out.println(string);

        for (Person person : people) {
            Direction d = ElevatorDirection.determineDirection(person.getStartFloor(), person.getEndFloor());
            double totalTime = person.getWaitTime() + person.getRideTime();
            string = String.format("%10s %15d %15d %15s %15.1f %15.1f %15.1f\n",
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
