import java.util.Random;

public class Main {

    private static Scheduler scheduler = new Scheduler();
    private static double[] random;
    private static int randomCounter;
    private static double initialState;

    private static int m = 100000;

    public static void main(String args[]) {

        // random = new double[m];
        // Random rdn = new Random();
        // for (int i = 0; i < random.length; i++) {
        // random[i] = rdn.nextDouble();
        // }

        random = generateNumbers(Math.pow(2, 64), m, 2, 7);

        randomCounter = 0;

        initialState = 2.5;
        // capacity servers minArrival maxArrival minExit maxExit
        Queue fila = new Queue(random, 2, 3, 2, 3, 2, 5);
        Queue fila2 = new Queue(random, 1, 3, 0, 0, 3, 5);

        scheduler.addAction(initialState, 1);

        for (Action action = scheduler.nextAction(); randomCounter < m; action = scheduler.nextAction()) {

            fila.saveTime(action.getTime());
            fila2.saveTime(action.getTime());

            if (action.getAction() == 1) {
                randomCounter = fila.chegada(randomCounter, scheduler);
                scheduler = fila.getScheduler();
            } else if (action.getAction() == 0) {
                randomCounter = fila2.saida(randomCounter, scheduler);
                scheduler = fila2.getScheduler();
            } else {
                randomCounter = fila.saidaPassagem(randomCounter, scheduler);
                scheduler = fila.getScheduler();
                randomCounter = fila2.chegadaPassagem(randomCounter, scheduler);
                scheduler = fila2.getScheduler();
            }

        }

        System.out.println("Final State 1:");
        System.out.printf("%-20s %s\n", "Time", "Percentage");
        for (int i = 0; i < fila.getState().length; i++) {
            System.out.printf("%-20s %s\n", format(fila.getState()[i]),
                    format((fila.getState()[i] / fila.getTime()) * 100) + "%");
        }
        System.out.printf("%-20s %s\n", format(fila.getTime()), "100%");

        System.out.println("\nLosses:");
        System.out.printf("%-20s %s\n", "Number", "Percentage");
        System.out.printf("%-20s %s\n", fila.getLosses(), format((double) fila.getLosses() / (double) m * 100) + "%");

        System.out.println("\n\n\nFinal State 2:");
        System.out.printf("%-20s %s\n", "Time", "Percentage");
        for (int i = 0; i < fila2.getState().length; i++) {
            System.out.printf("%-20s %s\n", format(fila2.getState()[i]),
                    format((fila2.getState()[i] / fila2.getTime()) * 100) + "%");
        }
        System.out.printf("%-20s %s\n", format(fila2.getTime()), "100%");
    }

    public static String print(double[] vetor) {
        String text = "";

        for (int i = 0; i < vetor.length; i++) {
            text = text + format(vetor[i]) + ", ";
        }
        return text;

    }

    public static double[] generateNumbers(double a, int m, double x, double c) {
        double number = x;

        double list[] = new double[m];

        for (int index = 0; index < m; index++) {
            double result = (a * number + c) % m;

            list[index] = (result / m);
            number = result;
        }
        return list;
    }

    public static double format(double number) {
        return Math.floor(number * 100) / 100;
    }

}