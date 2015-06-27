package data_access;

import business.doacoes.IDoador;
import business.doacoes.IDonativo;
import business.doacoes.IDonativoMonetario;
import business.doacoes.IEvento;
import business.familias.ICandidatura;
import business.familias.IMembro;
import business.familias.IRepresentante;
import business.projetos.IMaterial;
import business.projetos.IProjeto;
import business.projetos.ITarefa;
import business.recursoshumanos.IEquipa;
import business.recursoshumanos.IFuncionario;
import business.recursoshumanos.IVoluntario;
import exceptions.CandidaturaNaoExisteException;
import exceptions.DoadorNaoExisteException;
import exceptions.DonativoNaoExisteException;
import exceptions.EquipaNaoExisteException;
import exceptions.EventoNaoExisteException;
import exceptions.FuncionarioNaoExisteException;
import exceptions.MaterialNaoExisteException;
import exceptions.MembroNaoExisteException;
import exceptions.ProjetoNaoExisteException;
import exceptions.RepresentanteNaoExisteException;
import exceptions.TarefaNaoExisteException;
import exceptions.VoluntarioNaoExisteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**Curta descrição:
 * Facade do sistema de gestão da Habitat através do qual se acedem a todas as funcionalidades oferecidas pela camada de negócio
 * do projeto, assim como a toda a base de dados que suporta a aplicação pelo que esta classe é também
 * o DataAcessFacade do Sistema.
 * 
 * Detalhe do facade:
 * Neste facade fazemos a gestão das 4 principais componentes da camada de negócio:
 *  - Gestão de donativos e doadores e outros relacionados com doacoes;
 *  - Gestão de famílias que se candidatam;
 *  - Gestão de projetos, das obras e respetivas etapas;
 *  - Gestão dos recusos humanos da Habitat;
 * 
 * @author Jorge Caldas, José Cortez, Marcelo Gonçalves, Ricardo Silva
 * @version 2014.12.31
 */

public class SGHabitat {
    
    // Variáveis de instância
    private IFuncionario funcionario;
    
    /*Relativo a doacoes*/
    private final DoadorDAO doadores;
    private final DonativoDAO donativos;
    private final EventoDAO eventos;

    /*Relativo a familias*/
    private final CandidaturaDAO candidaturas;
    private final MembroDAO membros;
    private final RepresentanteDAO representantes;
    
    /*Relativo a projetos*/
    private final MaterialDAO materiais;
    private final ProjetoDAO projetos;
    private final TarefaDAO tarefas;
    
    /*Relativo a recursos humanos*/
    private final EquipaDAO equipas;
    private final FuncionarioDAO funcionarios;
    private final VoluntarioDAO voluntarios;
    
    
    /**
     * Construtor vazio para o facade da camada de negócio da aplicação.
     * @throws ConnectionErrorException 
     */
    public SGHabitat() throws ConnectionErrorException {
        this.funcionarios = new FuncionarioDAO();
        this.doadores = new DoadorDAO(); this.donativos = new DonativoDAO(); this.eventos = new EventoDAO();
        this.candidaturas = new CandidaturaDAO(); this.membros = new MembroDAO(); this.representantes = new RepresentanteDAO();
        this.materiais = new MaterialDAO(); this.projetos = new ProjetoDAO(); this.tarefas = new TarefaDAO();
        this.equipas = new EquipaDAO(); this.voluntarios = new VoluntarioDAO();
    }
    
    /**
     * Efetua autenticação de um funcionário no sistema.
     * @param username
     * @param password
     * @return 0 - funcionário não encontrado, erro; 1 - username incorreto; 2 - password incorreta; 3 - Log in bem sucedido.
     * @throws data_access.ConnectionErrorException
     */
    public int logIn(String username, String password) throws ConnectionErrorException {
        IFuncionario f = this.funcionarios.getByUsername(username);
        if(f==null){
            return 1; // username incorreto (1)
        } else{
            if(!f.getPassword().equals(password)){
                return 2; // pass incorreta (2)
            } else{
                this.funcionario=f;
                return 3; // campos válidos! (3)
            }
        }
    }
    
    /**
     * Método que permite fechar todas as ligações abertas à base de dados da aplicação
     */
    public void closeDataBaseConnections() {
        this.doadores.close(); this.donativos.close(); this.eventos.close();
        this.candidaturas.close(); this.membros.close(); this.representantes.close();
        this.materiais.close(); this.tarefas.close(); this.projetos.close();
        this.voluntarios.close(); this.equipas.close(); this.funcionarios.close();
    }
    
    
    
    /**************************************************************************************/
    /********************************** DOACOES *******************************************/
    /**************************************************************************************/
    
                                    /*Métodos de DoadorDAO*/
    /**
     * Calcula o número total de doadores no sistema.
     * @return Número total de doadores.
     */
    public int do_totalDoadores () { return this.doadores.size();}
    
