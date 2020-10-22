import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.io.File; 
import java.util.Scanner; 

public class Main {

    private static ArrayList<Queue> queues;
    private static Scheduler scheduler;
    private static double[] initialState;

    public static void main(String args[]) throws FileNotFoundException {

        File file = new File("./model.txt");
        Scanner reader = new Scanner(file);

        int m = Integer.parseInt(reader.nextLine());

        
        String[] initialStatesData = reader.nextLine().split(",");
        initialState = new double[initialStatesData.length];
        
        for (int i = 0; i < initialStatesData.length; i++) {
            initialState[i] = Double.parseDouble(initialStatesData[i]);
        }
        
        queues = new ArrayList();

        for (int i = 0; reader.hasNextLine(); i++) {

            String[] lineData = reader.nextLine().split(",");
            String[] connectionsData = reader.nextLine().split(",");

            double[] connections = new double[connectionsData.length];
            for (int j = 0; j < initialStatesData.length; j++) {
                connections[j] = Double.parseDouble(connectionsData[j]);
            }

            queues.add(new Queue(i, Integer.parseInt(lineData[0]), Integer.parseInt(lineData[1]), Integer.parseInt(lineData[2]), Integer.parseInt(lineData[3]), Integer.parseInt(lineData[4]), Integer.parseInt(lineData[5]), connections));

        }

        // Generator rdn = new Generator(1103515245.0, m, 12345.0, 1.0);

        Generator rdn = new Generator(new double[] {0.2176, 0.0103, 0.1109, 0.3456, 0.9910, 0.2323, 0.9211, 0.0322, 0.1211, 0.5131, 0.7208, 0.9172, 0.9922, 0.8324, 0.5011, 0.2931});

        scheduler = new Scheduler();

        for (int i = 0; i < initialState.length; i++) {
            if (initialState[i]!=0) scheduler.addAction(initialState[i], 1, -1, i);
        }

        for (Action action = scheduler.nextAction(); rdn.index() < rdn.size(); action = scheduler.nextAction()) {

            for (Queue line : queues) {
                line.saveTime(action.getTime());
            }
            if (action.getAction() == 1) {
                for (Queue line : queues) {
                    if (line.isInitial()) {
                        scheduler = line.chegada(rdn, scheduler);
                        rdn = line.getGenerator();
                    }
                }

            } else if (action.getAction() == 0) {
                Queue line = queues.get(action.getOrigin());
                scheduler = line.saida(rdn, scheduler);
                rdn = line.getGenerator();
            } else {
                Queue origin = queues.get(action.getOrigin());
                Queue destination = queues.get(action.getDestination());
                scheduler = origin.saida(rdn, scheduler);
                rdn = origin.getGenerator();
                scheduler = destination.chegada(rdn, scheduler);
                rdn = destination.getGenerator();
            }

        }

        System.out.println("\n\nResultados da Simulação\n");
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n");

        for (Queue line : queues) {
            System.out.println("Fila " + line.getIndex() + " - G/G/" + line.getServers() + "/" + line.getCapacity());
            
            if (line.getMinArrival()==0 && line.getMaxArrival()==0) System.out.printf("%-20s %s\n", "Chegada", "Inexistente");
            else System.out.printf("%-20s %s\n", "Chegada", (line.getMinArrival() + " ... " + line.getMaxArrival()));


            System.out.printf("%-20s %s\n\n", "Atendimento", (line.getMinExit() + " ... " + line.getMaxExit()));
            System.out.printf("%-20s %-20s %s\n", "Estado", "Tempo", "Porcentagem");

            for (int i = 0; i < line.getState().size(); i++) {
                System.out.printf("%-20s %-20s %s\n",i , (format((double)line.getState().get(i))  + " ut"), format(((double)line.getState().get(i) / line.getTime()) * 100) + "%");
            }
            System.out.printf("%-20s %-20s %s\n", "Total", (format(line.getTime()) + " ut"), "100%");
            
            System.out.printf("\nNúmero de Perdas: " + line.getLosses() + "\n\n");
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n");
            
        }

    }

    public static double format(double number) {
        return Math.floor(number * 1000) / 1000;
    }
}
