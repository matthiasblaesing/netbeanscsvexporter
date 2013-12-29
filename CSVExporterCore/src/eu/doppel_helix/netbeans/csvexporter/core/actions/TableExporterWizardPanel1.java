/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.doppel_helix.netbeans.csvexporter.core.actions;

import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfig;
import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfigColumn;
import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfigTable;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

public class TableExporterWizardPanel1 implements WizardDescriptor.Panel<WizardDescriptor> {

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private TableExporterVisualPanel1 component;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public TableExporterVisualPanel1 getComponent() {
        if (component == null) {
            component = new TableExporterVisualPanel1();
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx("help.key.here");
    }

    @Override
    public boolean isValid() {
        // If it is always OK to press Next or Finish, then:
        return true;
        // If it depends on some condition (form filled out...) and
        // this condition changes (last form field filled in...) then
        // use ChangeSupport to implement add/removeChangeListener below.
        // WizardDescriptor.ERROR/WARNING/INFORMATION_MESSAGE will also be useful.
    }

    @Override
    public void addChangeListener(ChangeListener l) {
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
        if(wiz.getProperty("perColumnShown") != null && ((Boolean)wiz.getProperty("perColumnShown"))) {
            getComponent().setResetColumnConvertersEnabled(true);
        } else {
            getComponent().setResetColumnConvertersEnabled(false);
        }
        ExporterConfig ec = (ExporterConfig) wiz.getProperty("ExporterConfig");
        getComponent().setConfig(ec.getBaseConfig());
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        ExporterConfig ec = (ExporterConfig) wiz.getProperty("ExporterConfig");
        ec.setBaseConfig(getComponent().getConfig());
        if((! getComponent().isResetColumnConverters()) || getComponent().isResetColumnConvertersEnabled()) {
            for(ExporterConfigTable ect: ec.getTableConfig()) {
                for(ExporterConfigColumn ecc: ect.getColumns()) {
                    ecc.setConverterConfig(ec.getBaseConfig().getConverterConfig(ecc.getSqlType()));
                }
            }
        }
    }

}
