package business.recursoshumanos;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**Classe que representa uma equipa, agregado de voluntários da instituição.
 *
 * @author Jorge Caldas, José Cortez, Marcelo Gonçalves, Ricardo Silva
 * @version 2014.12.29
 */

class Equipa implements IEquipa{
    
    // Variáveis de instância
    private int id;
    private String nome;
    private String pOrigem;
    private String obs;
    private int chefe; // nº do voluntário chefe de equipa   
    private Set<Integer> voluntarios; // nº's dos voluntários que trabalham na equipa
    
    /**
     * Construtor vazio
     */
    public Equipa(){
        this.id=0; this.nome="";
        this.pOrigem=""; this.obs="";
        this.chefe=0; this.voluntarios = new HashSet<>();
    }
    
    /**
     * Construtor parameterizado.
     * @param id
     * @param nome
     * @param pOrgigem, país de origem dos voluntários desta equipa.
     * @param obs, observações da equipa.
     * @param chefe, id do voluntário chefe de equipa.
     * @param vols, ids dos voluntários de equipa.
     */
    public Equipa(int id, String nome, String pOrgigem, String obs, int chefe, Set<Integer> vols){
        this.id=id; this.nome=nome;
        this.pOrigem=pOrigem; this.obs=obs;
        this.chefe=chefe; this.voluntarios = vols;
    }
    
    public Equipa(Equipa e){
        this.id=e.getId(); this.nome=e.getDesignacao();
        this.pOrigem=e.getpOrigem(); this.obs=e.getObs();
        this.chefe=e.getChefe(); this.voluntarios = e.getVoluntarios();
    }
    
    /*gets & sets*/

    @Override
    public int getId(){return id;}
    @Override
    public void setId(int id){this.id = id;}
    @Override
    public String getDesignacao(){return nome;}
    @Override
    public void setDesignacao(String nome){this.nome = nome;}
    @Override
    public String getpOrigem(){return pOrigem;}
    @Override
    public void setpOrigem(String pOrigem){this.pOrigem = pOrigem;}
    @Override
    public String getObs(){return obs;}
    @Override
    public void setObs(String obs){this.obs = obs;}
    @Override
    public int getChefe(){return chefe;}
    @Override
    public void setChefe(int chefe){this.chefe = chefe;}
    @Override
    public Set<Integer> getVoluntarios(){return voluntarios;}
    @Override
    public void setVoluntarios(Set<Integer> voluntarios){this.voluntarios = voluntarios;}
    
    /*equals, clone e hashcode*/
    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        
        else if(o==null || this.getClass()!=o.getClass()) return false;
        
        else{
            Equipa e = (Equipa) o;
            
            for(Integer nr : this.voluntarios)
                if(!e.getVoluntarios().contains(nr)) return false;
            
            return( this.id==e.getId() && this.nome.equals(e.getDesignacao()) &&
                    this.pOrigem.equals(e.getpOrigem()) && this.obs.equals(e.getObs()) &&
                    this.chefe==e.getChefe());
        }
    }
    
    @Override
    public IEquipa clone(){
        return new Equipa(this);
    }
    
    @Override
    public int hashCode(){
        return Arrays.hashCode(new Object[] {this.chefe, this.nome, this.id, this.obs, this.pOrigem, 
                               this.voluntarios});
    }
    
    /**
     * Método que devolve nº de voluntários de uma equipa.
     * @return inteiro que representa o tamanho da equipa. 
     */
    @Override
    public int size(){return this.voluntarios.size();}
    
    /**
     * Guarda uma referência de um voluntário nesta equipa.
     * @param nr, nº do voluntário a associar à equipa 
     */
    @Override
    public void addVoluntario(int nr){this.voluntarios.add(nr);}
    
    /**
     * Elimina um voluntário desta equipa.
     * @param nr, nº do voluntário que queremos remover da equipa. 
     */
    @Override
    public void remVoluntario(int nr){this.voluntarios.remove(nr);}
}
