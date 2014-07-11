package elevators;

public class ElevatorGroup {
    public final int numOfElevators;
    private final Elevator[] elevators;

    public ElevatorGroup(int numOfElevators, int buildingHeight) {
        this.numOfElevators = numOfElevators;
        this.elevators = new Elevator[numOfElevators];
        for (int i = 0; i < numOfElevators; i++) {
            Elevator.ElevatorInfo info = new Elevator.ElevatorInfo(0, buildingHeight-1, 0);
            this.elevators[i] = new Elevator(info);
        }
    }
}
