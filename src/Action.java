
public class Action {

    private double time;
    private int action;

    public Action(double time, int action) {

        this.time = time;
        this.action = action;

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
            return "{S - " + time + "}";
        if (action == 1)
            return "{C - " + time + "}";

        return "{P - " + time + "}";

    }
}
