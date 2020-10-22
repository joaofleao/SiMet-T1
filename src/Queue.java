import java.util.ArrayList;
public class Queue {
    private int capacity;
    private int index;
    private int servers;
    private double minArrival;
    private double maxArrival;
    private double minExit;
    private double maxExit;
    private double[] destinations;

    private int position = 0;
    private double time = 0;
    private int losses = 0;
    private ArrayList<Double> state;
    private Generator random;

    public Queue(int index, int servers, int capacity, double minArrival, double maxArrival, double minExit, double maxExit, double[] destinations) {
        this.capacity = capacity;
        this.index = index;
        this.servers = servers;
        this.minArrival = minArrival;
        this.maxArrival = maxArrival;
        this.minExit = minExit;
        this.maxExit = maxExit;
        this.destinations = destinations;
        this.state = new ArrayList();
    }

    public int getLosses() {
        return losses;
    }
    public int getIndex() {
        return index;
    }
    public int getServers() {
        return servers;
    }
    public int getCapacity() {
        return capacity;
    }
    public double getMaxArrival() {
        return maxArrival;
    }
    public double getMaxExit() {
        return maxExit;
    }
    public double getMinArrival() {
        return minArrival;
    }
    public double getMinExit() {
        return minExit;
    }
    public int getPosition() {
        return position;
    }


    public ArrayList getState() {
        return state;
    }

    public Generator getGenerator() {
        return random;
    }

    public double getTime() {
        return time;
    }

    public Scheduler chegada(Generator rdn, Scheduler escalonador) {

        random = rdn;

        if (position < capacity) {
            position++;

            if (position <= servers) {
                int destination = getDestinations();
                if(rdn.index() >= rdn.size()-1) {
                    random.finish();
                    return escalonador;
                }
                double randomNumber = random.nextNumber();

                if(destination==-1) {
                    escalonador.addAction((maxExit - minExit) * randomNumber + minExit + time, 0, index, -1);
                    
                }
                else {
                    escalonador.addAction((maxExit - minExit) * randomNumber + minExit + time, 2, index, destination);
                }
            }
            
        } else
        losses++;
        if (index==0) {
            if(rdn.index() >= rdn.size()-1) {
                random.finish();
                return escalonador;
            }
            double randomNumber = random.nextNumber();
            escalonador.addAction((maxArrival - minArrival) * randomNumber + minArrival + time, 1, -1, index);

        }

        return escalonador;

    }

    public Scheduler saida(Generator rdn, Scheduler escalonador) {
        random = rdn;

        position--;

        if (position >= servers) {
            int destination = getDestinations();
            if(rdn.index() >= rdn.size()-1) {
                random.finish();
                return escalonador;
            } 
            double randomNumber = random.nextNumber();
            
            if(destination==-1) {
                escalonador.addAction((maxExit - minExit) * randomNumber + minExit + time, 0, index, -1);
                
            }
            else {
                escalonador.addAction((maxExit - minExit) * randomNumber + minExit + time, 2, index, destination);
            }
        }
        
        
        return escalonador;
    }
    
    public int getDestinations() {
        double counter = 0;
        for (int i = 0; i < destinations.length; i++) {
            if (destinations[i]==1.0) return i-1;
            counter = counter + destinations[i];
        }

        if (counter==0) return -1;

        double randomNumber = random.nextNumber();
        
        double sum = 0;
        for (int i = 0; i < destinations.length; i++) {
            if (randomNumber < destinations[i] + sum) {
                return i;
            }
            sum = sum + destinations[i];
        }
        sum = destinations[0];
        
        return -1;

    }

    public boolean  isInitial() { 
        if (minArrival==0 && maxArrival==0) return false;
        return true;
    }

    public void saveTime(double timePassed) {
        if (state.size()<=position && (state.size()<=capacity || capacity==0)) state.add(0 + timePassed - time);
        else state.set(position, state.get(position) + timePassed - time);
        time = timePassed; 
    }

}
