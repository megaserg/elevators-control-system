package elevators;

import java.util.List;

public interface ControlSystemInterface {
    public void acceptRequest(Request request);

    // I assume that during one step, each elevator can either:
    // 1) load and unload passengers or
    // 2) move one floor up or down
    public List<Command> makeStep();
}
