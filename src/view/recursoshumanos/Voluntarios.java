package view.recursoshumanos;

import business.recursoshumanos.IVoluntario;
import business.recursoshumanos.ParseVoluntarioDocx;
import data_access.SGHabitat;
import exceptions.VoluntarioNaoExisteException;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import view.JCloseMainIFrameListener;
import view.MainMenu;
import view.OkDialog;
import view.RowComparator;

/** Classe para vista geral e de gestão de voluntários e equipas de voluntários.
 *
 * @author Jorge Caldas, José Cortez, Marcelo Gonçalves, Ricardo Silva
 * @version 2014.12.22
 */

public class Voluntarios extends javax.swing.JInternalFrame {

    // Variáveis de instancia
    private MainMenu parent;
    private SGHabitat hfacade;
    
    
    public Voluntarios(MainMenu parent, SGHabitat facade) {
        initComponents();
        
        this.hfacade=facade;
        this.addInternalFrameListener(new JCloseMainIFrameListener(this,parent));
        initVoluntariosComponents(parent);     
    }
    
    /**
     * Método que devolve tempo atual do sistema.
     * @return string com date e hora corrente do sistema. 
     */
    public String getCurrentDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    
    /**
     * Método que imprime relatórios de operações no painel do menu principal.
     * @param text, texto com informação acerca de alguma operação efetuada na operação. 
     */
    public void report (String text){
        this.parent.report(text);
    }
    
    /*--------------------------------------------------------------------------------------------*/
    /*------------------------------------ VOLUNTÁRIOS -------------------------------------------*/
    /*--------------------------------------------------------------------------------------------*/
    
