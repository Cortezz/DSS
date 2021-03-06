package business.projetos;

/**Implementação de factory pattern que permite instanciar Tarefas do exterior.
 *
 * @author Jorge Caldas, José Cortez, Marcelo Gonçalves, Ricardo Silva
 * @version 29.12.2014
 */
public class TarefaFactory {
    public TarefaFactory () {}
    public ITarefa createTarefa() { return new Tarefa(); }
}
