package data_access;
import business.familias.ICandidatura;
import business.familias.CandidaturaFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jorge Caldas, José Cortez, Marcelo Gonçalves, Ricardo Silva
 * @version 30.12.2014
 */

class CandidaturaDAO implements Map<Integer,ICandidatura> {

    public Connection conn;
    public MySQLParseTools parseTools;    

    public CandidaturaDAO() throws ConnectionErrorException{
        try {
            this.conn = (new MySQLManager()).getConnection();
            this.parseTools = new MySQLParseTools();
            } 
        catch (SQLException ex) {System.out.println("error_ICandidatura_bd");
            }    
    }

    @Override
    @SuppressWarnings("empty-statement")
    public int size() {
        try {
            int i = 0;
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT Nr FROM Candidaturas");
            for (;rs.next();i++);
            return i;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    @Override
    public boolean isEmpty() {
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT Nr FROM Candidaturas");
            return rs.next();
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}    
    }

    @Override
    public boolean containsKey(Object o) {
         try {
            Statement stm = conn.createStatement();
            String sql = "SELECT Nr FROM Candidaturas WHERE Nr='"+(int)o+"'";
            ResultSet rs = stm.executeQuery(sql);
            return rs.next();
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }


    @Override
    public boolean containsValue(Object o) {
        try {
            if(o instanceof ICandidatura) {
            
                ICandidatura v = (ICandidatura)o;
                Statement stm = conn.createStatement();
                
                int N=this.size();
                for(int key=1; key<N; key++){
                    ICandidatura isv = this.get(key);
                    if(v.equals(isv)) return true;
                }
                return false;
            } else return false;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }
 
    @Override
    public ICandidatura get(Object o) {
        ICandidatura cand = new CandidaturaFactory().createCandidatura();
        try {
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM Candidaturas WHERE Nr='"+(int)o+"'";
            ResultSet rs = stm.executeQuery(sql);
            if (rs.next()){
                cand.setNr(rs.getInt("Nr"));
                cand.setEstado(rs.getString("Estado"));
                cand.setDataDecisao(parseTools.parseSQLDate(rs.getString("DataDecisao")));
                cand.setFuncionarioRegistou(rs.getInt("FuncionarioRegistou"));
                cand.setFuncionarioAprovou(rs.getInt("FuncionarioAprovou"));
                cand.setDataSubmissao(parseTools.parseSQLDate(rs.getString("DataSubmissao")));
                cand.setDescricao(rs.getString("Descricao"));
                
                List<Integer> membros = new ArrayList<>();
                sql = "SELECT Candidatura FROM Membros WHERE Candidatura='"+cand.getNr()+"'";
                rs = stm.executeQuery(sql);
                for(;rs.next();)
                    membros.add(rs.getInt(1));
                cand.setMembros(membros);
                
                cand.setRepresentante(rs.getInt("Representante"));         
            
                return cand;
        }
        } catch (NullPointerException e) {throw new NullPointerException(e.getMessage());} catch (SQLException ex) {
            Logger.getLogger(CandidaturaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cand;
    }
    

    @Override
    public ICandidatura put(Integer k, ICandidatura cd) {
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM Candidaturas WHERE Nr='"+k+"'");
            int i  = stm.executeUpdate(insert(k,cd));
            ICandidatura c = new CandidaturaFactory().createCandidatura();
            c.setNr(cd.getNr());
            c.setEstado(cd.getEstado());
            c.setDataDecisao(cd.getDataDecisao());
            c.setFuncionarioRegistou(cd.getFuncionarioRegistou());
            c.setFuncionarioAprovou(cd.getFuncionarioAprovou());
            c.setMembros(cd.getMembros());
            c.setRepresentante(cd.getRepresentante());
            c.setDescricao(cd.getDescricao());
            c.setDataSubmissao(cd.getDataSubmissao());
            c.setRepresentante(cd.getRepresentante());
            
            return c;
        }
        catch (NullPointerException e) {throw new NullPointerException(e.getMessage());} catch (SQLException ex) {
            Logger.getLogger(CandidaturaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private String insert(Integer key, ICandidatura value) {
        List<Object> obj = new ArrayList<>();
        obj.add(value.getNr());
        obj.add(value.getDescricao());
        obj.add(parseTools.parseCalendar(value.getDataSubmissao()));
        obj.add(parseTools.parseCalendar(value.getDataDecisao()));
        obj.add(value.getFuncionarioAprovou());
        obj.add(value.getFuncionarioRegistou());
        obj.add(value.getEstado());
        obj.add(value.getRepresentante());
        
        return parseTools.createInsert("Candidaturas", obj);
    }


    @Override
    public ICandidatura remove(Object key) {
        try {            
            Statement stm = conn.createStatement();
            ICandidatura c = this.get(key);
            if(c!=null){
                stm.executeUpdate("DELETE FROM Candidaturas WHERE Nr='"+key+"'");
            }
            return c;
        }
        catch (Exception e) {throw new NullPointerException("Voluntário não existe");}
    }


    @Override
    public void putAll(Map<? extends Integer, ? extends ICandidatura> map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
     public void clear () {
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM Candidaturas WHERE Candidaturas.Nr>=0;");
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }
    
    @Override
    public int hashCode() {return Arrays.hashCode(new Object[]{conn, parseTools});}

    @Override
    public Set<Integer> keySet() {
        try {
            Set<Integer> set = new HashSet<>();
            int n = this.generateCandidaturaKey();
            for(int i=0; i<n; i++){
                if(this.containsKey(i)){
                    set.add(i);
                }
            }
            return set;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

     @Override
    public Set<Entry<Integer, ICandidatura>> entrySet() {
        try {
            Set<Entry<Integer,ICandidatura>> set;
            Set<Integer> keys = new HashSet<>(this.keySet());            
            
            HashMap<Integer,ICandidatura> map = new HashMap<>();
            for(Integer key : keys){
                if(this.containsKey(key)){
                    map.put(key,this.get(key));
                }
            }
            return map.entrySet();
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }
    
    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        
        else if(this.getClass() != o.getClass()) return false;
        
        else{
            CandidaturaDAO cdao = (CandidaturaDAO) o;
            
            for(ICandidatura e : this.values()){
                if(!cdao.containsKey(e.getNr())) return false;
                else{
                    if(!e.equals(cdao.get(e.getNr()))) return false;
                }
            }
            return true;
        }
    } 

    @Override
    public Collection<ICandidatura> values() {
        try {
            Set<ICandidatura> set = new HashSet<>();
            Set<Integer> keys = new HashSet<>(this.keySet());
            for(Integer key : keys)
                set.add(this.get(key));
            return set;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }
    
    public int generateCandidaturaKey(){
       try {
            if(!this.isEmpty()){
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("SELECT MAX(Nr) FROM Candidaturas;");
                if(rs.next()){
                    return(rs.getInt(1) + 1);
                }
            } else return 1;
        } catch (Exception e) {return 1;}
        
        return -1;
    }
     /**
     * Fechar a ligação à base de dados.
     */
    public void close(){
        try {
            this.conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(CandidaturaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}
