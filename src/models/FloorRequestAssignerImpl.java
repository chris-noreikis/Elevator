package models;

import gui.ElevatorDisplay.Direction;

public class FloorRequestAssignerImpl implements FloorRequestAssigner {
    public void addElevatorRequest(ElevatorRequest elevatorRequest, Person person) throws InvalidValueException {
        ElevatorLogger.getInstance().logAction("Person " + person.getId() + " presses " + elevatorRequest.getDirection() + " button on Floor " + elevatorRequest.getFloorNumber());

        int requestFloor = elevatorRequest.getFloorNumber();
        Direction requestDirection = elevatorRequest.getDirection();

        int numElevators = Building.getInstance().getNumberOfElevators();

        for (int i = 1; i < numElevators; i++) {
            Elevator e = ElevatorController.getInstance().getElevator(i);
            if (e.getCurrentFloor() == requestFloor && (e.getElevatorDirection() == requestDirection || e.getElevatorDirection() == Direction.IDLE)) {
                e.addFloorRequest(elevatorRequest);
                System.out.println("Adding floor request to elevator on floor;  " + e.getId());
                return;
            }
        }

        for (int i = 1; i < numElevators; i++) {
            Elevator e = ElevatorController.getInstance().getElevator(i);
            if (e.getElevatorDirection() != Direction.IDLE && e.isInDesiredDirection(requestFloor, requestDirection)) {
                e.addFloorRequest(elevatorRequest);
                System.out.println("Adding floor request to elevator travelling in same dir" + e.getId());
                return;
            }
        }

        for (int i = 1; i < numElevators; i++) {
            Elevator e = ElevatorController.getInstance().getElevator(i);
            if (e.getElevatorDirection() == Direction.IDLE) {
                e.addFloorRequest(elevatorRequest);
                System.out.println("Adding floor request to elevator that is idle" + e.getId());
                return;
            }
        }

        System.out.println("Adding to pending requests");
        ElevatorController.getInstance().addPendingRequest(elevatorRequest);
    }
}
