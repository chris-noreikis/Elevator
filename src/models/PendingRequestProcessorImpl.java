package models;

import gui.ElevatorDisplay.Direction;

import java.util.ArrayList;
import java.util.Comparator;

public class PendingRequestProcessorImpl implements PendingRequestProcessor {
    private ArrayList<ElevatorRequest> pendingRequests = new ArrayList<>();

    public ArrayList<ElevatorRequest> processPendingRequests(int startFloor) throws InvalidValueException {
        Building.getInstance().checkFloor("Invalid pending request start floor", startFloor);
        ArrayList<ElevatorRequest> sortedPendingRequests = new ArrayList<>(pendingRequests);
        sortedPendingRequests.sort(Comparator.comparingInt(o -> Math.abs(o.getFloorNumber() - startFloor)));

        ArrayList<ElevatorRequest> selectedPendingRequests = new ArrayList<>();
        if (sortedPendingRequests.size() > 0) {
            ElevatorRequest initialRequest = sortedPendingRequests.get(0);
            Direction directionToInitialRequest = ElevatorDirection.determineDirection(startFloor, initialRequest.getFloorNumber());
            selectedPendingRequests.add(initialRequest);

            for (int i = 1; i < sortedPendingRequests.size(); i++) {
                ElevatorRequest nextRequest = sortedPendingRequests.get(i);
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
