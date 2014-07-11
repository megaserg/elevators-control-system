package elevators;

public final class Request {
    public final int sourceFloor;
    public final Passenger passenger;

    public Request(int sourceFloor, Passenger passenger) {
        this.sourceFloor = sourceFloor;
        this.passenger = passenger;
    }
}
