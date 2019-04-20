package models;

import gui.ElevatorDisplay.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static java.util.stream.Collectors.joining;

public class Elevator {
    private int id;
    private boolean isDoorOpen;
    private int currentFloor;
    private Building building;
    private ArrayList<ElevatorRequest> floorRequests;
    private ArrayList<ElevatorRequest> riderRequests;
    private ArrayList<Person> peopleOnElevator;

    private Direction elevatorDirection;
    private int capacity;
    private int elevatorSpeedInMilliseconds;
    private int doorOpenTimeInMilliseconds;
    private int returnToFirstFloorAfter;
    private int timeTilClose = 0;
    private int timeLeftOnFloor = 0;
    public int idleCount = 0;
    public static final int floorTime = 1000;
    public static final int doorTime = 2000;

    public Elevator(int id, int capacity, int elevatorSpeedInMilliseconds, int doorOpenTimeInMilliseconds, int returnToFirstFloorAfter) {
        setIsDoorOpen(false);
        setCurrentFloor(1);
        setId(id);
        setBuilding(building);
        setCapacity(capacity);
        setElevatorDirection(Direction.IDLE);
        setElevatorSpeedInMilliseconds(elevatorSpeedInMilliseconds);
        setDoorOpenTimeInMilliseconds(doorOpenTimeInMilliseconds);
        setReturnToFirstFloorAfter(returnToFirstFloorAfter);
        floorRequests = new ArrayList<>();
        riderRequests = new ArrayList<>();
        peopleOnElevator = new ArrayList<>();

        System.out.println(String.format("Elevator %s created ...", id));
    }

    public void addFloorRequest(ElevatorRequest elevatorRequest) {
        int requestFloorElevator = elevatorRequest.getFloorNumber();
        if (this.getFloorRequests().contains(elevatorRequest)) {
            return;
        }

        if (getElevatorDirection() == Direction.IDLE) {
            setElevatorDirection(requestFloorElevator >= getCurrentFloor() ? Direction.UP : Direction.DOWN);
        }

        floorRequests.add(elevatorRequest);

        String action = "Elevator " + getID() + " is going to floor " + elevatorRequest.getFloorNumber() +
                " for " + elevatorRequest.getDirection() + " request " + getRequestText();
        ElevatorLogger.getInstance().logAction(action);
    }

    private String getRequestText() {
        return getRiderRequestsText() + getCurrentFloorRequestsText();
    }

    private String getRiderRequestsText() {
        String riders = riderRequests.stream()
                .map(e -> Integer.toString(e.getFloorNumber()))
                .collect(joining(","));

        riders = riders.equals("") ? "none" : riders;
        return "[Current Rider Requests: " + riders + "]";
    }

    private String getCurrentFloorRequestsText() {
        String floors = floorRequests.stream()
                .map(e -> Integer.toString(e.getFloorNumber()))
                .collect(joining(","));

        floors = floors.equals("") ? "none" : floors;
        return "[Current Floor Requests: " + floors + "]";
    }

    private String getRidersText() {
        String riders = peopleOnElevator.stream()
                .map(e -> e.toString())
                .collect(joining(","));

        riders = riders.equals("") ? "none" : riders;
        return "[Riders: " + riders + "]";
    }

    public void move(int time) {
//        System.out.println("Elevator " + this.getID() + " currently on Floor " + getCurrentFloor());

        if (timeTilClose > 0 || timeLeftOnFloor > 0) {
            timeTilClose = Math.max(timeTilClose - time, 0);
            timeLeftOnFloor = Math.max(timeLeftOnFloor - time, 0);
        }

        if (timeTilClose > 0 || timeLeftOnFloor > 0) {
            return;
        }

        if (isDoorOpen) {
            closeDoors();
            return;
        }

        if (isRequestPoolEmpty() && getElevatorDirection() == Direction.IDLE && getCurrentFloor() != 1) {
            idleCount += time;
            if (idleCount >= getReturnToFirstFloorAfter()) {
                idleCount = 0;
                addFloorRequest(new ElevatorRequest(Direction.DOWN, 1));
            }
        }

        setToIdleIfNoMoreRequests();

        if (!isRequestPoolEmpty()) {
            move();
        }
    }

