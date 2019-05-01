package models;

import exceptions.InvalidValueException;
import gui.*;
import java.util.ArrayList;
import java.lang.*;

public class Floor {
    private ArrayList<Person> waitingPersons;
    private ArrayList<Person> donePersons;
    private int floorNumber;

    public Floor(int floorNumber) throws InvalidValueException {
        waitingPersons = new ArrayList<>();
        donePersons = new ArrayList<>();
        setFloorNumber(floorNumber);
    }
    
    private void setFloorNumber(int floorNumber) throws InvalidValueException {
    	if(floorNumber <= 0) {
    		throw new InvalidValueException("Floor number is less than or equal to 0.");
    	}
    	this.floorNumber = floorNumber;
    }

    public void addWaitingPerson(Person p) throws InvalidValueException {
        if (waitingPersons.contains(p)) {
            throw new InvalidValueException("Person " + p + " is already waiting to get on floor " + getFloorNumber());
        }
        waitingPersons.add(p);
    }

    public Person getDonePerson(int personIndex) throws InvalidValueException{
    	try {
    		return donePersons.get(personIndex);
    	}
    	catch(IndexOutOfBoundsException e){
    		throw new InvalidValueException("Person Index out of bounds.");
    	}
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

    public Person getPersonInLine(int spotInLine) throws InvalidValueException{ 
    	try {
    		return waitingPersons.get(spotInLine);
    	}
    	catch(IndexOutOfBoundsException e){
    		throw new InvalidValueException("Spot in line nonexistent.");
    	} 
    }

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
