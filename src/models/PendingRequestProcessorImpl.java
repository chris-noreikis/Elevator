package models;

import gui.ElevatorDisplay.Direction;

import java.util.ArrayList;

public class PendingRequestProcessorImpl implements PendingRequestProcessor {
    private ArrayList<ElevatorRequest> pendingRequests = new ArrayList<>();

    public ArrayList<ElevatorRequest> processPendingRequests(int startFloor) throws InvalidValueException {
        ArrayList<ElevatorRequest> selectedPendingRequests = new ArrayList<>();
        if (pendingRequests.size() > 0) {
            ElevatorRequest initialRequest = pendingRequests.get(0);
            Direction directionToInitialRequest = ElevatorDirection.determineDirection(startFloor, initialRequest.getFloorNumber());
            selectedPendingRequests.add(initialRequest);

            for (int i = 1; i < pendingRequests.size(); i++) {
                ElevatorRequest nextRequest = pendingRequests.get(i);
                if (nextRequest.getDirection() == directionToInitialRequest) {
                    Direction nextDirection = ElevatorDirection.determineDirection(initialRequest.getFloorNumber(), nextRequest.getFloorNumber());
                    if (nextRequest.getDirection() == Direction.UP && nextDirection == Direction.UP) {
                        selectedPendingRequests.add(nextRequest);
                    } else if (nextRequest.getDirection() == Direction.DOWN && nextDirection == Direction.DOWN) {
                        selectedPendingRequests.add(nextRequest);
                    }
                }
            }
        }

        for (ElevatorRequest e : selectedPendingRequests) {
            pendingRequests.remove(e);
        }

        return selectedPendingRequests;
    }

    public void addPendingRequest(ElevatorRequest e) throws InvalidValueException {
        if (e == null) {
            throw new InvalidValueException("Elevator request cannot be null");
        }
        pendingRequests.add(e);
    }
}