    private void setToIdleIfNoMoreRequests() {
        if (isRequestPoolEmpty()) {
            setElevatorDirection(Direction.IDLE);
        }
    }

    private boolean isRequestPoolEmpty() {
        return riderRequests.isEmpty() && floorRequests.isEmpty();
    }

    private void move() {
        boolean hasFloorRequest = floorHasFloorRequest();
        boolean hasRiderRequest = floorHasRiderRequest();
        if (hasFloorRequest || hasRiderRequest) {
            openDoors();
            timeTilClose = Elevator.doorTime;
            if (hasFloorRequest) {
                handleFloorRequest();
            }
            if (hasRiderRequest) {
                handleRiderRequest();
            }

            ArrayList<ElevatorRequest> sortedRequests = getSortedRequests();
            if (!sortedRequests.isEmpty()) {
                setElevatorDirection(sortedRequests.get(0).getDirection());
            }
        } else {
            ArrayList<ElevatorRequest> sortedRequests = getSortedRequests();
            ElevatorRequest nextRequest = sortedRequests.get(0);
            timeLeftOnFloor = Elevator.floorTime;
            int nextFloor = nextRequest.getFloorNumber() > getCurrentFloor() ? currentFloor + 1 : currentFloor - 1;
            ElevatorLogger.getInstance()
                    .logAction("Elevator " + getID() + " moving from floor " + currentFloor + " to floor " + nextFloor + " " + getRequestText());
            setCurrentFloor(nextFloor);
        }
    }

    private void handleRiderRequest() {
        removeRiderRequests();
    }

    private void handleFloorRequest() {
        movePeopleFromFloorToElevator();
        ElevatorLogger.getInstance()
                .logAction("Elevator " + getID() + " has arrived at Floor " + currentFloor + " for Floor Request " + getRequestText());
    }

    private boolean floorHasRiderRequest() {
        for (ElevatorRequest e : riderRequests) {
            if (e.getFloorNumber() == getCurrentFloor()) {
                return true;
            }
        }
        return false;
    }

