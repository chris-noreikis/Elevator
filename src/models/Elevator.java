package models;

import gui.ElevatorDisplay;
import gui.ElevatorDisplay.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Elevator {
    private ArrayList<ElevatorRequest> floorRequests;
    private ArrayList<ElevatorRequest> riderRequests;
    private ArrayList<Person> peopleOnElevator;

    private int id;
    private boolean isDoorOpen;
    private int currentFloor;
    private int capacity;
    private int elevatorSpeed;
    private int doorOpenTime;
    private int returnToDefaultFloorTimeout;
    private int timeUntilDoorsClose = 0;
    private int timeUntilNextFloor = 0;
    private int idleTimeout = 0;
    private Direction elevatorDirection;

    public Elevator(int id, int capacity, int elevatorSpeed, int doorOpenTime, int returnToDefaultFloorTimeout) throws InvalidValueException {
        setIsDoorOpen(false);
        setCurrentFloor(1);
        setId(id);
        setCapacity(capacity);
        setElevatorDirection(Direction.IDLE);
        setElevatorSpeed(elevatorSpeed);
        setDoorOpenTime(doorOpenTime);
        setReturnToDefaultFloorTimeout(returnToDefaultFloorTimeout);
        floorRequests = new ArrayList<>();
        riderRequests = new ArrayList<>();
        peopleOnElevator = new ArrayList<>();
    }

    public void addFloorRequest(ElevatorRequest elevatorRequest) throws InvalidValueException {
        Building.getInstance().checkFloor("Invalid floor request: ", elevatorRequest.getFloorNumber());

        int requestFloorElevator = elevatorRequest.getFloorNumber();
        if (getFloorRequests().contains(elevatorRequest)) {
            return;
        }

        if (getElevatorDirection() == Direction.IDLE) {
            Direction dir = ElevatorDirection.determineDirection(getCurrentFloor(), requestFloorElevator);
            setElevatorDirection(dir);
        }

        floorRequests.add(elevatorRequest);

        String action = "Elevator " + getId() + " is going to Floor " + elevatorRequest.getFloorNumber() +
                " for " + elevatorRequest.getDirection() + " request " + getRequestText();
        ElevatorLogger.getInstance().logAction(action);
    }

    public void doTimeSlice(int time) throws InvalidValueException {
        if (time < 0) {
            throw new InvalidValueException("Elevator told to move with invalid time: " + time);
        }

        if (getTimeUntilDoorsClose() > 0 || getTimeUntilNextFloor() > 0) {
            decrementWaitTimes(time);
        }

        if (getTimeUntilDoorsClose() > 0 || getTimeUntilNextFloor() > 0) {
            return;
        }

        if (getIsDoorOpen()) {
            closeDoors();
            return;
        }

        if (isRequestPoolEmpty() && !isIdleOnDefaultFloor()) {
            int idleCount = getIdleTimeout();
            idleCount += time;
            setIdleTimeout(idleCount);
            if (idleCount >= getReturnToDefaultFloorTimeout()) {
                setIdleTimeout(0);
                addFloorRequest(new ElevatorRequest(Direction.UP, 1));
            }
        }

        setToIdleIfNoMoreRequests();

        if (isRequestPoolEmpty()) {
            processPendingRequests();
        } else {
            respondToRequests();
        }
    }

    public boolean isInDesiredDirection(int requestFloor, Direction requestDirection) throws InvalidValueException {
        Building.getInstance().checkFloor("Invalid request floor", requestFloor);

        if (requestDirection == null) {
            throw new InvalidValueException("Request direction cannot be null");
        }

        ArrayList<ElevatorRequest> elevatorRequests = getSortedRequests();
        if (elevatorRequests.size() > 0) {
            ElevatorRequest nextRequest = elevatorRequests.get(0);
            if (riderRequests.contains(nextRequest)) {
                if (isMovingTowardsFloor(requestFloor) && getElevatorDirection() == requestDirection) {
                    System.out.println("** RIDER REQUEST IN SAME DIR **" + requestFloor);
                    return true;
                }
            }

            if (floorRequests.contains(nextRequest)) {
                if (isMovingTowardsFloor(requestFloor) && getElevatorDirection() == requestDirection && requestDirection == nextRequest.getDirection()) {
                    System.out.println("** FLOOR REQUEST IN SAME DIR **" + requestFloor);
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isIdleOnDefaultFloor() {
        return getElevatorDirection() == Direction.IDLE && getCurrentFloor() == 1;
    }

    private void processPendingRequests() throws InvalidValueException {
        ArrayList<ElevatorRequest> requests = ElevatorController.getInstance().processPendingRequests();
        for (ElevatorRequest e : requests) {
            System.out.println("Adding elevator request from pending requests:" + e);
            addFloorRequest(e);
        }
    }

    private boolean isMovingTowardsFloor(int requestFloor) {
        if (getElevatorDirection() == Direction.UP && requestFloor >= getCurrentFloor()) {
            return true;
        }

        if (getElevatorDirection() == Direction.DOWN && requestFloor <= getCurrentFloor()) {
            return true;
        }

        return false;
    }

    private void decrementWaitTimes(int time) throws InvalidValueException {
        int nextDoorCloseTime = Math.max(getTimeUntilDoorsClose() - time, 0);
        setTimeUntilDoorsClose(nextDoorCloseTime);

        int nextTimeLeftOnFloorTime = Math.max(getTimeUntilNextFloor() - time, 0);
        setTimeUntilNextFloor(nextTimeLeftOnFloorTime);
    }

    private void respondToRequests() throws InvalidValueException {
        setDirectionIfTopOrBottomFloor();

        boolean hasFloorRequest = floorHasFloorRequest();
        boolean hasRiderRequest = floorHasRiderRequest();

        if (hasFloorRequest || hasRiderRequest) {
            openDoors();
            setTimeUntilDoorsClose(getDoorOpenTime());
            if (hasRiderRequest) {
                movePeopleFromElevatorToFloor();
            }

            if (hasFloorRequest) {
                movePeopleFromFloorToElevator();
            }
        } else {
            moveToNextRequest();
        }

        setNextElevatorDirection();
        writeElevatorStateToDisplay();
    }

    private void writeElevatorStateToDisplay() {
        ElevatorDisplay.getInstance().updateElevator(getId(), getCurrentFloor(), getPeopleOnElevator().size(), getElevatorDirection());
    }

    private void setNextElevatorDirection() {
        ArrayList<ElevatorRequest> sortedRequests = getSortedRequests();
        if (!sortedRequests.isEmpty()) {
            int nextRequestFloorNumber = sortedRequests.get(0).getFloorNumber();
            if (getElevatorDirection() == Direction.UP && nextRequestFloorNumber < currentFloor) {
                setElevatorDirection(Direction.DOWN);
            } else if (getElevatorDirection() == Direction.DOWN && nextRequestFloorNumber > currentFloor) {
                setElevatorDirection(Direction.UP);
            } else if (nextRequestFloorNumber == getCurrentFloor()) {
                setElevatorDirection(sortedRequests.get(0).getDirection());
            }
        }
    }

    private void moveToNextRequest() throws InvalidValueException {
        setTimeUntilNextFloor(getElevatorSpeed());
        int nextFloor = getElevatorDirection() == Direction.UP ? currentFloor + 1 : currentFloor - 1;
        ElevatorLogger.getInstance()
                .logAction("Elevator " + getId() + " moving from Floor " + currentFloor + " to Floor " + nextFloor + " " + getRequestText());
        setCurrentFloor(nextFloor);
    }

    private void setToIdleIfNoMoreRequests() {
        if (isRequestPoolEmpty()) {
            ElevatorDisplay.getInstance().updateElevator(getId(), getCurrentFloor(), getPeopleOnElevator().size(), getElevatorDirection());
            setElevatorDirection(Direction.IDLE);
        }
    }

    private void setDirectionIfTopOrBottomFloor() throws InvalidValueException {
        if (getCurrentFloor() == 1 && getElevatorDirection() == Direction.DOWN) {
            setElevatorDirection(Direction.UP);
        }

        if (getCurrentFloor() == Building.getInstance().getNumberOfFloors() && getElevatorDirection() == Direction.UP) {
            setElevatorDirection(Direction.DOWN);
        }
    }

    private boolean isRequestPoolEmpty() {
        return riderRequests.isEmpty() && floorRequests.isEmpty();
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

        sortedRequests.sort((o1, o2) -> o2.getFloorNumber() - o1.getFloorNumber());

        if (getElevatorDirection() == Direction.DOWN) {
            Collections.reverse(sortedRequests);
        }

        return sortedRequests;
    }

    private void removeFloorRequests() {
        floorRequests.removeIf(request -> request.getDirection() == getElevatorDirection() && request.getFloorNumber() == getCurrentFloor());
    }

    private void movePeopleFromFloorToElevator() throws InvalidValueException {
        removeFloorRequests();

        ElevatorLogger.getInstance()
                .logAction("Elevator " + getId() + " has arrived at Floor " + currentFloor + " for Floor Request " + getRequestText());


        Person p = Building.getInstance().peekNextPerson(getCurrentFloor(), getElevatorDirection());

        while (p != null && isElevatorSpaceAvailable()) {
            peopleOnElevator.add(p);
            Building.getInstance().removePerson(getCurrentFloor(), p);
            ElevatorLogger.getInstance().logAction("Person " + p.getId() + " entered Elevator " + getId() + " " + getRidersText());
            p.startElevatorRide();
            Direction dir = ElevatorDirection.determineDirection(p.getStartFloor(), p.getEndFloor());
            ElevatorRequest newRequest = new ElevatorRequest(dir, p.getEndFloor());
            if (!riderRequests.contains(newRequest)) {
                riderRequests.add(newRequest);
            }
            ElevatorLogger.getInstance().logAction("Elevator " + getId() +
                    " Rider Request made for Floor " + newRequest.getFloorNumber() + " " + getRequestText());

            p = Building.getInstance().peekNextPerson(getCurrentFloor(), getElevatorDirection());
        }

        if (p != null) {
            ElevatorRequest e = new ElevatorRequest(ElevatorDirection.determineDirection(p.getStartFloor(), p.getEndFloor()), p.getStartFloor());
            ElevatorController.getInstance().addElevatorRequest(e, p);
        }
    }

    private int getRemainingElevatorCapacity() {
        return getCapacity() - peopleOnElevator.size();
    }

    public boolean isElevatorSpaceAvailable() {
        return getRemainingElevatorCapacity() > 0;
    }

    private void movePeopleFromElevatorToFloor() throws InvalidValueException {
        ElevatorLogger.getInstance()
                .logAction("Elevator " + getId() + " has arrived at Floor " + currentFloor + " for Rider Request " + getRequestText());
        ArrayList<ElevatorRequest> filteredRiderRequests = new ArrayList<>();
        for (ElevatorRequest request : riderRequests) {
            if (request.getFloorNumber() == currentFloor) {
                continue;
            }

            filteredRiderRequests.add(request);
        }
        riderRequests = filteredRiderRequests;

        ArrayList<Person> filteredPeople = new ArrayList<>();
        Iterator<Person> personIterator = peopleOnElevator.iterator();
        while (personIterator.hasNext()) {
            Person p = personIterator.next();
            if (p.isAtDestinationFloor(currentFloor)) {
                personIterator.remove();
                filteredPeople.add(p);
                p.endElevatorRide();
                ElevatorLogger.getInstance().logAction("Person " + p.toString() + " has left Elevator " + getId() + " " + getRidersText());
                Building.getInstance().addDonePeople(getCurrentFloor(), filteredPeople);
            }
        }
    }

    private void openDoors() throws InvalidValueException {
        if (getIsDoorOpen()) {
            throw new InvalidValueException("Opening already open doors");
        }

        ElevatorLogger.getInstance().logAction("Elevator " + getId() + " Doors Open");
        setIsDoorOpen(true);
        ElevatorDisplay.getInstance().openDoors(getId());
    }

    private void closeDoors() throws InvalidValueException {
        if (!getIsDoorOpen()) {
            throw new InvalidValueException("Closing already closed doors");
        }

        ElevatorLogger.getInstance().logAction("Elevator " + getId() + " Doors Close");
        setIsDoorOpen(false);
        ElevatorDisplay.getInstance().closeDoors(getId());
    }

    private String getRequestText() {
        return getCurrentFloorRequestsText() + getRiderRequestsText();
    }

    private String getRiderRequestsText() {
        String riders = getCommaJoinedCollection(riderRequests);
        return "[Current Rider Requests: " + riders + "]";
    }

    private String getCurrentFloorRequestsText() {
        String floors = getCommaJoinedCollection(floorRequests);
        return "[Current Floor Requests: " + floors + "]";
    }

    private String getRidersText() {
        String riders = getCommaJoinedCollection(peopleOnElevator);
        return "[Riders: " + riders + "]";
    }

    private String getCommaJoinedCollection(ArrayList collection) {
        if (collection.isEmpty()) {
            return "none";
        }

        StringBuilder formattedItems = new StringBuilder();
        for (int i = 0; i < collection.size(); i++) {
            if (i > 0) {
                formattedItems.append(", ");
            }

            formattedItems.append(collection.get(i));
        }

        return formattedItems.toString();
    }

    private ArrayList<ElevatorRequest> getFloorRequests() {
        return this.floorRequests;
    }

    private void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private boolean getIsDoorOpen() {
        return this.isDoorOpen;
    }

    private void setIsDoorOpen(boolean nextValue) {
        this.isDoorOpen = nextValue;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    private void setCurrentFloor(int currentFloor) throws InvalidValueException {
        Building.getInstance().checkFloor("Elevator " + getId() + " set to incorrect floor", currentFloor);
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

    private void setCapacity(int capacity) throws InvalidValueException {
        if (capacity < 0) {
            throw new InvalidValueException("Elevator capacity must be greater than/equal to 0, got: " + capacity);
        }
        this.capacity = capacity;
    }

    private int getElevatorSpeed() {
        return elevatorSpeed;
    }

    private void setElevatorSpeed(int elevatorSpeed) throws InvalidValueException {
        if (elevatorSpeed < 0) {
            throw new InvalidValueException("Elevator speed must be greater than/equal to 0, got: " + elevatorSpeed);
        }

        this.elevatorSpeed = elevatorSpeed;
    }

    private int getDoorOpenTime() {
        return doorOpenTime;
    }

    private void setDoorOpenTime(int doorOpenTime) throws InvalidValueException {
        if (doorOpenTime < 0) {
            throw new InvalidValueException("Door open time must be greater than/equal to 0, got: " + doorOpenTime);
        }

        this.doorOpenTime = doorOpenTime;
    }

    private int getReturnToDefaultFloorTimeout() {
        return returnToDefaultFloorTimeout;
    }

    private void setReturnToDefaultFloorTimeout(int returnToDefaultFloorTimeout) throws InvalidValueException {
        if (returnToDefaultFloorTimeout < 0) {
            throw new InvalidValueException("Return to first floor time must be greater than/equal to 0, got: " + returnToDefaultFloorTimeout);
        }

        this.returnToDefaultFloorTimeout = returnToDefaultFloorTimeout;
    }

    private int getTimeUntilNextFloor() {
        return timeUntilNextFloor;
    }

    private void setTimeUntilNextFloor(int timeLeftOnFloorIn) throws InvalidValueException {
        if (timeLeftOnFloorIn < 0) {
            throw new InvalidValueException("Time left on floor must be greater than/equal to 0, got: " + timeLeftOnFloorIn);
        }

        this.timeUntilNextFloor = timeLeftOnFloorIn;
    }

    private int getIdleTimeout() {
        return idleTimeout;
    }

    private void setIdleTimeout(int idleTimeout) throws InvalidValueException {
        if (idleTimeout < 0) {
            throw new InvalidValueException("Idle count must be greater than/equal to 0, got: " + idleTimeout);
        }

        this.idleTimeout = idleTimeout;
    }

    private int getTimeUntilDoorsClose() {
        return timeUntilDoorsClose;
    }

    private void setTimeUntilDoorsClose(int timeTilClose) throws InvalidValueException {
        if (timeTilClose < 0) {
            throw new InvalidValueException("Time until doors close must be greater than/equal to 0, got: " + timeTilClose);
        }

        this.timeUntilDoorsClose = timeTilClose;
    }

    private ArrayList<Person> getPeopleOnElevator() {
        return this.peopleOnElevator;
    }

    public String toString() {
        String output = "";

        output += "   Elevator " + getId() + " report ...\n";
        output += "   Current Direction: " + getElevatorDirection() + "\n";
        output += "   Current Floor: " + getCurrentFloor() + "\n";
        output += "   Current Floor Requests " + floorRequests + "\n";
        output += "   Current Rider Requests " + riderRequests + "\n";
        output += "   Current Passengers: " + peopleOnElevator.toString() + "\n";
        output += "   Doors Open: " + getIsDoorOpen() + "\n";
        output += "   ---------------\n";

        return output;
    }
}
