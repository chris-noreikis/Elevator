package models;

import java.util.ArrayList;
import java.lang.*;
import java.util.Iterator;

import gui.ElevatorDisplay.Direction;

public class Floor {
    private ArrayList<Person> waitingPersons;
    private ArrayList<Person> donePersons;
    private int floorNumber;

    public Floor(int floorNumber) throws InvalidValueException {
        waitingPersons = new ArrayList<>();
        donePersons = new ArrayList<>();
        setFloorNumber(floorNumber);
    }

    public void addWaitingPerson(Person p) throws InvalidValueException {
        if (waitingPersons.contains(p)) {
            throw new InvalidValueException("Person " + p + " is already waiting to get on floor " + getFloorNumber());
        }
        waitingPersons.add(p);
    }

    public void addDonePeople(ArrayList<Person> people) throws InvalidValueException {
        donePersons.addAll(people);
    }

    public ArrayList<Person> getPeopleTravellingInDirection(Direction d) throws InvalidValueException {
        ArrayList<Person> people = new ArrayList<>();

        Iterator<Person> iter = waitingPersons.iterator();
        while (iter.hasNext()) {
            Person p = iter.next();
            if (p.isDirectionOfTravel(d)) {
                people.add(p);
                iter.remove();
            }
        }

        return people;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public String toString() {
        String output = "";
        output += "   Floor " + floorNumber + " Report \n";
        output += "   waitingPersons: " + waitingPersons.toString() + "\n";
        output += "   donePersons: " + donePersons.toString() + "\n";
        output += "---------------\n";

        return output;
    }

    private void setFloorNumber(int floorNumber) throws InvalidValueException {
        if(floorNumber <= 0) {
            throw new InvalidValueException("Floor number is less than or equal to 0.");
        }
        this.floorNumber = floorNumber;
    }
}