    private boolean floorHasFloorRequest() {
        for (ElevatorRequest e : floorRequests) {
            if (e.getDirection() == getElevatorDirection() && e.getFloorNumber() == getCurrentFloor()) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<ElevatorRequest> getSortedRequests() {
        ArrayList<ElevatorRequest> sortedRequests = new ArrayList<>();
        sortedRequests.addAll(floorRequests);
        sortedRequests.addAll(riderRequests);

        sortedRequests.sort(new Comparator<ElevatorRequest>() {
            @Override
            public int compare(ElevatorRequest o1, ElevatorRequest o2) {
                return o2.getFloorNumber() - o1.getFloorNumber();
            }
        });

        if (getElevatorDirection() == Direction.UP) {
            Collections.reverse(sortedRequests);
        }

        return sortedRequests;
    }

    private void movePeopleFromFloorToElevator() {
        Floor f = Building.getInstance().getFloor(currentFloor - 1);
        removeFloorRequests(f);
        for (int i = 0; i < f.getNumberOfWaitingPersons(); i++) {
            f.movePersonFromFloorToElevator(i, this);
            Person justAddedPerson = peopleOnElevator.get(peopleOnElevator.size() - 1);
            ElevatorLogger.getInstance().logAction("Person " + justAddedPerson.getId() + " entered Elevator " + getID() + " " + getRidersText());
            Direction d = getDirection(justAddedPerson.getEndFloor(), justAddedPerson.getStartFloor());
            ElevatorRequest newRequest = new ElevatorRequest(d, justAddedPerson.getEndFloor());
            riderRequests.add(newRequest);
            ElevatorLogger.getInstance().logAction("Elevator " + getID() +
                    " Rider Request made for Floor " + newRequest.getFloorNumber() + " " + getRequestText());
        }
    }

    private void removeFloorRequests(Floor f) {
        ArrayList<ElevatorRequest> filteredFloorRequests = new ArrayList<>();
        for (ElevatorRequest request : floorRequests) {
            if (request.getDirection() == getElevatorDirection() && request.getFloorNumber() == f.getFloorNumber()) {
                continue;
            }

            filteredFloorRequests.add(request);
        }
        floorRequests = filteredFloorRequests;
    }

    private void removeRiderRequests() {
        ArrayList<ElevatorRequest> filteredRiderRequests = new ArrayList<>();
        for (ElevatorRequest request : riderRequests) {
            if (request.getFloorNumber() == currentFloor) {
                continue;
            }

            filteredRiderRequests.add(request);
        }
        riderRequests = filteredRiderRequests;

        ArrayList<Person> filteredPeople = new ArrayList<>();
        for (Person p : peopleOnElevator) {
            if (p.isAtDestinationFloor(currentFloor)) {
                ElevatorLogger.getInstance().logAction("Person " + p.toString() + " has left Elevator " + getID() + " " + getRidersText());
                continue;
            }

            filteredPeople.add(p);
        }
        peopleOnElevator = filteredPeople;
    }

    private void openDoors() {
        ElevatorLogger.getInstance().logAction("Elevator " + getID() + " Doors Open");
        setIsDoorOpen(true);
    }

    private void closeDoors() {
        ElevatorLogger.getInstance().logAction("Elevator " + getID() + " Doors Close");
        setIsDoorOpen(false);
    }

    private void setBuilding(Building b) {
        this.building = b;
    }

    public ArrayList<ElevatorRequest> getFloorRequests() {
        return this.floorRequests;
    }

    private void setId(int id) {
        this.id = id;
    }

    private int getID() {
        return id;
    }

    public boolean getIsDoorOpen() {
        return this.isDoorOpen;
    }

    private void setIsDoorOpen(boolean nextValue) {
        this.isDoorOpen = nextValue;
    }

    public void pickUpPassenger(Person p) {
        this.peopleOnElevator.add(p);
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    private void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public Direction getElevatorDirection() {
        return elevatorDirection;
    }

    private void setElevatorDirection(Direction elevatorDirection) {
        this.elevatorDirection = elevatorDirection;
    }

    private int getCapacity() {
        return capacity;
    }

    private void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    private int getElevatorSpeedInMilliseconds() {
        return elevatorSpeedInMilliseconds;
    }

    private void setElevatorSpeedInMilliseconds(int elevatorSpeedInMilliseconds) {
        this.elevatorSpeedInMilliseconds = elevatorSpeedInMilliseconds;
    }

    private int getDoorOpenTimeInMilliseconds() {
        return doorOpenTimeInMilliseconds;
    }

    private void setDoorOpenTimeInMilliseconds(int doorOpenTimeInMilliseconds) {
        this.doorOpenTimeInMilliseconds = doorOpenTimeInMilliseconds;
    }

    private int getReturnToFirstFloorAfter() {
        return returnToFirstFloorAfter;
    }

    private void setReturnToFirstFloorAfter(int returnToFirstFloorAfter) {
        this.returnToFirstFloorAfter = returnToFirstFloorAfter;
    }

    public static Direction getDirection(int endFloor, int startFloor) {
        return endFloor > startFloor ? Direction.UP : Direction.DOWN;
    }

    public ArrayList<Person> getPeopleOnElevator() {
        return this.peopleOnElevator;
    }

    public ArrayList<ElevatorRequest> getRiderRequests() {
        return this.riderRequests;
    }

    public void resetState() {
        setIsDoorOpen(false);
        setCurrentFloor(1);
        setElevatorDirection(Direction.IDLE);
        floorRequests = new ArrayList<>();
        riderRequests = new ArrayList<>();
        peopleOnElevator = new ArrayList<>();
        timeLeftOnFloor = 0;
        timeTilClose = 0;
        idleCount = 0;
    }


    public String toString() {
        String output = "";

        output += "Elevator " + this.getID() + " report ...\n";
        output += "Current Direction: " + getElevatorDirection() + "\n";
        output += "Current Floor: " + getCurrentFloor() + "\n";
        output += "Current Floor Requests " + floorRequests + "\n";
        output += "Current Rider Requests " + riderRequests + "\n";
        output += "Current Passengers: " + peopleOnElevator.toString() + "\n";
        output += "Doors Open: " + getIsDoorOpen() + "\n";

        return output;
    }
}
