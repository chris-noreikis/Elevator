package models;

import exceptions.InvalidValueException;

import java.util.ArrayList;
import java.lang.*;

public class Floor {
    private ArrayList<Person> waitingPersons;
    private ArrayList<Person> donePersons;
    private int floorNumber;

    public Floor(int floorNumberIn) {
//        Building.getInstance().validateFloor(floorNumber);

        waitingPersons = new ArrayList<>();
        donePersons = new ArrayList<>();
        floorNumber = floorNumberIn;
    }

    public void addWaitingPerson(Person p) throws InvalidValueException {
        if (waitingPersons.contains(p)) {
            throw new InvalidValueException("Person " + p + " is already waiting to get on floor " + getFloorNumber());
        }
        waitingPersons.add(p);
    }

    public Person getDonePerson(int personIndex) {
        return donePersons.get(personIndex);
    }

    public void addDonePerson(Person p) throws InvalidValueException {
        if (donePersons.contains(p)) {
            throw new InvalidValueException("Person " + p + " is already on floor " + getFloorNumber());
        }

        donePersons.add(p);
    }

    public int getNumberOfPeopleInLine() {
        return waitingPersons.size();
    }

    public Person getPersonInLine(int spotInLine) { return waitingPersons.get(spotInLine); }

    public void removeWaitingPerson(Person p) throws InvalidValueException {
        if (!waitingPersons.contains(p)) {
            throw new InvalidValueException("Floor " + getFloorNumber() + " does not contain waiting person " + p);
        }

        waitingPersons.remove(p);
    }

    public int getFloorNumber() { return floorNumber; }

    public void resetState() {
        waitingPersons = new ArrayList<>();
        donePersons = new ArrayList<>();
    }

    public String toString() {
        String output = "";
        output += "Floor " + floorNumber + " Report ...\n";
        output += "waitingPersons: " + waitingPersons.toString() + "\n";
        output += "donePersons: " + donePersons.toString() + "\n";
        output += "---------------\n";

        return output;
    }
}
