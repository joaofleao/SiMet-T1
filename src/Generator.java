import java.util.Random;

public class Generator {
    private double a;
    private int m;
    private double c;
    private double x;
    private double[] list;
    private Random gerador;
    private int index;

    /**
     * Método construtor do objeto se baseando em variáveis
     * 
     * @param a multiplicador do gerador
     * @param m valor maximo/quantidade de valores
     * @param c costante do gerador
     * @param x semente inicial
     * @author João Flores de Leão
     */

    public Generator(double a, int m, double c, double x) {
        this.a = a;
        this.m = m;
        this.c = c;
        this.x = x;
        this.index = 0;
    }

    /**
     * Método construtor alternativo do objeto se baseando no JavaRandom
     * 
     * @param m valor maximo/quantidade de valores
     * @author João Flores de Leão
     */

    public Generator(int m) {
        gerador = new Random();
        this.m = m;
    }

    /**
     * Método construtor alternativo do objeto se baseando em uma lista pré definida
     * 
     * @param list lista com aleatórios a serem utilizados
     * @author João Flores de Leão
     */

    public Generator(double[] list) {
        this.list = list;
        this.m = list.length;
    }

    /**
     * Método de obtenção de número aleatório
     * 
     * @return o próximo número aleatório a ser gerado
     * @author João Flores de Leão
     */

    public double nextNumber() {

        if (list != null) {
            x = list[index];
        } else if (gerador != null) {
            x = gerador.nextDouble();

        } else {
            x = ((a * x + c) % m);
            x = x / m;
        }
        index++;
        return format(x);

    }

    /**
     * Método de obtenção da quantidade de números que será gerada
     * 
     * @return a quantidade de números a ser gerado
     * @author João Flores de Leão
     */
    public int size() {
        return m;
    }

    /**
     * Método de obtenção do index do número aleatório criado
     * 
     * @return index do número gerado
     * @author João Flores de Leão
     */

    public int index() {
        return index;
    }

    /**
     * Método para fechar prematuramente o código do gerador
     * 
     * @author João Flores de Leão
     */

    public void finish() {
        index = m;
    }

    /**
     * Método para formatar numeros decimais
     * 
     * @param number numero nao formatado
     * @return numero formatado
     * 
     * @author João Flores de Leão
     */

    public double format(double number) {
        return Math.floor(number * 10000) / 10000;
    }

}
