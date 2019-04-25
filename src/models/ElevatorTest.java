package models;

import SimulationConfiguration.BuildingConfigurable;
import SimulationConfiguration.ConfigurationException;
import SimulationConfiguration.ElevatorConfigurationFactory;
import gui.ElevatorDisplay;
import org.junit.jupiter.api.*;
import gui.ElevatorDisplay.Direction;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorTest {
    private int personCounter = 0;
    private ArrayList<Person> people;
    private Elevator elevator;

    @BeforeAll
    static void setupBuilding() {
        Building.getInstance();
        ElevatorController.getInstance();
    }

    @BeforeEach
    void resetState() {
        people = new ArrayList<>();
        elevator = ElevatorController.getInstance().getElevator(1);
        elevator.resetState();
        for (int i = 0; i < Building.getInstance().getNumberOfFloors(); i++) {
            Building.getInstance().getFloor(i).resetState();
        }
    }


    @Test
    @DisplayName("Test case 1 - Doors open")
    void opensDoorsForPassengerOnFirstFloor() throws InterruptedException {
        addPerson(1, 10, 1);
        assertEquals(elevator.getFloorRequests().get(0), new ElevatorRequest(Direction.UP, 1));
        moveElevatorNumTimes(1);
        assertEquals(true, elevator.getIsDoorOpen());
        moveElevatorNumTimes(1);
        assertEquals(true, elevator.getIsDoorOpen());
        moveElevatorNumTimes(1);
        assertEquals(false, elevator.getIsDoorOpen());
        assertEquals(1, elevator.getCurrentFloor());
        moveElevatorNumTimes(10);
        assertEquals(true, elevator.getIsDoorOpen());
    }

    @Test
    @DisplayName("Test case 1 - Rider can enter request")
    void enterRiderRequestForPassenger() throws InterruptedException {
        addPerson(1, 10, 1);
        ElevatorController.getInstance().moveElevators(1000);
        assertEquals(new ElevatorRequest(Direction.UP, 10), elevator.getRiderRequests().get(0));
    }

    @Test
    @DisplayName("Test case 1 - Rider can be moved to 10th floor")
    void takesPassengerToTenthFloor() throws InterruptedException {
        addPerson(1, 10, 1);

        // Trigger person pickup
        ElevatorController.getInstance().moveElevators(1000);

        // Triger elevator door close
        ElevatorController.getInstance().moveElevators(2000);

        // Move elevator up to tenth floor
        for (int i = 0; i < 11; i++) {
            ElevatorController.getInstance().moveElevators(1000);
        }
        assertEquals(true, elevator.getIsDoorOpen());
        assertEquals(0, elevator.getPeopleOnElevator().size());
        assertEquals(people.get(people.size() - 1), Building.getInstance().getFloor(9).getDonePerson(0));
    }

    @Test
    @DisplayName("Test case 1 - Elevator goes back to first floor after reset period")
    void idleTest1() throws InterruptedException {
        addPerson(1, 10, 1);
        // Doors open, passenger enters
        moveElevatorNumTimes(1);

        // Doors close
        moveElevatorNumTimes(2);

        // Up to Floor 10
        moveElevatorNumTimes(10);

        // Doors close
        moveElevatorNumTimes(2);

        // Wait for idle time
        moveElevatorNumTimes(5);
        assertEquals(10, elevator.getCurrentFloor());
        assertEquals(Direction.IDLE, elevator.getElevatorDirection());
        moveElevatorNumTimes(18);
        assertEquals(1, elevator.getCurrentFloor());
        assertEquals(Direction.IDLE, elevator.getElevatorDirection());
    }

    @Test
    @DisplayName("Test case 2 - Rider enters request from 20th floor")
    void testTwoMoveTwoPassenger() throws InterruptedException {
        addPerson(20, 5, 2);
        moveElevatorNumTimes(10);
        addPerson(15, 19, 2);
        moveElevatorNumTimes(40);
        assertEquals(people.get(people.size() - 1), Building.getInstance().getFloor(18).getDonePerson(0));
        assertEquals(people.get(people.size() - 2), Building.getInstance().getFloor(4).getDonePerson(0));
    }

    @Test
    @DisplayName("Test case 3 - Rider enters request from 20th floor")
    void testThreeMoveTwoPassengers() throws InterruptedException {
        addPerson(20, 1, 3);
        moveElevatorNumTimes(25);
        addPerson(10, 1, 3);
        moveElevatorNumTimes(40);
        assertEquals(people.get(people.size() - 1), Building.getInstance().getFloor(0).getDonePerson(1));
        assertEquals(people.get(people.size() - 2), Building.getInstance().getFloor(0).getDonePerson(0));
    }

    @Test
    @DisplayName("Test case 4 - Rider enters request from 20th floor")
    void testFourMovePassengers() throws InterruptedException {
        addPerson(1, 10, 1);
        moveElevatorNumTimes(5);
        addPerson(8, 17, 1);
        moveElevatorNumTimes(5);
    }

    @Test
    @DisplayName("Rider enters down request from 5th floor")
    void canGoDown() throws InterruptedException {
        addPerson(5, 1, 1);
        moveElevatorNumTimes(15);
        assertEquals(people.get(0), Building.getInstance().getFloor(0).getDonePerson(0));
    }

    private void moveElevatorNumTimes(int numTimes) throws InterruptedException {
        for (int i = 0; i < numTimes; i++) {
            ElevatorController.getInstance().moveElevators(1000);
//            Thread.sleep(1000);
        }
    }

    private void addPerson(int start, int end, int elevId) {
        ElevatorDisplay.Direction d = end > start ? ElevatorDisplay.Direction.UP : ElevatorDisplay.Direction.DOWN;
        Person p = new Person(start, end, "P" + personCounter++);
        ElevatorLogger.getInstance().logAction("Person " + p.getId() + " created " + " on Floor " + start + " wants to go " + d + " to floor " + end);
        people.add(p);
        Building.getInstance().addPerson(p, start);
        ElevatorController.getInstance().getElevator(elevId).addFloorRequest(new ElevatorRequest(d, start));
        ElevatorLogger.getInstance().logAction("Person " + p.getId() + " pressed " + d + " on Floor " + start);
    }
}