package business.recursoshumanos;

import data_access.SGHabitat;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.JFileChooser;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**Parse de uma ficha (ou mais) de voluntários permitindo carregar os dados da mesma na base de dados da aplicação.
 *
 * @author Jorge Caldas, José Cortez, Marcelo Gonçalves, Ricardo Silva
 * @version 2014.12.21
 */

public class ParseVoluntarioDocx {

    private static int NR_VOL; // Permite gerar chaves únicas parciais, que não existem verdadeiramente na base
    
    public static List<IVoluntario> parse(SGHabitat facade) throws Exception {
        ArrayList<IVoluntario> voluntarios = new ArrayList<>();
        IVoluntario v = new VoluntarioFactory().createVoluntario();
        v.setNome("null");
        NR_VOL = facade.rh_generateVoluntarioKey();
         
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        int returnValue = chooser.showOpenDialog(null);
        
        if(returnValue == JFileChooser.APPROVE_OPTION){
            File[] files = chooser.getSelectedFiles();
            ArrayList<String> filePaths = new ArrayList<>();
            
            for(File f : files){filePaths.add(f.getAbsolutePath());} // Buscar caminhos dos diversos ficheiros
            
            for(String fileName : filePaths){
                v = readTables(fileName, facade); // Parse de uma ficha de voluntario
                voluntarios.add(v); // Adicionar nova instância de voluntários ao ArrayList
            }                
        }
        return voluntarios;
     }
     
    private static IVoluntario readTables (String fileName, SGHabitat facade) throws Exception {         
        
        InputStream fis = new FileInputStream(fileName);  
        POIFSFileSystem fs = new POIFSFileSystem(fis);  
        HWPFDocument doc = new HWPFDocument(fs);  
  
        Range range = doc.getRange();
        
        // Parametros do voluntário
        List<String> params = new ArrayList<>();
        GregorianCalendar datanasc = new GregorianCalendar();
        List<String> linguas = new ArrayList<>();
        
        Paragraph par = null;
        for (int i=0; i<range.numParagraphs(); i++) {
            
            try{
                par = range.getParagraph(i);
            } catch(Exception e){
                System.out.println("Ocorreu um erro ao ler o ficheiro. Por favor tente mais tarde.");
            }

            String[] toks=null;
            switch(i){
                case 7: // Nome do voluntario (params(0))
                    if(par!=null && par.text()!=null && !par.text().equals("")){
                        params.add(par.text().trim());
                    } else params.add("sem nome");
                    break;
                case 10: // Data de nascimento (datanasc)
                    if(par!=null && par.text()!=null && !par.text().equals("")){
                        
                        // Multi split parse com: '/', '.' e " "(espaço)
                        toks = par.text().split("/");
                        if(toks.length!=3){
                            toks = par.text().split(".");
                            if(toks.length!=3){
                                toks = par.text().split(" ");
                            }
                        }
                        
                        if(toks.length==3){
                            datanasc=new GregorianCalendar(Integer.parseInt(toks[2].trim()),
                                   (Integer.parseInt(toks[1].trim()) - 1),Integer.parseInt(toks[0].trim()));
                        }
                        else datanasc = new GregorianCalendar();
                        
                    } else datanasc = new GregorianCalendar();
                    break;
                case 12: // Profissão (params(1))
                    if(par!=null && par.text()!=null && !par.text().equals("")){
                        params.add(par.text().trim());
                    } else params.add("desconhecida");
                    break;
                case 15: // Rua (params(2))
                    if(par!=null && par.text()!=null && !par.text().equals("")){
                        params.add(par.text().trim());
                    } else params.add("desconhecida");
                    break;
                case 18: // Código postal (params(3));
                    if(par!=null && par.text()!=null && !par.text().equals("")){
                        params.add(par.text().trim());
                    } else params.add("desc");
                    break;
                case 20: // Localidade (params(4))
                    if(par!=null && par.text()!=null && !par.text().equals("")){
                        params.add(par.text().trim());
                    } else params.add("desconhecida");
                    break;
                case 23: // Telefone (params(5))
                    if(par!=null && par.text()!=null && !par.text().equals("")){
                        params.add(par.text().trim());
                    } else params.add("desconhecido");
                    break;
                case 25: // Telemóvel (params(6))
                    if(par!=null && par.text()!=null && !par.text().equals("")){
                        params.add(par.text().trim());
                    } else params.add("desconhecido");
                    break;
                case 28: // E mail (params(7))
                    if(par!=null && par.text()!=null && !par.text().equals("")){
                        toks=par.text().split("\" " );
                        if(toks!=null && toks.length>1){ // Reforçar testes. ## Campo muito susceptível a erros ! ##
                            params.add(toks[1].trim());
                        } else params.add("desconhecido");
                    } else params.add("desconhecido");
                    break;
                case 31: // Habilitações (params(8))
                    if(par!=null && par.text()!=null && !par.text().equals("")){
                        params.add(par.text().trim());
                    } else params.add("desconhecido");
                    break;
                case 34: // Línguas (linguas)
                    if(par!=null && par.text()!=null && !par.text().equals("")){
                        toks=par.text().split(", ");
                        linguas=new ArrayList<>();
                        for(int j=0; j<toks.length; j++){linguas.add(toks[j].trim());}
                    } else params.add("desconhecido");
                    break;
                case 37: // Formação complementar (params(9))
                    if(par!=null && par.text()!=null && !par.text().equals("")){
                        params.add(par.text().trim());
                    } else params.add("desconhecida");
                    break;
                case 40: // Experiência voluntariado (params(10))
                    if(par!=null && par.text()!=null && !par.text().equals("")){
                        params.add(par.text().trim());
                    } else params.add("desconhecida");
                    break;
                case 62: // Conhecimentos de construção (params(11))
                    if(par.text()!=null && !par.text().equals("")){
                        params.add(par.text().trim());
                    } else params.add("desconhecida");
                    break;
                case 64: // Gostaria de trabalhar com outros voluntários? (params(12))
                    if(par!=null && par.text()!=null && !par.text().equals("")){
                        params.add(par.text().trim());
                    } else params.add("desconhecido");
                    break;
                case 67: // Disponibilidade (params(13))
                    if(par!=null && par.text()!=null && !par.text().equals("")){
                        params.add(par.text().trim());
                    } else params.add("desconhecido");
                    break;                   
            }
        }
        StringBuilder obs = new StringBuilder();
        obs.append("Formação complementar: ").append(params.get(9)).append("\n");
        obs.append("Experiência Voluntariado: ").append(params.get(10)).append("\n");
        obs.append("Conhecimentos de construção: ").append(params.get(11)).append("\n");
        obs.append("Vontade de trabalhar com outros voluntários: ").append(params.get(12)).append("\n");
        obs.append("Disponibilidade: ").append(params.get(13)).append("\n");
        
        IVoluntario v = new VoluntarioFactory().createVoluntario();
             
        v.setNr(NR_VOL);
        v.setNome(params.get(0));
        v.setDatanasc(datanasc);
        v.setDataInicioVol(new GregorianCalendar());
        v.setProfissao(params.get(1));
        v.setRua(params.get(2));
        v.setCodPostal(params.get(3));
        v.setLocalidade(params.get(4));
        v.setTelef(params.get(5));
        v.setTelem(params.get(6));
        v.setEmail(params.get(7));
        v.setHabilitacoes(params.get(8));
        v.setObs(obs.toString());
        v.setLinguas(linguas);
        NR_VOL++;
        
        return v;
    }
}
