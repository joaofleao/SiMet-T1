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

    /**
     * Método construtor do objeto
     * 
     * @param index        qual o index dessa fila na lista descrita na Main
     * @param servers      quantos servidores estao disponiveis nessa fila
     * @param capacity     qual a capacidade dessa fila
     * @param minArrival   qual o tempo minimo de chegada de novos clientes
     * @param maxArrival   qual o tempo máximo de chegada de novos clientes
     * @param minExit      qual o tempo mínimo de atendimento em um servidor
     * @param maxExit      qual o tempo máximo de atendimento em um servidor
     * @param destinations quais as conexões dessa fila com as outras
     * @author João Flores de Leão
     */

    public Queue(int index, int servers, int capacity, double minArrival, double maxArrival, double minExit,
            double maxExit, double[] destinations) {
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

    /**
     * Método get do losses
     * 
     * @return valor de losses
     * 
     * @author João Flores de Leão
     */
    public int getLosses() {
        return losses;
    }

    /**
     * Método get do index
     * 
     * @return valor de index
     * 
     * @author João Flores de Leão
     */
    public int getIndex() {
        return index;
    }

    /**
     * Método get do servers
     * 
     * @return valor de servers
     * 
     * @author João Flores de Leão
     */
    public int getServers() {
        return servers;
    }

    /**
     * Método get do capacidade
     * 
     * @return valor de capacidade
     * 
     * @author João Flores de Leão
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Método get do maxArrival
     * 
     * @return valor de maxArrival
     * 
     * @author João Flores de Leão
     */
    public double getMaxArrival() {
        return maxArrival;
    }

    /**
     * Método get do maxExit
     * 
     * @return valor de maxExit
     * 
     * @author João Flores de Leão
     */
    public double getMaxExit() {
        return maxExit;
    }

    /**
     * Método get do minArrival
     * 
     * @return valor de minArrival
     * 
     * @author João Flores de Leão
     */
    public double getMinArrival() {
        return minArrival;
    }

    /**
     * Método get do minExit
     * 
     * @return valor de minExit
     * 
     * @author João Flores de Leão
     */
    public double getMinExit() {
        return minExit;
    }

    /**
     * Método get do position
     * 
     * @return valor de position
     * 
     * @author João Flores de Leão
     */
    public int getPosition() {
        return position;
    }

    /**
     * Método get do state
     * 
     * @return valor de state
     * 
     * @author João Flores de Leão
     */
    public ArrayList getState() {
        return state;
    }

    /**
     * Método get do gerador
     * 
     * @return valor de gerador
     * 
     * @author João Flores de Leão
     */
    public Generator getGenerator() {
        return random;
    }

    /**
     * Método get do tempo
     * 
     * @return valor de tempo
     * 
     * @author João Flores de Leão
     */
    public double getTime() {
        return time;
    }

    /**
     * Método de execução de uma chegada na fila
     * 
     * @param rdn         gerador de numeros aleatorios com informações atualizadas
     * @param escalonador escalonador com dados de todas as filas
     * @return atualização do escalonador
     * 
     * @author João Flores de Leão
     */
    public Scheduler chegada(Generator rdn, Scheduler escalonador) {

        random = rdn;

        if (position < capacity || capacity == 0) {
            position++;

            if (position <= servers) {
                if (rdn.index() >= rdn.size() - 2) {
                    random.finish();
                    return escalonador;
                }
                int destination = getDestinations();
                double randomNumber = random.nextNumber();

                if (destination == -1) {
                    escalonador.addAction((maxExit - minExit) * randomNumber + minExit + time, 0, index, -1);

                } else {
                    escalonador.addAction((maxExit - minExit) * randomNumber + minExit + time, 2, index, destination);
                }
            }

        } else
            losses++;
        if (rdn.index() >= rdn.size() - 1) {
            random.finish();
            return escalonador;
        }
        double randomNumber = random.nextNumber();
        escalonador.addAction((maxArrival - minArrival) * randomNumber + minArrival + time, 1, -1, index);

        return escalonador;

    }

    /**
     * Método de execução de uma chegada na fila vinda de outra fila
     * 
     * @param rdn         gerador de numeros aleatorios com informações atualizadas
     * @param escalonador escalonador com dados de todas as filas
     * @return atualização do escalonador
     * 
     * @author João Flores de Leão
     */
    public Scheduler chegadaDePassagem(Generator rdn, Scheduler escalonador) {

        random = rdn;

        if (position < capacity || capacity == 0) {
            position++;

            if (position <= servers) {
                if (rdn.index() >= rdn.size() - 2) {
                    random.finish();
                    return escalonador;
                }
                int destination = getDestinations();
                double randomNumber = random.nextNumber();

                if (destination == -1) {
                    escalonador.addAction((maxExit - minExit) * randomNumber + minExit + time, 0, index, -1);

                } else {
                    escalonador.addAction((maxExit - minExit) * randomNumber + minExit + time, 2, index, destination);
                }
            }

        } else
            losses++;

        return escalonador;

    }

    /**
     * Método de execução de uma saída na fila
     * 
     * @param rdn         gerador de numeros aleatorios com informações atualizadas
     * @param escalonador escalonador com dados de todas as filas
     * @return atualização do escalonador
     * 
     * @author João Flores de Leão
     */

    public Scheduler saida(Generator rdn, Scheduler escalonador) {
        random = rdn;

        position--;

        if (position >= servers) {
            if (rdn.index() >= rdn.size() - 2) {
                random.finish();
                return escalonador;
            }
            int destination = getDestinations();
            double randomNumber = random.nextNumber();

            if (destination == -1) {
                escalonador.addAction((maxExit - minExit) * randomNumber + minExit + time, 0, index, -1);

            } else {
                escalonador.addAction((maxExit - minExit) * randomNumber + minExit + time, 2, index, destination);
            }
        }

        return escalonador;
    }

    /**
     * Método de verificação de um destino
     * 
     * @return qual destino do cliente que foi atendido
     * 
     * @author João Flores de Leão
     */

    public int getDestinations() {
        double counter = 0;
        for (int i = 0; i < destinations.length; i++) {
            if (destinations[i] == 1.0)
                return i - 1;
            counter = counter + destinations[i];
        }

        if (counter == 0)
            return -1;

        double randomNumber = random.nextNumber();

        double sum = 0;
        for (int i = 0; i < destinations.length; i++) {
            if (randomNumber < destinations[i] + sum) {
                return i;
            }
            sum = sum + destinations[i];
        }

        return -1;

    }

    /**
     * Método de verificação do tipo de fila
     * 
     * @return avisa se a fila possui initialStates
     * 
     * @author João Flores de Leão
     */

    public boolean isInitial() {
        if (minArrival == 0 && maxArrival == 0)
            return false;
        return true;
    }

    /**
     * Método de salvar o tempo em states
     * 
     * @param timePassed atualiza a lista de states com o novo tempo
     * 
     * @author João Flores de Leão
     */

    public void saveTime(double timePassed) {
        if (state.size() <= position && (state.size() <= capacity || capacity == 0))
            state.add(timePassed - time);
        else
            state.set(position, state.get(position) + timePassed - time);
        time = timePassed;
    }

}
