package models;

import java.util.ArrayList;

public interface PendingRequestProcessor {
    public ArrayList<ElevatorRequest> processPendingRequests(int startFloor) throws InvalidValueException;

    public void addPendingRequest(ElevatorRequest e) throws InvalidValueException;
}
