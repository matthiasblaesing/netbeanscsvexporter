/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.doppel_helix.netbeans.csvexporter.core.actions;

import au.com.bytecode.opencsv.CSVReader;
import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfig;
import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfigTable;
import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverterFactory;
import static eu.doppel_helix.netbeans.csvexporter.core.util.JDBC.buildTableName;
import eu.doppel_helix.netbeans.csvexporter.core.util.LoggingPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.SwingWorker;
import org.netbeans.api.db.explorer.DatabaseConnection;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.nodes.Node;
import org.openide.windows.WindowManager;

// An example action demonstrating how the wizard could be called from within
// your code. You can move the code below wherever you need, or register an action:
// @ActionID(category="...", id="eu.doppel_helix.netbeans.csvexporter.core.actions.TableImporterWizardAction")
// @ActionRegistration(displayName="Open TableImporter Wizard")
// @ActionReference(path="Menu/Tools", position=...)
public final class TableImporterWizardAction extends BaseAction {

    private String buildInsertSQL(String targetTable, int columnCount) {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(targetTable);
        sb.append(" VALUES (");
        for (int i = 0; i < columnCount; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(" ?");
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    protected boolean enable(Node[] activatedNodes) {
        return true;
    }

    @Override
    public String getName() {
        return "Import Tables";
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        List<WizardDescriptor.Panel<WizardDescriptor>> panels = new ArrayList<WizardDescriptor.Panel<WizardDescriptor>>();
        panels.add(new TableImporterWizardPanel1());
        panels.add(new TableImporterWizardPanel2());
        panels.add(new TableImporterWizardPanel3());
        String[] steps = new String[panels.size()];
        for (int i = 0; i < panels.size(); i++) {
            Component c = panels.get(i).getComponent();
            // Default step name to component name of panel.
            steps[i] = c.getName();
            if (c instanceof JComponent) { // assume Swing components
                JComponent jc = (JComponent) c;
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, i);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
                jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
                jc.putClientProperty(WizardDescriptor.PROP_CONTENT_NUMBERED, true);
            }
        }
        WizardDescriptor wiz = new WizardDescriptor(new WizardDescriptor.ArrayIterator<WizardDescriptor>(panels));
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wiz.setTitleFormat(new MessageFormat("{0}"));
        wiz.setTitle("Import Tables");
        final ExporterConfig ec = new ExporterConfig(CSVConverterFactory.getInstance().getDefaultConfig());

        DatabaseConnection dc = findConnection(activatedNodes);
        final Connection c = dc.getJDBCConnection();

        extractTablesFromNodes(activatedNodes, c, ec);

        wiz.putProperty("ExporterConfig", ec);
        wiz.putProperty("ColumnConfigDone", false);

        if (DialogDisplayer.getDefault().notify(wiz)
                == WizardDescriptor.FINISH_OPTION) {

            final LoggingPanel lp = new LoggingPanel();

            final JDialog jd = new JDialog(WindowManager.getDefault().getMainWindow(), "Import");
            jd.setModal(true);
            jd.setLocationByPlatform(true);
            jd.setLayout(new BorderLayout());
            jd.add(lp, BorderLayout.CENTER);
            jd.setSize(640, 480);

            final JButton jb = new JButton("Close");
            jd.add(jb, BorderLayout.SOUTH);

            final SwingWorker sw = new SwingWorker<Object, Object>() {

                @Override
                protected Object doInBackground() throws Exception {
                    try {
                        lp.logMessage("BEGIN IMPORT");
                        lp.setRunning(true);
                        for (ExporterConfigTable ect : ec.getTableConfig()) {
                            String tableName = buildTableName(c, ect);

                            File inputFile = new File(ec.getBaseConfig().getBasePath(),
                                    ect.getFilename());
                            try (PreparedStatement ps = c.prepareStatement(buildInsertSQL(tableName, ect.getColumns().size()));
                                    CSVReader out = new CSVReader(
                                            new InputStreamReader(
                                                    new BufferedInputStream(
                                                            new FileInputStream(inputFile)),
                                                    ec.getBaseConfig().getCharset()),
                                            ec.getBaseConfig().getSeperator(),
                                            ec.getBaseConfig().getQuoteChar(),
                                            ec.getBaseConfig().getEscapeChar(),
                                            ec.getBaseConfig().isIncludeColumnTitle() ? 1 : 0
                                    )) {

                                lp.logMessage("Importing: " + tableName);

                                String[] row;
                                int rowCount = 0;

                                long start = System.currentTimeMillis();

                                while ((row = out.readNext()) != null) {
                                    for (int i = 0; i
                                            < ect.getColumns().size(); i++) {
                                        ect.getColumns().get(i).getCSVConverter().setValue(ps, i
                                                + 1, row[i]);
                                    }
                                    ps.execute();
                                    rowCount++;
                                    lp.setCurrent(rowCount);

                                    if (Thread.interrupted()) {
                                        throw new InterruptedException();
                                    }
                                }

                                long end = System.currentTimeMillis();
                                long diff = end - start;
                                if (diff == 0) {
                                    diff = 1;
                                }
                                lp.logMessage(String.format("%d rows in %d ms: %d rows/s", rowCount, end
                                        - start, (rowCount * 1000) / diff));
                            }
                            lp.setRunning(false);
                        }
                        lp.logMessage("DONE");
                    } catch (SQLException | IOException ex) {
                        lp.logMessage(ex.getMessage());
                    } catch (InterruptedException ie) {
                        lp.logMessage("INTERRUPTED");
                    } catch (Throwable th) {
                        System.out.println(th);
                    }
                    return null;
                }

                @Override
                protected void done() {
                    jd.setEnabled(true);
                }

            };

            jb.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (sw.isDone()) {
                        jd.setVisible(false);
                    } else {
                        sw.cancel(true);
                    }
                }
            });

            sw.execute();

            jd.setVisible(true);
        }
    }

}
