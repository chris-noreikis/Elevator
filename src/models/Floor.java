package models;

import java.util.ArrayList;
import java.lang.*;

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
        ElevatorLogger.getInstance().logAction("Person " + p.getId() + " has entered Floor " + getFloorNumber());
        waitingPersons.add(p);
    }

    public void addDonePeople(ArrayList<Person> people) throws InvalidValueException {
        if (people == null) {
            throw new InvalidValueException("People cannot be null");
        }

        for (Person p : people) {
            ElevatorLogger.getInstance().logAction("Person " + p.getId() + " has entered Floor " + getFloorNumber());
        }

        donePersons.addAll(people);
    }

    public Person peekNextPerson(Direction d) throws InvalidValueException {
        for (Person p : waitingPersons) {
            if (p.isDirectionOfTravel(d)) {
                return p;
            }
        }

        return null;
    }

    public void removePerson(Person p) throws InvalidValueException {
        if (!waitingPersons.contains(p)) {
            throw new InvalidValueException("Person " + p + " is not waiting on Floor " + getFloorNumber());
        }
        ElevatorLogger.getInstance().logAction("Person " + p.getId() + " has left Floor " + getFloorNumber());
        waitingPersons.remove(p);
    }

    private int getFloorNumber() {
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
