package models;

import gui.ElevatorDisplay;
import org.omg.CORBA.DynAnyPackage.Invalid;

import java.util.ArrayList;

public class PendingRequestProcessorImpl implements PendingRequestProcessor {
    private ArrayList<ElevatorRequest> pendingRequests = new ArrayList<>();

    public ArrayList<ElevatorRequest> processPendingRequests() throws InvalidValueException {
        ArrayList<ElevatorRequest> selectedPendingRequests = new ArrayList<>();
        if (pendingRequests.size() > 0) {
            ElevatorRequest request = pendingRequests.get(0);
            selectedPendingRequests.add(request);

            for (int i = 1; i < pendingRequests.size() - 1; i++) {
                ElevatorRequest nextRequest = pendingRequests.get(i);
                if (nextRequest.getDirection() == request.getDirection()) {
                    ElevatorDisplay.Direction nextDirection = ElevatorDirection.determineDirection(request.getFloorNumber(), nextRequest.getFloorNumber());
                    if (nextRequest.getDirection() == ElevatorDisplay.Direction.UP && nextDirection == ElevatorDisplay.Direction.UP) {
                        selectedPendingRequests.add(nextRequest);
                    } else if (nextRequest.getDirection() == ElevatorDisplay.Direction.DOWN && nextDirection == ElevatorDisplay.Direction.DOWN) {
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