    /**
     * Retorna um doador, dado um NIF.
     * @param nif, NIF do doador.
     * @return Doador correspondente ao NIF passado como parâmetro.
     * @throws exceptions.DoadorNaoExisteException 
     */
    public IDoador do_getDoador (String nif) throws DoadorNaoExisteException
    {
        if(this.doadores.containsKey(nif)){
            return this.doadores.get(nif);
        } else throw new DoadorNaoExisteException(nif);        
    }
    
    /**
     *  Averigua se exista um doador, através do seu NIF.
     * @param nif, NIF do doador.
     * @return Verdadeiro se existir, caso contrário falso.
     */
    public boolean do_doadorExiste(String nif){return this.doadores.containsKey(nif);}
    
    
    /**
     * Elimina um doador, dado um NIF.
     * @param nif, NIF dum doador.
     * @return Verdadeiro, caso seja removido com sucesso e falso, caso contrário.
     */
    public boolean do_remDoador(String nif) {
        if(this.doadores.containsKey(nif))
        {
            this.doadores.remove(nif);
            return true;
        } 
        else return false;
    }
    
    /**
     * Grava um doador na base de dados.
     * @param d, Um doador.
     * @return Verdadeiro, caso seja removido com sucesso ou falso, caso contrário.
     */
    public boolean do_saveDoador(IDoador d){
         if(!this.doadores.containsKey(d.getNIF()))
         {
            this.doadores.put(d.getNIF(), d);
            return true;
         } 
         else return false;
    }
    
    /**
     * Atualiza a informação sobre um dado doador.
     * @param d Um doador.
     * @return Verdadeiro, caso a edição seja efectuada com sucesso ou falso, caso contrário.
     */
    public boolean do_editDoador(IDoador d) {
        if(this.doadores.containsKey(d.getNIF()))
        {
            this.doadores.put(d.getNIF(), d);
            return true;
        } 
        return false;
    }
    
    /**
     * Calcula o dinheiro total doado por um doador, dado o seu NIF.
     * @param nif NIF dum doador.
     * @return O montante total.
     */
    public float do_totalDoadoPorUmDoador(String nif) {
        if (this.doadores.containsKey(nif))
        {
            float total = 0;
            IDoador d = this.doadores.get(nif);
            Set<Integer> don = d.getDonativos();
            for (Integer i: don)
            {
                IDonativo id = this.donativos.get(i);
                if (id instanceof IDonativoMonetario)
                {
                    IDonativoMonetario idm = (IDonativoMonetario) id;
                    total += idm.getValor();
                }
                
            }
            return total;
        }
        
        return 0;
    }
    
    
                                            /*Métodos de DonativoDAO*/
    
    /**
     * Calcula o número total de donativos no sistema.
     * @return  Número total de donativos.
     */
    public int do_totalDonativos () { return this.donativos.size();}
    
    public IDonativo do_getDonativo (int nr) throws DonativoNaoExisteException{
        if (this.donativos.containsKey(nr))
            return this.donativos.get(nr);
        else throw new DonativoNaoExisteException(nr);
        
    }
    
    
    /**
     * Averigua se um donativo, pesquisado pelo seu número de recibo, existe.
     * @param nr Número de recibo dum donativo.
     * @return Verdadeiro, caso o donativo exista ou falso, caso contrário.
     */
    public boolean do_donativoExiste (int nr) {return this.donativos.containsKey(nr);}
    
    
    /**
     * Remove um donativo, dado o seu número de recibo.
     * @param nr Número de recibo dum donativo.
     * @return Verdadeiro, caso a remoção seja efectuada com sucesso ou falso, caso contrário.
     */
    public boolean do_remDonativo (int nr){
        if (this.donativos.containsKey(nr))
        {
            this.donativos.remove(nr);
            return true;
        }
        else return false;
    }
    
    /**
     * Guarda um donativo na base de dados.
     * @param d Um donativo.
     * @return Verdadeiro, caso a operação seja feita com sucesso ou falso, caso contrário.
     */
    public boolean do_saveDonativo (IDonativo d){
        if(!this.donativos.containsKey(d.getNRecibo()))
        {
            this.donativos.put(d.getNRecibo(), d);
            return true;
        } 
        else return false;
    }
    
    
    /**
     * Atualiza um donativo, dado o seu número de recibo.
     * @param d Número de recibo dum donativo.
     * @return Verdadeiro, caso a operação seja efectuada com sucesso ou falso, caso contrário.
     */
    public boolean do_editDonativo (IDonativo d){
        if (this.donativos.containsKey(d.getNRecibo()))
        {
            this.donativos.put(d.getNRecibo(),d);
            return true;
        }
        return false;
    }
    
                                                    /* Métodos de EventoDAO*/
    
    
    
