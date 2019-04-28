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

    public void movePersonFromFloorToElevator(int personIndex, Elevator e) {
        Person personToAdd = waitingPersons.get(personIndex);
        e.pickUpPassenger(personToAdd);
        removeWaitingPerson(personIndex);
    }

    public void addDonePerson(Person p) {
        donePersons.add(p);
    }

    public int getNumberOfWaitingPersons() {
        return waitingPersons.size();
    }

    private void removeWaitingPerson(int personIndex) {
        waitingPersons.remove(personIndex);
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
