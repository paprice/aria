/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package windows;

import static Language.LanguageProcessing.Parse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;

/**
 *
 * @author despa
 */
public class MainWindow extends javax.swing.JFrame {

    /**
     * Creates new form MainWindow
     */
    int nbSquare = 1;
    // à changer la grosseur du tableau
    JTextPane[] discu = new JTextPane[100];

    public MainWindow() {
        initComponents();
        this.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        UserText = new javax.swing.JTextPane();
        Send = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("A.R.I.A");
        setBackground(new java.awt.Color(0, 0, 204));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setPreferredSize(new java.awt.Dimension(1000, 750));
        setSize(new java.awt.Dimension(1000, 750));

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 750));

        UserText.setToolTipText("Entrer votre texte");
        jScrollPane1.setViewportView(UserText);

        Send.setText("Envoyer");
        Send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 862, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Send, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(816, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(Send, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))
                .addContainerGap())
        );

        jMenu1.setText("Fichier");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Modifier");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1007, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 890, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendActionPerformed
        // TODO add your handling code here:

        int panel = jPanel1.getHeight();
        int userTExt = UserText.getHeight();

        for (int i = 0; i < nbSquare - 1; i++) {
            discu[i].setBounds(discu[i].getX(), discu[i].getY() - (userTExt + 15), discu[i].getWidth(), discu[i].getHeight());
        }

        int height = panel - 2 * (userTExt + 15);
        JTextPane text = new JTextPane();
        text.setText(UserText.getText());

        text.setBounds((jPanel1.getWidth() - 5 - UserText.getWidth()), height, UserText.getWidth(), UserText.getHeight());
        text.setBackground(new java.awt.Color(0, 191, 255));
        text.setEnabled(false);
        text.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        text.setVisible(true);
        jPanel1.add(text);
        discu[nbSquare - 1] = text;
        UserText.setText("");
        nbSquare++;

        try {
            GetAIResponse(Parse(text.getText()));
        } catch (IOException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_SendActionPerformed

    private void GetAIResponse(String resp) {

        int panel = jPanel1.getHeight();
        int userTExt = UserText.getHeight();

        for (int i = 0; i < nbSquare - 1; i++) {
            discu[i].setBounds(discu[i].getX(), discu[i].getY() - (userTExt + 15), discu[i].getWidth(), discu[i].getHeight());
        }

        int height = panel - 2 * (userTExt + 15);
        JTextPane text = new JTextPane();
        text.setText(resp);

        text.setBounds(5, height, UserText.getWidth(), UserText.getHeight());
        text.setBackground(new java.awt.Color(124, 252, 0));
        text.setEnabled(false);
        text.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        text.setVisible(true);
        jPanel1.add(text);
        discu[nbSquare - 1] = text;
        nbSquare++;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Send;
    private javax.swing.JTextPane UserText;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
