package models;

import exceptions.InvalidValueException;
import gui.ElevatorDisplay.Direction;
import java.util.ArrayList;
import java.lang.*;

public class Floor {
    private ArrayList<Person> waitingPersons;
    private ArrayList<Person> donePersons;
    private int floorNumber;
    private int highestFloor;

    public Floor(int floorNumberIn) throws InvalidValueException {
    	
    	highestFloor = Building.getInstance().getNumberOfFloors();
    	
        if(floorNumberIn < 1) {
        	ElevatorLogger.getInstance().logAction("Floor number less than 1");
            throw new InvalidValueException("Floor number must be 1 or greater"); 
        }
        
        if(floorNumberIn > highestFloor) {
        	 throw new InvalidValueException("Enter a floor number less than "+ floorNumberIn);
        }

        waitingPersons = new ArrayList<>();
        donePersons = new ArrayList<>();
        floorNumber = floorNumberIn;
    }

    public void addWaitingPerson(Person p) {
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

     public static void main(String[] args) {
    	 
    	 try {
    		 Floor f = new Floor(170);
    		 System.out.print(f);
    	 }catch(InvalidValueException e){
    		 e.printStackTrace();
    	  }
       
    }
}