    /**
     * Calcula o número total de eventos.
     * @return Número total de eventos registados no sistema.
     */
    public int do_totalEventos () { return this.eventos.size();}
    
   
    /**
     * Retorna um evento pelo seu número.
     * @param nr Número dum evento.
     * @return Evento correspondente ao número passado como argumento. Caso não exista lança excepção.
     * @throws exceptions.EventoNaoExisteException 
     */
    public IEvento do_getEvento (int nr) throws EventoNaoExisteException
    {
        if (this.eventos.containsKey(nr))
            return this.eventos.get(nr);
        else throw new EventoNaoExisteException(nr);
    }
    
    /**
     * Averigua se um dado evento existe, através do seu número.
     * @param nr Número dum evento.
     * @return Verdadeiro, caso o evento exista ou falso, caso contrário.
     */
    public boolean do_eventoExiste (int nr) { return this.eventos.containsKey(nr);}
    
    
    /**
     * Remove um evento, dado o seu número.
     * @param nr Número do evento
     * @return Verdadeiro, caso a remoção seja efectuada com sucesso ou  falso, caso contrário.
     */
    public boolean do_remEvento (int nr){
        if (this.eventos.containsKey(nr))
        {
            this.eventos.remove(nr);
            return true;
        }
        return false;
    }
    
    
     /**
     * Guarda um evento na base de dados.
     * @param e Um evento.
     * @return Verdadeiro, caso a operação seja feita com sucesso ou falso, caso contrário.
     */
    public boolean do_saveEvento (IEvento e){
        if(!this.eventos.containsKey(e.getNr()))
        {
            this.eventos.put(e.getNr(), e);
            return true;
        } 
        else return false;
    }
    
    
    /**
     * Atualiza um evento, dado o seu número.
     * @param e Número de recibo dum donativo.
     * @return Verdadeiro, caso a operação seja feita com sucesso ou falso, caso contrário.
     */
    public boolean do_editEvento (IEvento e){
        if (this.eventos.containsKey(e.getNr()))
        {
            this.eventos.put(e.getNr(),e);
            return true;
        }
        return false;
    }
    
    
   
    /**************************************************************************************/
    /********************************* PROJETOS *******************************************/
    /**************************************************************************************/
    
    /* Método de ProjetoDAO */    
    /**
     * Calcula nº total de projetos registados no sistema.
     * @return nº total de projetos registados
     */
    public int pr_totalProjetos(){return this.projetos.size();}
    
    /**
     * Retorna um projeto pelo seu nº.
     * @param nr, nº de projeto.
     * @return Projeto correspondente ao nº passado como argumento, caso não exista lança excepção.
     * @throws exceptions.ProjetoNaoExisteException
     */
    public IProjeto pr_getProjeto(int nr) throws ProjetoNaoExisteException {
        if(this.projetos.containsKey(nr)){
            return this.projetos.get(nr);
        } else throw new ProjetoNaoExisteException(nr);
    }
    
    /**
     * Verifica se um projeto com uma dada chave existe.
     * @param nr, chave de projeto que queremos verificar se tem projeto associado.
     * @return true caso projeto com a chave exista, false caso contrário.
     */
    public boolean pr_projetoExiste(int nr){return this.projetos.containsKey(nr);}
    
    /**
     * Elimina um dado projeto.
     * @param nr, nº do projeto que pretendemos remover.
     * @return true caso remoção seja bem sucedida, false caso contrário.
     */
    public boolean pr_remProjeto(int nr) {
        if(this.projetos.containsKey(nr)){
            this.projetos.remove(nr);
            return true;
        } else return false;
    }
    
    /**
     * Guardar um projeto na base de dados.
     * @param p, um projeto que se pretende registar na base de dados do sistema.
     * @return true caso seja guardado com sucesso false em caso contrário.
     */
    public boolean saveProjeto(IProjeto p){
         if(!this.projetos.containsKey(p.getNr())){
            this.projetos.put(p.getNr(), p);
            return true;
        } else return false;
    }
    
    /**
     * Atualiza um projeto já registado no sistema.
     * @param p, projeto alterado que queremos atualizar na base de dados.
     * @return true caso projeto seja atualizado, false caso contrário. 
     */
    public boolean pr_editProjeto(IProjeto p) {
        if(this.projetos.containsKey(p.getNr())){
            this.projetos.put(p.getNr(), p);
            return true;
        } return false;
    }
    
    /**
     * Edita/Atualiza o registo de um projeto.
     * @param nr, nº do projeto que queremos atualizar/editar.
     * @param p, instância desse projeto.
     * @return true caso se verifiquem alterações no registo do projeto, false caso contrário.
     */
    public boolean updateProjeto(int nr, IProjeto p) {
        if(this.projetos.containsKey(nr)){
            this.projetos.put(p.getNr(), p);
            return true;
        } return false;
    }
    
