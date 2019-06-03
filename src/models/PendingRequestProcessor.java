package models;

import java.util.ArrayList;

public interface PendingRequestProcessor {
    ArrayList<ElevatorRequest> processPendingRequests(int startFloor) throws InvalidValueException;

    void addPendingRequest(ElevatorRequest e) throws InvalidValueException;
}
