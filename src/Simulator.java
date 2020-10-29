import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

public class Simulator {

    private static ArrayList<Queue> queues;
    private static Scheduler scheduler;
    private static double[] initialState;
    private static int m;
    private static double a;
    private static double c;
    private static double semente;

    /**
     * Executa as funcionalidades do simulador
     * 
     * @param args[0] Nome do arquivo que será lido
     * @author João Flores de Leão
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {

        if (args.length != 1) { // avisa as maneiras de uso da aplciação
            System.out.println("\nManeiras de uso:");

            System.out.printf("%-38s %s\n", "Executar ", "$java Simulator ./(nomeDoArquivo).txt");
            System.out.printf("%-38s %s\n", "Gerar arquivo de modelo ", "$java Simulator example");
            System.out.printf("%-40s %s\n", "Gerar arquivo de instrucoes ", "$java Simulator instructions\n");

            return;
        }

        if (args[0].equals("example")) { // cria um modelo de arquivo de entrada
            generateModel();
            System.out.println("Arquivo de modelo gerado");
            return;
        } else if (args[0].equals("instructions")) { // cria um arquivo de instruções
            generateInstructions();
            System.out.println("Arquivo de instrucoes gerado");
            return;
        }

        readFile(args[0]); // le o arquivo e salva as variaveis globalmente

        Generator rdn = new Generator(a, m, c, semente); // cria um objeto GENERATOR para que seja possivel a chamada de
                                                         // numeros pseudo-aleatorios

        scheduler = new Scheduler(); // cria escalonador

        for (int i = 0; i < initialState.length; i++) { // agenda todas as entradas iniciais
            if (initialState[i] != 0)
                scheduler.addAction(initialState[i], 1, -1, i);
        }

        for (Action action = scheduler.nextAction(); rdn.index() < rdn.size(); action = scheduler.nextAction()) {

            for (Queue line : queues) { // salva o tempo de todas as filas para realizar proxima ação
                line.saveTime(action.getTime());
            }

            if (action.getAction() == 1) { // executa a entrada em uma fila
                for (Queue line : queues) {
                    if (line.isInitial()) {
                        scheduler = line.chegada(rdn, scheduler);
                        rdn = line.getGenerator();
                    }
                }

            } else if (action.getAction() == 0) { // executa a saida de uma fila
                Queue line = queues.get(action.getOrigin());
                scheduler = line.saida(rdn, scheduler);
                rdn = line.getGenerator();
            } else { // executa a passagem de uma fila para outra
                Queue origin = queues.get(action.getOrigin());
                Queue destination = queues.get(action.getDestination());
                scheduler = origin.saida(rdn, scheduler);
                rdn = origin.getGenerator();
                scheduler = destination.chegadaDePassagem(rdn, scheduler);
                rdn = destination.getGenerator();
            }

        }

        printResults(); // prints all the results formatted

    }

    /**
     * Cria um arquivo de modelo para auxiliar o usuário a usar a aplicação
     * 
     * @author João Flores de Leão
     * @throws IOException
     */
    public static void generateModel() throws IOException {

        String fileData = "100000\n1103515245\n12345\n1\n1.0,0.0,0.0\n1,0,1,4,1,1.5\n0.0,0.8,0.2\n3,5,0,0,5,10\n0.3,0.0,0.5\n2,8,0,0,10,20\n0.0,0.7,0.0";
        FileOutputStream file = new FileOutputStream("model.txt");
        file.write(fileData.getBytes());
        file.flush();
        file.close();

    }

    /**
     * Cria um arquivo de instrucoes para auxiliar o usuário a usar a aplicação
     * 
     * @author João Flores de Leão
     * @throws IOException
     */
    public static void generateInstructions() throws IOException {

        String fileData = "Para executar com a sua própria simulação,\ncrie um arquivo no formato .txt com os seguintes valores nas linhas indicadas.\n"
                + "\nLinha 1: número de aleatórios que serão gerados [M]"
                + "\nLinha 2: semente que irá gerar os números pseudo-aleatórios [a]"
                + "\nLinha 3: número a ser adicionado ao gerar os números aleatórios [C]"
                + "\nLinha 4: valor inicial dos números aleatórios [semente]"
                + "\nLinha 5: valores que serão agendadas as primeiras chegadas nas filas. Inserir 0.0 caso não possua um primerio agendamento) [primeiraChegadaFila0, primeiraChegadaFila1,(...)]"
                + "\nLinha 6: dados de uma fila, separados por virgulas e indicando as seguintes informações [servidores,capacidade,minArrival,maxArrival,minService,maxService]"
                + "\nLinha 7: dados de probablidade de roteamento para cada uma das filas [probabilidadeFila0,probabilidadeFila1,(...)]"
                + "\nLinha n: repetir linha 6 até que todas as filas estejam adicionadas"
                + "\nLinha n+1: repetir linha 7 até que todos os roteamentos estejam adicionados";

        FileOutputStream file = new FileOutputStream("instructions.txt");
        file.write(fileData.getBytes());
        file.flush();
        file.close();

    }

    /**
     * Imprime no console os resultados da execução da simulação, apresentando as
     * filas e os seus estados.
     * 
     * @author João Flores de Leão
     */
    public static void printResults() {
        System.out.println("\n\nResultados da Simulacao\n");
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n");

        for (Queue line : queues) {
            System.out.println("Fila " + line.getIndex() + " - G/G/" + line.getServers() + "/" + line.getCapacity());

            if (line.getMinArrival() == 0 && line.getMaxArrival() == 0)
                System.out.printf("%-20s %s\n", "Chegada", "Inexistente");
            else
                System.out.printf("%-20s %s\n", "Chegada", (line.getMinArrival() + " ... " + line.getMaxArrival()));

            System.out.printf("%-20s %s\n\n", "Atendimento", (line.getMinExit() + " ... " + line.getMaxExit()));
            System.out.printf("%-20s %-20s %s\n", "Estado", "Tempo", "Porcentagem");

            for (int i = 0; i < line.getState().size(); i++) {
                System.out.printf("%-20s %-20s %s\n", i, (format((double) line.getState().get(i)) + " ut"),
                        format(((double) line.getState().get(i) / line.getTime()) * 100) + "%");
            }
            System.out.printf("%-20s %-20s %s\n", "Total", (format(line.getTime()) + " ut"), "100%");

            System.out.printf("\nNumero de Perdas: " + line.getLosses() + "\n\n");
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n");

        }
    }

    /**
     * Lê um arquivo e atribui os seus valores as variaveis globais.
     * 
     * @param fileName Nome do arquivo que será lido
     * @author João Flores de Leão
     * @throws FileNotFoundException
     */
    public static void readFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner reader = new Scanner(file);

        m = Integer.parseInt(reader.nextLine());
        a = Double.parseDouble(reader.nextLine());
        c = Double.parseDouble(reader.nextLine());
        semente = Double.parseDouble(reader.nextLine());

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

            queues.add(new Queue(i, Integer.parseInt(lineData[0]), Integer.parseInt(lineData[1]),
                    Double.parseDouble(lineData[2]), Double.parseDouble(lineData[3]), Double.parseDouble(lineData[4]),
                    Double.parseDouble(lineData[5]), connections));

        }
    }

    /**
     * Formata números em formato DOUBLE para apresentarem apenas 4 casas depois da
     * vírgula, arredondando-o para baixo.
     * 
     * @return Número formatado com 4 casas decimais.
     * @author João Flores de Leão
     */
    public static double format(double number) {
        return Math.floor(number * 1000) / 1000;
    }
}