    /* Método de MaterialDAO */    
    /**
     * Calcula nº total de materiais registados no sistema.
     * @return nº total de materiais registados
     */
    public int pr_totalMateriais(){return this.materiais.size();}
    
    /**
     * Retorna um material pelo seu nº.
     * @param nr, nº de meterial.
     * @return Material correspondente ao nº passado como argumento, caso não exista lança excepção.
     * @throws exceptions.MaterialNaoExisteException
     */
    public IMaterial pr_getMaterial(int nr) throws MaterialNaoExisteException {
        if(this.materiais.containsKey(nr)){
            return this.materiais.get(nr);
        } else throw new MaterialNaoExisteException(nr);
    }
    
    /**
     * Verifica se um material com uma dada chave existe.
     * @param nr, chave de material que queremos verificar se tem material associado.
     * @return true caso material com a chave exista, false caso contrário.
     */
    public boolean pr_materialExiste(int nr){return this.materiais.containsKey(nr);}
    
    /**
     * Elimina um dado material.
     * @param nr, nº do material que pretendemos remover.
     * @return true caso remoção seja bem sucedida, false caso contrário.
     */
    public boolean pr_remMaterial(int nr) {
        if(this.materiais.containsKey(nr)){
            this.materiais.remove(nr);
            return true;
        } else return false;
    }
    
    /**
     * Guardar um material na base de dados.
     * @param m, um material que se pretende registar na base de dados do sistema.
     * @return true caso seja guardado com sucesso false em caso contrário.
     */
    public boolean pr_saveProjeto(IMaterial m){
         if(!this.materiais.containsKey(m.getId())){
            this.materiais.put(m.getId(), m);
            return true;
        } else return false;
    }
    
    /**
     * Atualiza um material já registado no sistema.
     * @param m, material alterado que queremos atualizar na base de dados.
     * @return true caso material seja atualizado, false caso contrário. 
     */
    public boolean pr_editMaterial(IMaterial m) {
        if(this.materiais.containsKey(m.getId())){
            this.materiais.put(m.getId(), m);
            return true;
        } return false;
    }
    
    /**
     * Retorna um set de materiais criados com base num campo de pesquisa.
     * @param searchinput, nome do material
     * @return set de materiais que preenchem os requisitos de pesquisa (vazio, não encontramos equipa(s)).
     * @throws exceptions.MaterialNaoExisteException
     */
    public Set<IMaterial> pr_searchEquipa(String searchinput) throws MaterialNaoExisteException {
        Set<IMaterial> set = new HashSet<>();
        String lowersearch = searchinput.trim().toLowerCase();
        for(IMaterial e : this.materiais.values()){
            if(e.getNome().toLowerCase().contains(lowersearch)){
                 set.add(e);
            }
        }
        return set;
    }
    
    /* Método de TarefaDAO */    
    /**
     * Calcula nº total de tarefas registadas no sistema.
     * @return nº total de tarefas registadas
     */
    public int pr_totalTarefas(){return this.tarefas.size();}
    
    /**
     * Retorna uma tarefa pelo seu nº.
     * @param nr, nº de tarefa.
     * @return Tarefa correspondente ao nº passado como argumento, caso não exista lança excepção.
     * @throws exceptions.TarefaNaoExisteException
     */
    public ITarefa pr_getTarefa(int nr) throws TarefaNaoExisteException {
        if(this.tarefas.containsKey(nr)){
            return this.tarefas.get(nr);
        } else throw new TarefaNaoExisteException(nr);
    }
    
    /**
     * Verifica se uma tarefa com uma dada chave existe.
     * @param nr, chave de tarefa que queremos verificar se tem tarefa associado.
     * @return true caso tarefa com a chave exista, false caso contrário.
     */
    public boolean pr_tarefaExiste(int nr){return this.tarefas.containsKey(nr);}
    
    /**
     * Elimina uma dada tarefa.
     * @param nr, nº da tarefa que pretendemos remover.
     * @return true caso remoção seja bem sucedida, false caso contrário.
     */
    public boolean pr_remTarefa(int nr) {
        if(this.tarefas.containsKey(nr)){
            this.tarefas.remove(nr);
            return true;
        } else return false;
    }
    
    /**
     * Guardar uma tarefa na base de dados.
     * @param t, uma tarefa que se pretende registar na base de dados do sistema.
     * @return true caso seja guardado com sucesso false em caso contrário.
     */
    public boolean pr_saveTarefa(ITarefa t){
         if(!this.tarefas.containsKey(t.getId())){
            this.tarefas.put(t.getId(), t);
            return true;
        } else return false;
    }
    
