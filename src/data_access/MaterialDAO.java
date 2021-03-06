package data_access;

import business.projetos.IMaterial;
import business.projetos.MaterialFactory;
import java.sql.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementação de um Data Acess Object para gerir instancias da classe Material.
 * @author Jorge Caldas, José Cortez, Marcelo Gonçalves, Ricardo Silva
 * @version 30.12.2014
 */

class MaterialDAO {
    
    public Connection conn;
    private MySQLParseTools parseTools;


    public MaterialDAO () throws ConnectionErrorException {
        try {
            this.parseTools = new MySQLParseTools();
            this.conn = (new MySQLManager()).getConnection();
        } catch (SQLException ex) {System.out.println("error_material_bd");}
    }
    
    public int size() {
        try {
            int i = 0;
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT nome FROM Material");
            while(rs.next()){ i++; }
            return i;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }
    
    public boolean isEmpty() {
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT Nome FROM Material");
            return !rs.next();
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }
    
    public boolean containsKey(Object key) throws NullPointerException {
        try {
            Statement stm = conn.createStatement();
            String sql = "SELECT nome FROM Material WHERE Id='"+(int)key+"'";
            ResultSet rs = stm.executeQuery(sql);
            return rs.next();
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }
    
    public boolean containsValue(Object value) {
        try {
            if(value instanceof IMaterial) {
                IMaterial v = (IMaterial)value;
                Statement stm = conn.createStatement();
               
                int N=this.size();
                for(int key=1; key<N; key++){
                    IMaterial isv = this.get(key);
                    if(isv!=null){
                        if(v.equals(isv)) return true;
                    }
                }
                return false;
            } else return false;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }
    
    public IMaterial get(Object key) {
        try {
            IMaterial mt = null;
            Statement stm = conn.createStatement();
            String sql = "SELECT * FROM Material WHERE Id='"+(int)key+"'";
            ResultSet rs = stm.executeQuery(sql);
            if (rs.next()){ 
                mt = new MaterialFactory().createMaterial();
                mt.setId(rs.getInt(1));
                mt.setNome(rs.getString(2));
                mt.setDesc(rs.getString(3));
                mt.setQTD(rs.getInt(4));
            }
            return mt;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }
    
    public IMaterial put(Integer key, IMaterial value) {
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM Material WHERE Id='"+key+"'");
            int i  = stm.executeUpdate(insert(key,value));
            IMaterial mt = new MaterialFactory().createMaterial();
            mt.setId(value.getId());
            mt.setNome(value.getNome());
            mt.setDesc(value.getDesc());
            mt.setQTD(value.getQTD());
            return mt;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }

    private String insert(Integer key, IMaterial value){
        MySQLParseTools pt = new MySQLParseTools();

        ArrayList<Object> valores = new ArrayList<>();
        valores.add(key);
        valores.add(value.getNome());
        valores.add(value.getDesc());
        valores.add(value.getQTD());
        
        String sql = pt.createInsert("Material", valores);
        return sql;
    }
    
    public IMaterial remove(Object key) {
        try {
            IMaterial mt = this.get(key);
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM Material WHERE Id='"+key+"'");
            return mt;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }
    
    public Collection<IMaterial> values() {
        try {
            Collection<IMaterial> col = new HashSet<>();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Material");
            for (;rs.next();) {
                IMaterial mt = new MaterialFactory().createMaterial();
                mt.setId(rs.getInt(1));
                mt.setNome(rs.getString(2));
                mt.setDesc(rs.getString(3));
                mt.setQTD(rs.getInt(4));
                col.add(mt);
            }
            return col;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }
    
    public void clear () {
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM Material WHERE Material.Id>=0;");
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
    }
        
    public Set<Entry<Integer, IMaterial>> entrySet() {
        try {
            Set<Entry<Integer,IMaterial>> set;
            HashMap<Integer,IMaterial> map = new HashMap<>();
            int n = this.size();
            for(int i=0; i<n; i++){
                if(this.containsKey(i)){
                    map.put(i,this.get(i));
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
            MaterialDAO mdao = (MaterialDAO) o;
            
            for(IMaterial m : this.values()){
                if(!mdao.containsKey(m.getId())) return false;
                else{
                    if(!m.equals(mdao.get(m.getId()))) return false;
                }
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        int hash = Arrays.hashCode(new Object[]{conn, parseTools});
        for(IMaterial m : this.values())
            hash+=m.hashCode();
        return hash;
    }
    
    /**
     * Procura a maior chave de projeto existente na base de dados e retorna
     * esse valor incrementado em uma unidade
     * @return Chave que identificará univocamente no sistema um material. 
     */
    public int generateMaterialKey(){
       try {
            if(!this.isEmpty()){
                Statement stm = conn.createStatement();
                ResultSet rs = stm.executeQuery("SELECT MAX(Id) FROM Material;");
                int result = 0;
                if (rs.next()) {      
                    result = rs.getInt(1);  
                } 
                return result + 1;
            } else return 1;
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());} 
    }
    
    /**
     * Fechar a ligação à base de dados.
     */
    public void close(){
        try {
            this.conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(MaterialDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
