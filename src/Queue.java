public class Queue {

    private Scheduler scheduler = new Scheduler();

    private int capacity;
    private int servers;
    private double minArrival;
    private double maxArrival;
    private double minExit;
    private double maxExit;

    private int position = 0;
    private double time = 0;
    private int losses = 0;
    private double[] state;

    private double[] random;

    public Queue(double[] random, int servers, int capacity, double minArrival, double maxArrival, double minExit,
            double maxExit) {
        this.random = random;
        this.capacity = capacity;
        this.servers = servers;
        this.minArrival = minArrival;
        this.maxArrival = maxArrival;
        this.minExit = minExit;
        this.maxExit = maxExit;
        this.state = new double[capacity + 1];
    }

    public int getLosses() {
        return losses;
    }

    public double[] getState() {
        return state;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public double getTime() {
        return time;
    }

    public int chegada(int randomCounter, Scheduler escalonador) {

        if (position < capacity) {
            position++;

            if (position <= servers) {
                escalonador.addAction((maxExit - minExit) * random[randomCounter] + minExit + time, 2);
                randomCounter++;
            }

        } else
            losses++;

        try {
            escalonador.addAction((maxArrival - minArrival) * random[randomCounter] + minArrival + time, 1);
            randomCounter++;
        } catch (Exception e) {
        }
        scheduler = escalonador;

        return randomCounter;

    }

    public int saida(int randomCounter, Scheduler escalonador) {

        position--;

        if (position >= servers) {

            escalonador.addAction((maxExit - minExit) * random[randomCounter] + minExit + time, 0);
            randomCounter++;
        }
        scheduler = escalonador;
        return randomCounter;
    }

    public int saidaPassagem(int randomCounter, Scheduler escalonador) {

        position--;

        if (position >= servers) {
            escalonador.addAction((maxExit - minExit) * random[randomCounter] + minExit + time, 2);
            randomCounter++;
        }
        scheduler = escalonador;
        return randomCounter;
    }

    public int chegadaPassagem(int randomCounter, Scheduler escalonador) {

        if (position < capacity) {
            position++;

            if (position <= servers) {
                escalonador.addAction((maxExit - minExit) * random[randomCounter] + minExit + time, 0);
                randomCounter++;
            }

        }
        scheduler = escalonador;
        return randomCounter;
    }

    public void saveTime(double timePassed) {

        state[position] = state[position] + timePassed - time;
        time = timePassed;

    }

}
