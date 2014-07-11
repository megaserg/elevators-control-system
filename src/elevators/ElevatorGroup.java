package elevators;

public class ElevatorGroup {
    public final int numOfElevators;
    public final Elevator[] elevators;

    public ElevatorGroup(int numOfElevators, int buildingHeight) {
        this.numOfElevators = numOfElevators;
        this.elevators = new Elevator[numOfElevators];
        for (int i = 0; i < numOfElevators; i++) {
            // Hardcoded default: every elevator works for the whole building.
            Elevator.ElevatorInfo info = new Elevator.ElevatorInfo(i, 1, buildingHeight);
            this.elevators[i] = new Elevator(info);
        }
    }
}