    /**
     * Atualiza uma tarefa já registada no sistema.
     * @param t, tarefa alterada que queremos atualizar na base de dados.
     * @return true caso tarefa seja atualizada, false caso contrário. 
     */
    public boolean pr_editTarefa(ITarefa t) {
        if(this.tarefas.containsKey(t.getId())){
            this.tarefas.put(t.getId(), t);
            return true;
        } return false;
    }
    
    /**
     * Retorna um set de tarefas criadas com base num campo de pesquisa.
     * @param searchinput, designacao da tarefa
     * @return set de tarefas que preenchem os requisitos de pesquisa (vazio, não encontramos tarefa(s)).
     * @throws exceptions.TarefaNaoExisteException
     */
    public Set<ITarefa> pr_searchTarefa(String searchinput) throws TarefaNaoExisteException {
        Set<ITarefa> set = new HashSet<>();
        String lowersearch = searchinput.trim().toLowerCase();
        for(ITarefa t : this.tarefas.values()){
            if(t.getDesig().toLowerCase().contains(lowersearch)){
                 set.add(t);
            }
        }
        return set;
    }
    
    
    
    /**************************************************************************************/
    /********************************* FAMÍLIAS *******************************************/
    /**************************************************************************************/
    
    /**
     * Total de candidaturas.
     * @return total de candidaturas registadas no sistema de inf. da Habitat. 
     */
    public int fm_nrCandidaturas(){return candidaturas.size();}
    
    /**
     *  Regista uma candidatura
     * @param c, uma candidatura que queremos registar na base de dados 
     */
  /**
     * Retorna uma Candidatura pelo seu nº.
     * @param nr, nº da Candidatura.
     * @return 
     * @throws exceptions.CandidaturaNaoExisteException
     */
    public ICandidatura fm_getCandidatura(int nr) throws CandidaturaNaoExisteException {
        if(this.candidaturas.containsKey(nr)){
            return this.candidaturas.get(nr);
        } else throw new CandidaturaNaoExisteException(nr);
    }
    
    /**
     * Verifica se uma Candidatura com uma dada chave existe.
     * @param nr, queremos verificar se candidatura existe.
     * @return true caso candidatura com a chave exista, false caso contrário.
     */
    public boolean fm_CandidaturaExiste(int nr){return this.candidaturas.containsKey(nr);}
    
    /**
     * Elimina uma dada Candidatura.
     * @param nr, nº da Candidatura que pretendemos remover.
     * @return true caso remoção seja bem sucedida, false caso contrário.
     */
    public boolean fm_remCandidatura(int nr) {
        if(this.candidaturas.containsKey(nr)){
            this.candidaturas.remove(nr);
            return true;
        } else return false;
    }
    
    /**
     * Guarda uma Candidatura na base de dados.
     * @param c, instância da Candidatura que queremos guardar na base de dados.
     * @return true caso o registo seja guardado, false caso contrário.
     */
    public boolean fm_saveCandidatura(ICandidatura c){
        if(!this.candidaturas.containsKey(c.getNr())){
            this.candidaturas.put(c.getNr(), c);
            return true;
        } else return false;
    }
    
    /**
     * Atualiza uma Candidatura já registado no sistema.
     * @param c, voluntário alterado que queremos atualizar na base de dados.
     * @return true caso voluntário seja atualizado, false caso contrário. 
     */
    public boolean fm_editCandidatura(ICandidatura c) {
        if(this.candidaturas.containsKey(c.getNr())){
            this.candidaturas.put(c.getNr(), c);
            return true;
        } return false;
    }
    /**
    * 
    * @param searchinput
    * @return
    * @throws CandidaturaNaoExisteException 
    */
    public Set<ICandidatura> fm_searchCandidatura(String searchinput) throws CandidaturaNaoExisteException {
        Set<ICandidatura> set = new HashSet<>();
        String lowersearch = searchinput.trim().toLowerCase();
        for(ICandidatura e : this.candidaturas.values()){
            if(e.getDescricao().toLowerCase().contains(lowersearch)){
                 set.add(e);
            }
        }
        return set;
    }
    
    /*Método de MembroDAO*/    
    /**
     * Calcula nº total de membros registadas no sistema.
     * @return nº total de membros registadas
     */
    public int fm_totalMembros(){return this.membros.size();}
    
    /**
     * Retorna um Membro pelo seu id.
     * @param id, id do membro.
     * @return Equipa correspondente ao id passado como argumento, caso não exista lança excepção.
     * @throws exceptions.MembroNaoExisteException
     */
    public IMembro rh_getMembro(int id) throws MembroNaoExisteException {
        if(this.membros.containsKey(id)){
            return this.membros.get(id);
        } else throw new MembroNaoExisteException(id);
    }
    
    /**
     * Verifica se um Membro com um dada chave existe.
     * @param id, verifica se Membro existe.
     * @return true caso Membro com a chave exista, false caso contrário.
     */
    public boolean fm_MembroExiste(int id){return this.membros.containsKey(id);}

