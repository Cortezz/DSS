package exceptions;

/**Excepção para tratar casos em que um donativo não existe.
 * 
 * @author Jorge Caldas, José Cortez, Marcelo Gonçalves, Ricardo Silva
 * @version 2015.01.07
 */
public class EventoNaoExisteException extends Exception {
    
    public EventoNaoExisteException (int nr)
    {
        super ("O evento com o número "+nr+" não existe.");
    }
    
    public EventoNaoExisteException (String nome)
    {
        super ("O evento com o nome '"+nome+"' não existe");
    }
    
}
