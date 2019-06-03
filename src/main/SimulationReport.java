package main;

import gui.ElevatorDisplay;
import models.*;

import java.util.ArrayList;
import java.util.Comparator;

public class SimulationReport {
    private ArrayList<Person> people;

    public SimulationReport(ArrayList<Person> peopleIn) throws InvalidValueException {
        setPeople(peopleIn);
    }

    private void setPeople(ArrayList<Person> peopleIn) throws InvalidValueException {
        if (peopleIn == null) {
            throw new InvalidValueException("People cannot be null");
        }

        this.people = peopleIn;
    }

    public void printMaxMinWaitTimes()  {
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

    public void printRideLogs() throws InvalidValueException {
        System.out.println();

        String string = String.format("%6s %15s %15s %15s %15s %15s %15s",
                "Person", "Start Floor", "End Floor", "Direction", "Wait Time", "Ride Time", "Total Time");
        System.out.println(string);

        string = String.format("%6s %15s %15s %15s %15s %15s %15s",
                "------", "-----------", "---------", "---------", "---------", "---------", "----------");
        System.out.println(string);

        for (Person person : people) {
            ElevatorDisplay.Direction d = ElevatorDirection.determineDirection(person.getStartFloor(), person.getEndFloor());
            double totalTime = person.getWaitTime() + person.getRideTime();
            string = String.format("%6s %15d %15d %15s %15.1f %15.1f %15.1f\n",
                    person.getId(), person.getStartFloor(), person.getEndFloor(), d, person.getWaitTime(), person.getRideTime(), totalTime);
            System.out.print(string);
        }
    }

    public void printBuildingReport() throws InvalidValueException {
        System.out.println("");
        System.out.println(Building.getInstance());
        System.out.println("");
        System.out.println(ElevatorController.getInstance());
    }
}
