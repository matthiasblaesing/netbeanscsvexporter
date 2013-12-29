/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.doppel_helix.netbeans.csvexporter.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.openide.util.Exceptions;
import org.openide.util.Mutex;
import org.openide.util.MutexException;

public class LoggingPanel extends javax.swing.JPanel implements LogListener, ProgressListener {

    private SimpleDateFormat sdf = new SimpleDateFormat("'['HH:mm:ss']'");
    private int currentCount;
    private Integer totalCount;
    private boolean running;

    /**
     * Creates new form LoggingPanel
     */
    public LoggingPanel() {
        initComponents();

    }

    @Override
    public void logMessage(final String message) {
        Mutex.EVENT.writeAccess(new Runnable() {
            @Override
            public void run() {
                try {
                    String formattedMessage = String.format("[%1$tH:%1$tM:%1$tS] %2$s\n",
                            new Date(), message);
                    Document d = jTextPane1.getDocument();
                    d.insertString(d.getEndPosition().getOffset(), formattedMessage, null);
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @Override
    public void setCurrent(int currentCount) {
        this.currentCount = currentCount;
        updateProgressbar();
    }

    @Override
    public void setTotal(Integer totalCount) {
        this.totalCount = totalCount;
        updateProgressbar();
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
        updateProgressbar();
    }

    private final Runnable progressbarUpdater = new Runnable() {

        @Override
        public void run() {
            jProgressBar1.setMinimum(0);
            if (totalCount != null) {
                jProgressBar1.setIndeterminate(false);
                jProgressBar1.setMaximum(totalCount);
                jProgressBar1.setString(String.format("%d / %d", currentCount, totalCount));
            } else {
                jProgressBar1.setIndeterminate(running);
                jProgressBar1.setString(String.format("%d", currentCount));
            }
            jProgressBar1.setStringPainted(true);
        }
    };

    private void updateProgressbar() {
        Mutex.EVENT.writeAccess(progressbarUpdater);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        setLayout(new java.awt.BorderLayout());
        add(jProgressBar1, java.awt.BorderLayout.NORTH);

        jTextPane1.setEditable(false);
        jScrollPane2.setViewportView(jTextPane1);

        add(jScrollPane2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
