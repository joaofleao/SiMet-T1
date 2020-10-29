
public class Action {

    private double time;
    private int action;
    private int destination;
    private int origin;

    /**
     * Método construtor do objeto
     * 
     * @param time        horario que a ação sera realizada
     * @param origin      onde essa ação está sendo realizada
     * @param action      qual ação esta sendo realizada (cheagda, saida ou
     *                    passagem)
     * @param destination para onde essa ação está levando o cliente
     * @author João Flores de Leão
     */

    public Action(double time, int action, int origin, int destination) {

        this.time = time;
        this.action = action;
        this.destination = destination;
        this.origin = origin;

    }

    /**
     * Método get de da variavel destination
     * 
     * @return variavel destination
     * @author João Flores de Leão
     */

    public int getDestination() {
        return destination;
    }

    /**
     * Método get de da variavel origin
     * 
     * @return variavel origin
     * @author João Flores de Leão
     */
    public int getOrigin() {
        return origin;
    }

    /**
     * Método set de da variavel destination
     * 
     * @param destination valor novo
     * @author João Flores de Leão
     */
    public void setDestination(int destination) {
        this.destination = destination;
    }

    /**
     * Método set de da variavel origin
     * 
     * @param origin valor novo
     * @author João Flores de Leão
     */
    public void setOrigin(int origin) {
        this.origin = origin;
    }

    /**
     * Método get de da variavel time
     * 
     * @return variavel time
     * @author João Flores de Leão
     */
    public double getTime() {
        return time;
    }

    /**
     * Método get de da variavel action
     * 
     * @return variavel action
     * @author João Flores de Leão
     */

    public int getAction() {
        return action;
    }

    /**
     * Método set de da variavel action
     * 
     * @param action valor novo
     * @author João Flores de Leão
     */
    public void setAction(int action) {
        this.action = action;
    }

    /**
     * Método set de da variavel time
     * 
     * @param time valor novo
     * @author João Flores de Leão
     */
    public void setTime(double time) {
        this.time = time;
    }

    /**
     * Método set de da variavel destination
     * 
     * @return objeto em formato de string
     * @author João Flores de Leão
     */

    public String toString() {
        if (action == 0)
            return "{S" + origin + " - " + time + "}";
        if (action == 1)
            return "{C" + origin + " - " + time + "}";

        return "{P" + origin + "" + destination + " - " + time + "}";

    }
}
