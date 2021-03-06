package business.projetos;

import java.util.*;

/**
 * Classe que representa uma Tarefa.
 * @author Jorge Caldas, José Cortez, Marcelo Gonçalves, Ricardo Silva
 * @version 29.12.2014
 */
class Tarefa implements ITarefa{
    private int id;
    private String designacao;
    private String descricao;
    private GregorianCalendar dataInicial;
    private GregorianCalendar dataFinal;
    private HashMap<Integer, Integer> materialgasto; 
    
    /**
     * Construtor vazio.
     */
    public Tarefa(){
        this.id = 0; 
        this.dataInicial = new GregorianCalendar();
        this.dataFinal = new GregorianCalendar(); 
        this.designacao = ""; 
        this.descricao = ""; 
        this.materialgasto = new HashMap<>();
    }

    /**
     * Construtor parameterizado.
     * @param id, identificador de uma tarefa
     * @param dataInicial, data em que a tarefa foi iniciada
     * @param dataFinal, data em que a tarefa foi terminada
     * @param designacao, designação da tarefa a desenvolver
     * @param materialgasto, material gasto na tarefa 
     */
    public Tarefa (int id, GregorianCalendar dataInicial, GregorianCalendar dataFinal, 
            String designacao, String descricao, Map<Integer, Integer> materialgasto){
        this.id = id; 
        this.dataInicial = dataInicial; 
        this.dataFinal = dataFinal; 
        this.designacao = designacao;
        this.descricao = descricao; 
        this.materialgasto = new HashMap<>();
        for(Map.Entry<Integer,Integer> entry : materialgasto.entrySet()){
            this.materialgasto.put(entry.getKey(),entry.getValue());
        }  
    }
    
    /**
     * Construtor de cópia.
     * @param t, uma tarefa.
     */
    public Tarefa (Tarefa t){
        this.id = t.getId(); 
        this.dataInicial = t.getDataInicioT(); 
        this.dataFinal = t.getDataFinalT(); 
        this.designacao = t.getDesig();
        this.materialgasto = t.getMaterial();
        this.descricao = t.getDesc(); 
    }
    
    /* Gets & Sets */
    @Override
    public int getId(){return this.id;}
    @Override
    public void setId(int id) {this.id = id;}
    @Override
    public GregorianCalendar getDataInicioT(){return this.dataInicial;}
    @Override
    public void setDataInicioT(GregorianCalendar data){this.dataInicial = data;}
    @Override
    public GregorianCalendar getDataFinalT(){return this.dataFinal;}
    @Override
    public void setDataFinalT(GregorianCalendar data){this.dataFinal = data;}
    @Override
    public String getDesig(){return this.designacao;}
    @Override
    public void setDesig(String designacao){this.designacao = designacao;}
    @Override
    public HashMap<Integer, Integer> getMaterial() { return this.materialgasto; }
    @Override
    public void setMaterial(HashMap<Integer,Integer> r) { this.materialgasto = r; }
    @Override
    public String getDesc() {return this.descricao; }  
    @Override
    public void setDesc(String desc) { this.descricao = desc; }        

    /* Equals e Clone */
    @Override
    public boolean equals (Object o){
        if(this==o) return true;
        
        else if(o==null || this.getClass()!=o.getClass()) return false;
        
        else{
            Tarefa t = (Tarefa) o;

            for(Map.Entry<Integer,Integer> h : t.getMaterial().entrySet()){
                if(!this.materialgasto.containsKey(h.getKey())) return false;
            }
            
            return( this.id == t.getId()
                    && this.dataInicial.equals(t.getDataInicioT()) 
                    && this.dataFinal.equals(t.getDataFinalT())
                    && this.designacao.equals(t.getDesig())
                    && this.descricao.equals(t.getDesc()) );
        }
    }
    
    @Override
    public ITarefa clone(){
        return new Tarefa(this);
    }
    
    @Override
    public int hashCode(){
        return Arrays.hashCode( new Object[] {this.id, this.dataInicial, this.dataFinal, this.designacao, this.descricao, this.materialgasto} );
    }
    
    /**
     * Método que permite saber se a tarefa ja terminou ou não
     * @return boolean, true se a tarefa já terminou
     */
    @Override
    public boolean isFinished(){
        return (!(dataFinal==null));
    }
    
}
