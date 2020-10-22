public class Generator {
    private double a;
    private int m;
    private double c;
    private double x;
    private double[] list;

    private int index;

    public Generator(double a, int m, double c, double x) {
        this.a = a;
        this.m = m;
        this.c = c;
        this.x = x;
        this.index = 0;
    }

    public Generator(double[] list) {
        this.list = list;
        this.m = list.length;
    }

    public double nextNumber() {

        if (list != null) {
            x = list[index];
        } else {
            x = ((a * x + c) % m);
        }

        index++;

        if (list != null) {
            return x;
        }

        return format(x / m);

    }
    public int size() {
        return m;
    }
    public int index() {
        return index;
    }
    public void finish() {
       index = m;
    }


    public double format(double number) {
        return Math.floor(number * 1000) / 1000;
    }

}
