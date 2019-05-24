package models;

import configuration.SimulationConfiguration;
import gui.ElevatorDisplay.Direction;

public class FloorRequestAssignerImpl implements FloorRequestAssigner {
    public void addElevatorRequest(ElevatorRequest elevatorRequest, Person person) throws InvalidValueException {
        ElevatorLogger.getInstance().logAction("Person " + person.getId() + " presses " + elevatorRequest.getDirection() + " button on Floor " + elevatorRequest.getFloorNumber());
        ElevatorController elevatorController = ElevatorController.getInstance();

        int requestFloor = elevatorRequest.getFloorNumber();
        Direction requestDirection = elevatorRequest.getDirection();

        int numElevators = SimulationConfiguration.getInstance().getNumberOfElevators();

        for (int i = 1; i <= numElevators; i++) {
            boolean isIdleOrInDirection = (elevatorController.getElevatorDirection(i) == Direction.IDLE || elevatorController.getElevatorDirection(i) == requestDirection);
            boolean isSpaceOpen = elevatorController.isElevatorSpaceOpen(i);
            if (elevatorController.isElevatorOnFloor(i, requestFloor) && isIdleOrInDirection && isSpaceOpen) {
                elevatorController.addFloorRequestToElevator(i, elevatorRequest);
                return;
            }
        }

        for (int i = 1; i <= numElevators; i++) {
            boolean isSpaceOpen = elevatorController.isElevatorSpaceOpen(i);
            if (elevatorController.getElevatorDirection(i) != Direction.IDLE && elevatorController.isElevatorInDesiredDirection(i, requestFloor, requestDirection) && isSpaceOpen) {
                elevatorController.addFloorRequestToElevator(i, elevatorRequest);
                return;
            }
        }

        for (int i = 1; i <= numElevators; i++) {
            boolean isSpaceOpen = elevatorController.isElevatorSpaceOpen(i);
            if (elevatorController.getElevatorDirection(i) == Direction.IDLE && isSpaceOpen) {
                elevatorController.addFloorRequestToElevator(i, elevatorRequest);
                return;
            }
        }

        ElevatorController.getInstance().addPendingRequest(elevatorRequest);
    }
}
