
public class Action {

    private double time;
    private int action;
    private int destination;
    private int origin;

    public Action(double time, int action, int origin, int destination) {

        this.time = time;
        this.action = action;
        this.destination = destination;
        this.origin = origin;

    }

    public int getDestination() {
        return destination;
    }

    public int getOrigin() {
        return origin;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public double getTime() {
        return time;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String toString() {
        if (action == 0)
            return "{S" + origin + " - " + time + "}";
        if (action == 1)
            return "{C" + origin + " - " + time + "}";

        return "{P" + origin + "" + destination + " - " + time + "}";

    }
}