    /**
    * Iniciar componentes que estão dentro da tab "Voluntários"
    * @param parent, parent frame, referência do menu principal. 
    */
    private void initVoluntariosComponents(MainMenu parent) {
        this.voluntariosTable.setAutoCreateRowSorter(false);
        TableRowSorter trsv = new TableRowSorter(this.voluntariosTable.getModel());
        
        // Sorter de linhas da tabela
        for(int i=0; i<3; i++){
            trsv.setComparator(i, new RowComparator());
        }
        voluntariosTable.setRowSorter(trsv);
        this.voluntariosTable.setAutoCreateRowSorter(false);     
        
        this.parent=parent;
        fillTableCells();
        
        final Voluntarios aux=this;
        /*Para operações de consulta em tabelas*/
        voluntariosTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                JTable table =(JTable) me.getSource();
                Point p = me.getPoint();
                int row = table.rowAtPoint(p);
                if (me.getClickCount() == 2) {
                    int nr = (Integer)table.getValueAt(row,0); // Buscar o nº do vol da linha em que se fez doubleClick
                    // Exibir dados num registo individual
                    JInternalFrame rv=null;
                    try {
                        IVoluntario v = aux.hfacade.rh_getVoluntario(nr);
                        rv = new RegistoVoluntario(aux.hfacade, v, aux, true, false);
                    } catch (VoluntarioNaoExisteException ex) {
                        Logger.getLogger(Voluntarios.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    aux.getDesktopPane().add(rv);
                    rv.show();
                    rv.setClosable(true);
                }
            }
        }); 
    }
    
    /**
     * Permite frames filhas desta a aceder à parent frame da mesma.
     * @return frame parent MainMenu
     */
    public MainMenu getVoluntariosParent(){return this.parent;}
    
    /**
     * Método que percorre conjunto de voluntários e preenche a tabela em conformidade com os dados a mostar.
     */
    public void fillTableCells(){
        DefaultTableModel model = (DefaultTableModel) voluntariosTable.getModel();
        for(IVoluntario v : hfacade.rh_voluntariosValues()){ // Preencher tabela com dados de voluntários
            model.addRow(new Object[]{v.getNr(), v.getFirstAndLastName(), v.getHorasVoluntariado(), "desc"});
        }        
    }
    
    /**
     * Reescreve a tabela de voluntários.
     */
    public void rewriteTable(){
        DefaultTableModel model = (DefaultTableModel) voluntariosTable.getModel();
        // Apagar linhas da tabela
        int n = model.getRowCount();
        for (int i=n-1; i>=0; i--) {
            model.removeRow(i);
        }
        this.fillTableCells();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        searchbarTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        panels = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        voluntariosTable = new javax.swing.JTable();
        addVoluntarioButton = new javax.swing.JButton();
        loadButton = new javax.swing.JButton();

        setBorder(null);
        setTitle("Voluntários");

        searchButton.setIcon(new javax.swing.ImageIcon("D:\\Dropbox\\Dropbox\\BD e DSS\\GUI\\icons\\lupa_icon.png")); // NOI18N
        searchButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        searchButton.setContentAreaFilled(false);
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onClickSearchButton(evt);
            }
        });

        voluntariosTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nr", "Nome", "Horas"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        voluntariosTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        voluntariosTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(voluntariosTable);
        if (voluntariosTable.getColumnModel().getColumnCount() > 0) {
            voluntariosTable.getColumnModel().getColumn(0).setMaxWidth(1000);
            voluntariosTable.getColumnModel().getColumn(2).setMaxWidth(1000);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        addVoluntarioButton.setIcon(new javax.swing.ImageIcon("D:\\Dropbox\\Dropbox\\BD e DSS\\GUI\\icons\\add_icon.png")); // NOI18N
        addVoluntarioButton.setText("       Adicionar");
        addVoluntarioButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        addVoluntarioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onClickAddButton(evt);
            }
        });

        loadButton.setIcon(new javax.swing.ImageIcon("D:\\Dropbox\\Dropbox\\BD e DSS\\GUI\\icons\\upload_icon.png")); // NOI18N
        loadButton.setText("     Carregar Ficha");
        loadButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onClickLoadButton(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addVoluntarioButton, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                    .addComponent(loadButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(addVoluntarioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(loadButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 332, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        panels.addTab("Voluntários", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(searchbarTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(panels, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(searchbarTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(searchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addComponent(panels)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * O clique no botão seatch desencadeia um mecanismos de pesquisa caso input seja validado.
     * @param evt 
     */
    private void onClickSearchButton(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onClickSearchButton
        // Pesquisar voluntário por nome ou por número
        String searchinput = this.searchbarTextField.getText().trim();
        if(this.voluntariosTable.getRowCount()==0 && (searchinput==null || searchinput.equals(""))){
            this.fillTableCells();
            return;
        }
        
        if(this.panels.getSelectedIndex()==0){ // Voluntários
            try{
                int nr = Integer.parseInt(searchinput); // Pesquisar voluntário por nº caso string seja um número inteiro
                if(hfacade.rh_voluntarioExiste(nr)){
                    // Exibir dados num registo individual
                    JInternalFrame rv = new RegistoVoluntario(hfacade, hfacade.rh_getVoluntario(nr), this, true, false);
                    this.getDesktopPane().add(rv);
                    rv.show();
                    rv.setClosable(true);
                }
            } catch(NumberFormatException e){ // Supomos que input seja um nome próprio ou apelido
                    DefaultTableModel model = (DefaultTableModel) voluntariosTable.getModel();
                    
                    // Apagar linhas da tabela
                    int n = model.getRowCount();
                    for (int i=n-1; i>=0; i--) {
                        model.removeRow(i);
                    }
                    
                    Set<IVoluntario> set;
                    try {
                        set = this.hfacade.rh_searchVoluntario(searchinput);
                    } catch (VoluntarioNaoExisteException ex) {
                        set=new HashSet<>();
                    }
                    int resultsfound=set.size();
                    
                    // Preencher tabela
                    for(IVoluntario v : set){
                        model.addRow(new Object[]{v.getNr(), v.getFirstAndLastName(), v.getHorasVoluntariado(), "desc", "desc"});
                    }
                    
                    if(resultsfound!=0 && !searchinput.equals("")){
                        new OkDialog(this).show("Número de resultados encontrados para "+searchinput+": "+resultsfound);
                    }else{
                        new OkDialog(this).show("Nenhum resultado encontrado para "+searchinput);
                    }                   
            } catch (VoluntarioNaoExisteException ex) {
                Logger.getLogger(Voluntarios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_onClickSearchButton

    /**
     * O clique no botão loadButton permite ao utilizador carregar uma ficha voluntário (.docx) para o sistema.
     * @param evt
     */
    private void onClickLoadButton(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onClickLoadButton
        ArrayList<IVoluntario> voluntarios = new ArrayList<>();
        try {
            voluntarios = (ArrayList<IVoluntario>) ParseVoluntarioDocx.parse(this.hfacade);
        } catch (Exception ex) {
            Logger.getLogger(Voluntarios.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Parse não teve efeito por ser cancelado, ou porque ocorreu um erro
        if (voluntarios.isEmpty()) return;
        else if(voluntarios.get(0).getNome().equals("null")) return;

        for(IVoluntario v : voluntarios){
            // Exibir dados num registo individual
            RegistoVoluntario rv = new RegistoVoluntario(hfacade,v, this, false, true);
            this.getDesktopPane().add(rv);
            rv.show();
            rv.setClosable(true);
        }
    }//GEN-LAST:event_onClickLoadButton

    /**
     * O clique no botão addVoluntario abre frame para registo manual.
     * @param evt
     */
    private void onClickAddButton(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onClickAddButton
        // Exibir dados num registo individual
        RegistoVoluntario rv = new RegistoVoluntario(hfacade,this);
        this.getDesktopPane().add(rv);
        rv.show();
        rv.setClosable(true);
    }//GEN-LAST:event_onClickAddButton

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addVoluntarioButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton loadButton;
    private javax.swing.JTabbedPane panels;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchbarTextField;
    private javax.swing.JTable voluntariosTable;
    // End of variables declaration//GEN-END:variables
}
