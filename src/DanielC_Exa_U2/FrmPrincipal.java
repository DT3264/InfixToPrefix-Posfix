package DanielC_Exa_U2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.formdev.flatlaf.IntelliJTheme;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Dani
 */
public class FrmPrincipal extends javax.swing.JFrame {

    private Conversor conversor;
    private Calculador calculador;
    private Timer temporizador;
    int intervalo=500;
    
    /**
     * Creates new form FrmPrincipal
     */
    public FrmPrincipal() {
        initComponents();
        setLocationRelativeTo(null);
        btnConvertir.setFocusable(false);
        btnCalcular.setFocusable(false);
    }
    private void showMessage(String msg){
        JOptionPane.showMessageDialog(rootPane, msg);
    }
    
    private void muestraEstadoValido(boolean esValida){
        if(esValida){
            lblValida.setText("Estado de expresión: Válida");
            lblValida.setForeground(Color.green);
        }
        else{
            lblValida.setText("Estado de expresión: No válida");
            lblValida.setForeground(Color.red);
            btnCalcular.setEnabled(false);
        }
    }
    
    private boolean esInfijaValido(String infija){
        ScriptEngineManager manager=new ScriptEngineManager();
        ScriptEngine engine=manager.getEngineByName("js");
        try {
            engine.eval(infija);
            boolean esValida=infija.length()>0;
            muestraEstadoValido(esValida);
            return esValida;
        } catch (ScriptException e) {
            return false;
        }
        
    }
    
    private void pruebaInfijaAlFondo(){
        new Thread( () -> esInfijaValido(txtInfija.getText()) ).start();
    }
    
    private void limpiaTabla(JTable tabla){
        int filas=tabla.getModel().getRowCount();
        while(filas>0){
            ((DefaultTableModel)tabla.getModel()).removeRow(filas-1);
            filas--;
        }
    }
    
    private void agregaATabla(JTable tabla, Object[] obj){
        ((DefaultTableModel)tabla.getModel()).addRow(obj);
    }
    
    private boolean aPrefija(){
        return cmbMetodo.getSelectedIndex()==0;
    }
    
    private TimerTask timerConversor(){
        return new TimerTask() {
            @Override
              public void run() {
                String estado=String.format("Token: %3s ; Pos: %3d", conversor.tokenActual, conversor.pos);
                lblEstado.setText(estado);
                txtSalida.setText(conversor.salida);
                limpiaTabla(tblPila);
                conversor.stack.forEach(op -> agregaATabla(tblPila, new Object[]{op})); 
                
                if(conversor.listo){
                    btnCalcular.setEnabled(true);
                    lblEstado.setText("");
                    showMessage("Listo");
                    temporizador.cancel();
                    temporizador=null;
                }
                
              }
        };
    }
    
    private TimerTask timerCalculador(){
        return new TimerTask() {
            @Override
            public void run() {
                String estado=String.format("Token: %3s ; Pos: %3d", calculador.tokenActual, calculador.pos);
                lblEstado.setText(estado);
                limpiaTabla(tblPila);
                limpiaTabla(tblOperaciones);
                calculador.stack.forEach(op -> agregaATabla(tblPila, new Object[]{op}));
                calculador.operaciones.forEach(op -> agregaATabla(tblOperaciones, op));
                
                if(calculador.listo){
                    lblEstado.setText("");
                    txtResultado.setText(conversor.entrada + " = " + calculador.resultado);
                    showMessage("Listo");
                    temporizador.cancel();
                    temporizador=null;
                }
            }
        };
    }
            
    private void convertir(){
        if(temporizador!=null){
            showMessage("Hay una operación en proceso");
            return;
        }
        
        String infija=txtInfija.getText();
        boolean esValida=esInfijaValido(infija);
        if(!esValida){
            showMessage("La cadena no es válida");
            return;
        }
        
        lblSalida.setText("SALIDA (" + (aPrefija() ? "PRE" : "POS") + ")");
        txtResultado.setText("");
        limpiaTabla(tblOperaciones);
        
        conversor=new Conversor(infija, aPrefija());
        new Thread(() -> conversor.convierte() ).start();
        
        temporizador=new Timer();
        temporizador.schedule(timerConversor(), 0, intervalo);
    }
    
    private void calcular(){
        if(temporizador!=null){
            showMessage("Hay una operación en proceso");
            return;
        }
        
        txtInfija.setText(conversor.entrada);
        
        calculador=new Calculador(conversor.salida, aPrefija());
        new Thread(() -> calculador.calcula() ).start();
        
        temporizador=new Timer();
        temporizador.schedule(timerCalculador(), 0, intervalo);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblInfija = new javax.swing.JLabel();
        txtInfija = new javax.swing.JTextField();
        btnConvertir = new javax.swing.JButton();
        cmbMetodo = new javax.swing.JComboBox<>();
        lblSalida = new javax.swing.JLabel();
        txtSalida = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblOperaciones = new javax.swing.JTable();
        btnCalcular = new javax.swing.JButton();
        txtResultado = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        lblValida = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPila = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        lblInfija.setText("INFIJA");

        txtInfija.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtInfijaKeyReleased(evt);
            }
        });

        btnConvertir.setText("Convertir");
        btnConvertir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConvertirActionPerformed(evt);
            }
        });

        cmbMetodo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Prefija", "Posfija" }));

        lblSalida.setText("SALIDA (___)");

        txtSalida.setEditable(false);

        tblOperaciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Valor 1", "Operador", "Valor 2", "Resultado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblOperaciones);

        btnCalcular.setText("Calcular");
        btnCalcular.setEnabled(false);
        btnCalcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularActionPerformed(evt);
            }
        });

        txtResultado.setEditable(false);

        jLabel1.setText("RESULTADO");

        lblValida.setText("Estado de expresión: ");

        lblEstado.setText("Token ; Pos: ");

        tblPila.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Pila"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblPila);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSalida)
                            .addComponent(lblInfija)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addComponent(lblValida)
                                        .addGap(0, 135, Short.MAX_VALUE))
                                    .addComponent(txtSalida)
                                    .addComponent(txtInfija, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cmbMetodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(83, 83, 83))
                                    .addComponent(lblEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtResultado)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnCalcular, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnConvertir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblValida, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtInfija, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblInfija)
                    .addComponent(cmbMetodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConvertir))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSalida)
                    .addComponent(lblEstado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtResultado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnCalcular)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConvertirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConvertirActionPerformed
        convertir();
    }//GEN-LAST:event_btnConvertirActionPerformed

    private void txtInfijaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInfijaKeyReleased
        pruebaInfijaAlFondo();
    }//GEN-LAST:event_txtInfijaKeyReleased

    private void btnCalcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularActionPerformed
        calcular();
    }//GEN-LAST:event_btnCalcularActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                IntelliJTheme.install(FrmPrincipal.class.getResourceAsStream("DarkPurple.theme.json"));
                new FrmPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCalcular;
    private javax.swing.JButton btnConvertir;
    private javax.swing.JComboBox<String> cmbMetodo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblInfija;
    private javax.swing.JLabel lblSalida;
    private javax.swing.JLabel lblValida;
    private javax.swing.JTable tblOperaciones;
    private javax.swing.JTable tblPila;
    private javax.swing.JTextField txtInfija;
    private javax.swing.JTextField txtResultado;
    private javax.swing.JTextField txtSalida;
    // End of variables declaration//GEN-END:variables
}