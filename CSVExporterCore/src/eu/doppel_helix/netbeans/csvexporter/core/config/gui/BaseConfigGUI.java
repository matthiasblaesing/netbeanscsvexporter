package eu.doppel_helix.netbeans.csvexporter.core.config.gui;

import eu.doppel_helix.netbeans.csvexporter.core.config.CSVConverterConfig;
import eu.doppel_helix.netbeans.csvexporter.core.config.CSVConverterConfigType;
import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverterFactory;
import eu.doppel_helix.netbeans.csvexporter.core.converter.ConfigDescriptor;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.xml.bind.JAXBException;

public class BaseConfigGUI extends javax.swing.JPanel {
    private final CSVConverterFactory cccF = new CSVConverterFactory();

    /**
     * Creates new form BaseConfigGUI
     */
    public BaseConfigGUI() {
        initComponents();

        configSelector.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                loadConfig();
            }
        });

        updateFileList();

        configSelector.setSelectedIndex(0);
        loadConfig();
    }

    private void loadConfig() {
        try {
            ConfigDescriptor config = (ConfigDescriptor) configSelector.getSelectedItem();
            baseConfigEditor1.setConfig(cccF.readSystemConfig(config));
        } catch (JAXBException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        actionMenu = new javax.swing.JPopupMenu();
        saveMenu = new javax.swing.JMenuItem();
        saveAsNewMenu = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        exportMenuItem = new javax.swing.JMenuItem();
        importMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        revertMenu = new javax.swing.JMenuItem();
        refreshFileList = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        deleteMenuItem = new javax.swing.JMenuItem();
        jPanel4 = new javax.swing.JPanel();
        configSelector = new javax.swing.JComboBox<ConfigDescriptor>();
        actionsMenuButton = new javax.swing.JButton();
        baseConfigEditor1 = new eu.doppel_helix.netbeans.csvexporter.core.config.gui.BaseConfigEditor();

        saveMenu.setText("Save");
        saveMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuActionPerformed(evt);
            }
        });
        actionMenu.add(saveMenu);

        saveAsNewMenu.setText("Save As New");
        saveAsNewMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsNewMenuActionPerformed(evt);
            }
        });
        actionMenu.add(saveAsNewMenu);
        actionMenu.add(jSeparator1);

        exportMenuItem.setText("Export");
        exportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportMenuItemActionPerformed(evt);
            }
        });
        actionMenu.add(exportMenuItem);

        importMenuItem.setText("Import");
        importMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importMenuItemActionPerformed(evt);
            }
        });
        actionMenu.add(importMenuItem);
        actionMenu.add(jSeparator2);

        revertMenu.setText("Revert");
        revertMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                revertMenuActionPerformed(evt);
            }
        });
        actionMenu.add(revertMenu);

        refreshFileList.setText("Refresh Filelist");
        refreshFileList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshFileListActionPerformed(evt);
            }
        });
        actionMenu.add(refreshFileList);
        actionMenu.add(jSeparator3);

        deleteMenuItem.setText("Delete");
        deleteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteMenuItemActionPerformed(evt);
            }
        });
        actionMenu.add(deleteMenuItem);

        setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.GridBagLayout());

        configSelector.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        configSelector.setRenderer(new eu.doppel_helix.netbeans.csvexporter.core.config.gui.ConfigDescriptorRenderer());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(configSelector, gridBagConstraints);

        actionsMenuButton.setText("...");
        actionsMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionsMenuButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel4.add(actionsMenuButton, gridBagConstraints);

        add(jPanel4, java.awt.BorderLayout.PAGE_START);
        add(baseConfigEditor1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void actionsMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actionsMenuButtonActionPerformed
        if (evt.getSource() instanceof Component) {
            ConfigDescriptor config = (ConfigDescriptor) configSelector.getSelectedItem();
            if(config.isWriteable()) {
                saveMenu.setEnabled(true);
                deleteMenuItem.setEnabled(true);
            } else {
                saveMenu.setEnabled(false);
                deleteMenuItem.setEnabled(false);
            }
            Component c = (Component) evt.getSource();
            actionMenu.show(c, 0, c.getHeight());
        }
    }//GEN-LAST:event_actionsMenuButtonActionPerformed

    private void updateFileList() {
        configSelector.setModel(new DefaultComboBoxModel(
                cccF.listSystemConfigs().toArray(new ConfigDescriptor[]{})));
    }

    private void refreshFileListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshFileListActionPerformed
        updateFileList();
    }//GEN-LAST:event_refreshFileListActionPerformed

    private void revertMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_revertMenuActionPerformed
        loadConfig();
    }//GEN-LAST:event_revertMenuActionPerformed

    private void saveAsNewMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsNewMenuActionPerformed
        try {
            ConfigDescriptor newDescriptor = cccF.writeSystemConfig(baseConfigEditor1.getConfig(), null);
            ((DefaultComboBoxModel<ConfigDescriptor>) configSelector.getModel()).addElement(newDescriptor);
            configSelector.setSelectedItem(newDescriptor);
            configSelector.repaint();
            loadConfig();
        } catch (JAXBException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }//GEN-LAST:event_saveAsNewMenuActionPerformed

    private void exportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportMenuItemActionPerformed
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setMultiSelectionEnabled(false);
        jfc.setDialogTitle("Export");
        if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File targetFile = jfc.getSelectedFile();
            try {
                cccF.writeConfig(baseConfigEditor1.getConfig(), targetFile.getAbsolutePath());
            } catch (JAXBException ex) {
                throw new RuntimeException(ex);
            }
        }
    }//GEN-LAST:event_exportMenuItemActionPerformed

    private void importMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importMenuItemActionPerformed
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.setMultiSelectionEnabled(false);
        jfc.setDialogTitle("Import");
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File source = jfc.getSelectedFile();
            try {
                CSVConverterConfig ccc = cccF.readConfig(source.getAbsolutePath());
                cccF.writeSystemConfig(ccc, null);
                updateFileList();
            } catch (JAXBException | IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }//GEN-LAST:event_importMenuItemActionPerformed

    private void saveMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuActionPerformed
        try {
            ConfigDescriptor config = (ConfigDescriptor) configSelector.getSelectedItem();
            // Save was called without a known configuration or from Default Config
            // switch into the save as new case
            if(! config.isWriteable()) {
                saveAsNewMenuActionPerformed(evt);
                return;
            }
            cccF.writeSystemConfig(baseConfigEditor1.getConfig(), config.getIdentifier());
            config.setName(baseConfigEditor1.getConfig().getName());
            configSelector.repaint();
        } catch (JAXBException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }//GEN-LAST:event_saveMenuActionPerformed

    private void deleteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteMenuItemActionPerformed
//        ConfigDescriptor config = (ConfigDescriptor) configSelector.getSelectedItem();
//        int result = JOptionPane.showConfirmDialog(this,
//                String.format("Really delete configuration '%s'?", config.getName()),
//                "Delete confirmation",
//                JOptionPane.OK_CANCEL_OPTION);
//        if(result == JOptionPane.OK_OPTION) {
//            try {
//                cccF.deleteSystemConfig(config.getIdentifier());
//                updateFileList();
//                configSelector.setSelectedItem(0);
//                loadConfig();
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
//        }
    }//GEN-LAST:event_deleteMenuItemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu actionMenu;
    private javax.swing.JButton actionsMenuButton;
    private eu.doppel_helix.netbeans.csvexporter.core.config.gui.BaseConfigEditor baseConfigEditor1;
    private javax.swing.JComboBox<ConfigDescriptor> configSelector;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JMenuItem exportMenuItem;
    private javax.swing.JMenuItem importMenuItem;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JMenuItem refreshFileList;
    private javax.swing.JMenuItem revertMenu;
    private javax.swing.JMenuItem saveAsNewMenu;
    private javax.swing.JMenuItem saveMenu;
    // End of variables declaration//GEN-END:variables
}