    /**
     * Retorna um set de equipas criados com base num campo de pesquisa.
     * @param searchinput, país de origem da equipa.
     * @return set de equipas que preenchem os requisitos de pesquisa (vazio, não encontramos equipa(s)).
     * @throws exceptions.MembroNaoExisteException
     */
    public Set<IMembro> fm_searchMembro(String searchinput) throws MembroNaoExisteException {
        Set<IMembro> set = new HashSet<>();
        String lowersearch = searchinput.trim().toLowerCase();
        for(IMembro e : this.membros.values()){
            if(e.getNome().toLowerCase().contains(lowersearch)){
                 set.add(e);
            }
        }
        return set;
    }
    
    /**
     * Elimina um dado Membro.
     * @param id, nº do Membro que pretendemos remover.
     * @return true caso remoção seja bem sucedida, false caso contrário.
     */
    public boolean fm_remMembro(int id) {
        if(this.membros.containsKey(id)){
            this.membros.remove(id);
            return true;
        } else return false;
    }
    
    /**
     * Guarda um Membro na base de dados.
     * @param m, instância de Membro que queremos guardar na base de dados.
     * @return true caso o registo seja guardado, false caso contrário.
     */
    public boolean fm_saveMembro(IMembro m){
        if(!this.membros.containsKey(m.getId())){
            this.membros.put(m.getId(), m);
            return true;
        } else return false;
    }
    
    /**
     * Atualiza um Membro já registado no sistema.
     * @param m, Membro alterado que queremos atualizar na base de dados.
     * @return true caso equipa seja atualizada, false caso contrário. 
     */
    public boolean fm_editMembro(IMembro m ) {
        if(this.membros.containsKey(m.getId())){
            this.membros.put(m.getId(), m);
            return true;
        } return false;
    }
  
    /**
     * Conjunto de Membro de uma dada Candidatura.
     * @param id, 
     * @return Set de Membros que pertencem à Candidatura associada ao id.
     */
    public Set<IMembro> rh_getMembrosDaCandidatura(int id) {
        return new HashSet<>(this.membros.getMembrosCandidatura(id));
    }
    
    
    // RepresentanteDAO
    
    
     /**
     * Total de representantes.
     * @return total de representantes registadas no sistema de inf. da Habitat. 
     */
    public int fm_nrRepresentantes(){return representantes.size();}
    
    /**
     * Retorna um Representante pelo seu nº.
     * @param nr, nº da Representante.
     * @return 
     * @throws exceptions.RepresentanteNaoExisteException
     */
    public IRepresentante fm_getRepresentante(int nr) throws RepresentanteNaoExisteException {
        if(this.representantes.containsKey(nr)){
            return this.representantes.get(nr);
        } else throw new RepresentanteNaoExisteException(nr);
    }
    
    /**
     * Verifica se um Representante com uma dada chave existe.
     * @param nr, queremos verificar se Representante existe.
     * @return true caso Representante com a chave exista, false caso contrário.
     */
    public boolean fm_RepresentanteExiste(int nr){return this.representantes.containsKey(nr);}
    
    /**
     * Elimina um dado Representante.
     * @param nr, nº de Representante que pretendemos remover.
     * @return true caso remoção seja bem sucedida, false caso contrário.
     */
    public boolean fm_remRepresentante(int nr) {
        if(this.representantes.containsKey(nr)){
            this.representantes.remove(nr);
            return true;
        } else return false;
    }
    
    /**
     * Guarda um Representante na base de dados.
     * @param c.
     * @return true caso o registo seja guardado, false caso contrário.
     */
    public boolean fm_saveRepresentante(IRepresentante c){
        if(!this.representantes.containsKey(c.getNr())){
            this.representantes.put(c.getNr(), c);
            return true;
        } else return false;
    }
    
    /**
     * Atualiza uma Candidatura já registado no sistema.
     * @param c, voluntário alterado que queremos atualizar na base de dados.
     * @return true caso voluntário seja atualizado, false caso contrário. 
     */
    public boolean fm_editRepresentante(IRepresentante c) {
        if(this.representantes.containsKey(c.getNr())){
            this.representantes.put(c.getNr(), c);
            return true;
        } return false;
    }
    
    /**
     * 
     * @param searchinput
     * @return
     * @throws RepresentanteNaoExisteException 
     */
    public Set<IRepresentante> fm_searchRepresentante(String searchinput) throws RepresentanteNaoExisteException {
        Set<IRepresentante> set = new HashSet<>();
        String lowersearch = searchinput.trim().toLowerCase();
        for(IRepresentante e : this.representantes.values()){
            if(e.getNome().toLowerCase().contains(lowersearch)){
                 set.add(e);
            }
        }
        return set;
    }
    
    
    /**************************************************************************************/
    /****************************** RECURSOS HUMANOS **************************************/
    /**************************************************************************************/
    
