package models;

import gui.ElevatorDisplay.Direction;

import java.util.ArrayList;

public class Floor {
    private ArrayList<Person> waitingPersons;
    private ArrayList<Person> donePersons;
    private int floorNumber;

    public Floor(int floorNumberIn) {
        waitingPersons = new ArrayList<>();
        donePersons = new ArrayList<>();
        floorNumber = floorNumberIn;
    }

    public void addWaitingPerson(Person p) {
        System.out.println("Adding person " + p.getId() + " to floor " + this.floorNumber);
        waitingPersons.add(p);
    }

    public Person getWaitingPerson(int personIndex) {
        return waitingPersons.get(personIndex);
    }

    public void movePersonFromFloorToElevator(int personIndex, Elevator e) {
        Person personToAdd = waitingPersons.get(personIndex);
        e.pickUpPassenger(personToAdd);
        removeWaitingPerson(personIndex);
    }

    public int getNumberOfWaitingPersons() {
        return waitingPersons.size();
    }

    private void removeWaitingPerson(int personIndex) {
        waitingPersons.remove(personIndex);
    }

    public int getFloorNumber() { return floorNumber; }

    public String toString() {
        String output = "";
        output += "Floor " + floorNumber + " Report ...\n";
        output += "waitingPersons: " + waitingPersons.toString() + "\n";

        return output;
    }
}
