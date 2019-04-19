package models;

import java.util.ArrayList;

public class Floor {
    private ArrayList<Person> waitingPersons;
    private ArrayList<Person> donePersons;
    private int floorNumber;

    public Floor(int floorNumber) {
        waitingPersons = new ArrayList<>();
        donePersons = new ArrayList<>();
        this.floorNumber = floorNumber;
    }

    public void addWaitingPerson(Person p) {
        System.out.println("Adding person " + p.getId() + " to floor " + this.floorNumber);
        waitingPersons.add(p);
    }

    public Person getWaitingPerson(int personIndex) {
        return waitingPersons.get(personIndex);
    }

    public void sendPassengersToElevator(Elevator e) {
        for (int i = 0; i < waitingPersons.size(); i++) {
            Person personToAdd = waitingPersons.remove(i);
            e.pickUpPassenger(personToAdd);
            removeWaitingPerson(i);
        }
    }

    public int getNumberOfWaitingPersons() {
        return waitingPersons.size();
    }

    private void removeWaitingPerson(int personIndex) {
        waitingPersons.remove(personIndex);
    }

    public String toString() {
        String output = "";
        output += "Floor " + floorNumber + " Report ...\n";
        output += "waitingPersons: " + waitingPersons.toString() + "\n";

        return output;
    }
}