    /*Seguintes métodos do DAO da classe voluntário: size(), put(), get(), remove(); (..., para já)*/
    
    /*Método de VoluntarioDAO*/    
    /**
     * Calcula nº total de voluntários registados no sistema.
     * @return nº total de voluntários registados
     */
    public int rh_totalVoluntarios(){return this.voluntarios.size();}
    
    /**
     * Verifica se existem voluntários na base.
     * @return true caso exista pelo menos um voluntário na base de dados, false caso contrário. 
     */
    public boolean rh_isVoluntariosEmpty(){return this.voluntarios.isEmpty();}
    
    /**
     * Retorna um voluntário pelo seu nº.
     * @param nr, nº de voluntário.
     * @return Voluntário correspondente ao nº passado como argumento, caso não exista lança excepção.
     * @throws exceptions.VoluntarioNaoExisteException
     */
    public IVoluntario rh_getVoluntario(int nr) throws VoluntarioNaoExisteException {
        if(this.voluntarios.containsKey(nr)){
            return this.voluntarios.get(nr);
        } else throw new VoluntarioNaoExisteException(nr);
    }
    
    /**
     * Verifica se um voluntário com um dada chave existe.
     * @param nr, chave de voluntário que queremos verificar se tem voluntário associado.
     * @return true caso voluntário com a chave exista, false caso contrário.
     */
    public boolean rh_voluntarioExiste(int nr){return this.voluntarios.containsKey(nr);}

    /**
     * Retorna um set de voluntários criados com base num campo de pesquisa.
     * @param searchinput, String um nome (próprio ou apelido) de um ou mais voluntário.
     * @return set de voluntários que preenchem os requisitos de pesquisa (vazio, não encontramos voluntário(s)).
     * @throws exceptions.VoluntarioNaoExisteException
     */
    public Set<IVoluntario> rh_searchVoluntario(String searchinput) throws VoluntarioNaoExisteException {
        Set<IVoluntario> set = new HashSet<>();
        String lowersearch = searchinput.trim().toLowerCase();
        for(IVoluntario v : this.voluntarios.values()){
            if(v.getNome().toLowerCase().contains(lowersearch)){
                 set.add(v);
            }
        }
        return set;
    }
    
    /**
     * Elimina um dado voluntário.
     * @param nr, nº do voluntário que pretendemos remover.
     * @return true caso remoção seja bem sucedida, false caso contrário.
     */
    public boolean rh_remVoluntario(int nr) {
        return (this.voluntarios.remove(nr)!=null);
    }
    
    /**
     * Guarda um voluntário na base de dados.
     * @param v, instância de voluntário que queremos guardar na base de dados.
     * @return true caso o registo seja guardado, false caso contrário.
     */
    public boolean rh_saveVoluntario(IVoluntario v){
        return (this.voluntarios.put(v.getNr(),v)!=null);
    }
    
    /**
     * Gerar chave única para um novo funcionário.
     * @return chave única gerada automaticamente pelo DAO destinada a um novo voluntário 
     */
    public int rh_generateVoluntarioKey(){return this.voluntarios.generateVoluntarioKey();}

    /**
     * 
     * Inserir horas de voluntariado num dado voluntário.
     * @param nrproj id do proejto em que o voluntário trabalhou
     * @param vid id do voluntário
     * @param horas nº de horas de trabalho
     * @return true caso id de projeto exista e sejam inseridas as horas, false caso contrário
     */
    public boolean rh_addHorasVoluntariado(int nrproj, int vid, int horas){
        if(this.projetos.containsKey(nrproj)){
            this.voluntarios.addHorasVoluntariado(nrproj,vid,horas);
            return true;
        } else return false;
    }
    
    public boolean rh_containsVoluntario(IVoluntario v){return this.voluntarios.containsValue(v);}
    
    public Collection<IVoluntario> rh_voluntariosValues(){return this.voluntarios.values();}
    
    /*Método de EquipaDAO*/    
    /**
     * Calcula nº total de equipas registadas no sistema.
     * @return nº total de equipas registadas
     */
    public int rh_totalEquipas(){return this.equipas.size();}
    
    /**
     * Retorna um equipa pelo seu id.
     * @param id, id da equipa.
     * @return Equipa correspondente ao id passado como argumento, caso não exista lança excepção.
     * @throws exceptions.EquipaNaoExisteException
     */
    public IEquipa rh_getEquipa(int id) throws EquipaNaoExisteException {
        if(this.equipas.containsKey(id)){
            return this.equipas.get(id);
        } else throw new EquipaNaoExisteException(id);
    }
    
    /**
     * Verifica se um equipa com um dada chave existe.
     * @param id, chave de equipa que queremos eerificar se tem equipa associado.
     * @return true caso equipa com a chave exista, false caso contrário.
     */
    public boolean rh_equipaExiste(int id){return this.equipas.containsKey(id);}

    /**
     * Retorna um set de equipas criados com base num campo de pesquisa.
     * @param searchinput, país de origem da equipa.
     * @return set de equipas que preenchem os requisitos de pesquisa (vazio, não encontramos equipa(s)).
     * @throws exceptions.EquipaNaoExisteException
     */
    public Set<IEquipa> rh_searchEquipa(String searchinput) throws EquipaNaoExisteException {
        Set<IEquipa> set = new HashSet<>();
        String lowersearch = searchinput.trim().toLowerCase();
        for(IEquipa e : this.equipas.values()){
            if(e.getpOrigem().toLowerCase().contains(lowersearch)){
                 set.add(e);
            }
        }
        return set;
    }
    
    /**
     * Elimina uma dada equipa.
     * @param id, nº do equipa que pretendemos remover.
     * @return true caso remoção seja bem sucedida, false caso contrário.
     */
    public boolean rh_remEquipa(int id) {
        if(this.equipas.containsKey(id)){
            this.equipas.remove(id);
            return true;
        } else return false;
    }
    
    /**
     * Guarda uma equipa na base de dados.
     * @param e, instância de equipa que queremos guardar na base de dados.
     * @return true caso o registo seja guardado, false caso contrário.
     */
    public boolean rh_saveEquipa(IEquipa e){
        if(!this.equipas.containsKey(e.getId())){
            this.equipas.put(e.getId(), e);
            return true;
        } else return false;
    }
    
    /**
     * Atualiza uma equipa já registado no sistema.
     * @param e, equipa alterada que queremos atualizar na base de dados.
     * @return true caso equipa seja atualizada, false caso contrário. 
     */
    public boolean rh_editEquipa(IEquipa e) {
        if(this.equipas.containsKey(e.getId())){
            this.equipas.put(e.getId(), e);
            return true;
        } return false;
    }
    
    /**
     * Calcula set de voluntários que não estão associados a nenhuma equipa.
     * @return set de voluntários que não estão no presente associados a nenhuma equipa. 
     */
    public Set<IVoluntario> rh_getVoluntariosSemEquipa() {
        return new HashSet<>(this.voluntarios.getVoluntariosDeEquipa(-1));
    }
    
    /**
     * Conjunto de voluntários de uma dada equipa.
     * @param id, id da equipa do conjunto de voluntários.
     * @return Set de voluntários que pertencem à equipa associada ao id.
     */
    public Set<IVoluntario> rh_getVoluntariosDaEquipa(int id) {
        return new HashSet<>(this.voluntarios.getVoluntariosDeEquipa(id));
    }
    
    /**
     * Devolve o nome do chefe de um equipa.
     * @param id equipa sobre a qual pretendemos obter o nome do seu chefe.
     * @return String que é nome do chefe da equipa.
     */
    public String rh_getNomeChefeEq(int id){
        return this.voluntarios.get(this.equipas.get(id).getChefe()).getFirstAndLastName();
    }
    
    
    /*Métodos de FuncionarioDAO*/
    /**
     * Retorna um funcionário pelo seu username
     * @param user, username do suposto funcionário.
     * @return Equipa correspondente ao id passado como argumento, caso não exista lança excepção.
     * @throws exceptions.FuncionarioNaoExisteException
     */
    public IFuncionario rh_getFuncionario(String user) throws FuncionarioNaoExisteException {
        IFuncionario f = this.funcionarios.getByUsername(user);
        if(f!=null) return f;
        else throw new FuncionarioNaoExisteException(user);
    }
    
    /**
     * Fornece username de utilizador que está a interagir com o sistema.
     * @return username do funcionário com sessão iniciada. 
     */
    public String rh_getUsername(){return this.funcionario.getUsername();}
    
    /**
     * Fornece primeiro e último nome do utilizador que está a interagir com o sistema.
     * @return String com primeiro e último nome do funcionário. 
     */
    public String rh_getFuShortName(){return this.funcionario.getFirstAndLastName();}
    
    /**
     * Fornece funcionário com sessão iniciada.
     * @return IFuncionario, funcionário com sessão iniciada no sistema. 
     */
    public IFuncionario rh_getFuncLogged(){return this.funcionario;}
    
    /**
     * Verificar determinadas permissões para um funcionário.
     * @param perms, permissões que queremos averiguar.
     * @return true caso o funcionário possua as permissões false caso contrário.
     */
    public boolean rh_funHasPermissions(List<String> perms){return this.funcionario.hasPermissions(perms);}
    
    /**
     * Criar conjunto de chaves de todos os voluntários.
     * @return set que contém números de todos os voluntários registados na base. 
     */
    public Set<Integer> rh_voluntariosKeys(){return this.voluntarios.keySet();}
}
