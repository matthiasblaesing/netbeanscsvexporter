/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.doppel_helix.netbeans.csvexporter.core.actions;

import au.com.bytecode.opencsv.CSVWriter;
import static eu.doppel_helix.netbeans.csvexporter.core.actions.BaseAction.findConnection;
import static eu.doppel_helix.netbeans.csvexporter.core.util.JDBC.getColumnsForExporter;
import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfig;
import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfigTable;
import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverterFactory;
import eu.doppel_helix.netbeans.csvexporter.core.util.JDBC;
import eu.doppel_helix.netbeans.csvexporter.core.util.LoggingPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.SwingWorker;
import org.netbeans.api.db.explorer.DatabaseConnection;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.windows.WindowManager;

// An example action demonstrating how the wizard could be called from within
// your code. You can move the code below wherever you need, or register an action:
// @ActionID(category="...", id="eu.doppel_helix.netbeans.csvexporter.core.importer.TableImporterWizardAction")
// @ActionRegistration(displayName="Open TableImporter Wizard")
// @ActionReference(path="Menu/Tools", position=...)
public final class TableExporterWizardAction extends BaseAction {

    private static String buildQuerySQL(Connection c, String tableName, int offset, int size) {
        return String.format("SELECT * FROM %1$s LIMIT %3$d OFFSET %2$d", tableName, offset, size);
    }

    @Override
    protected void performAction(Node[] activatedNodes) {
        List<WizardDescriptor.Panel<WizardDescriptor>> panels = new ArrayList<WizardDescriptor.Panel<WizardDescriptor>>();
        panels.add(new TableExporterWizardPanel1());
        panels.add(new TableExporterWizardPanel2());
        panels.add(new TableExporterWizardPanel3());
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
        WizardDescriptor wiz = new WizardDescriptor(new WizardDescriptor.ArrayIterator<>(panels));
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wiz.setTitleFormat(new MessageFormat("{0}"));
        wiz.setTitle("Export Tables");
        final ExporterConfig ec = new ExporterConfig(CSVConverterFactory.getInstance().getDefaultConfig());

        DatabaseConnection dc = findConnection(activatedNodes);
        final Connection c = dc.getJDBCConnection();

        for (Node n : activatedNodes) {
            Collection objects = n.getLookup().lookupAll(Object.class);
            for (Object o : objects) {
                if (o.getClass().getName().equals("org.netbeans.modules.db.metadata.model.api.MetadataElementHandle")) {
                    try {
                        Field f = o.getClass().getDeclaredField("names");
                        f.setAccessible(true);
                        String[] names = (String[]) f.get(o);
                        f = o.getClass().getDeclaredField("kinds");
                        f.setAccessible(true);
                        Object[] kinds = (Object[]) f.get(o);
                        if (kinds.length == 3
                                && ((Enum) kinds[2]).name().equals("TABLE")) {
                            getColumnsForExporter(c, ec, names[0], names[1], names[2]);
                        }
                    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }

        wiz.putProperty("ExporterConfig", ec);
        wiz.putProperty("ColumnConfigDone", false);

        if (DialogDisplayer.getDefault().notify(wiz)
                == WizardDescriptor.FINISH_OPTION) {

            final LoggingPanel lp = new LoggingPanel();

            final JDialog jd = new JDialog(WindowManager.getDefault().getMainWindow(), "Export");
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
                        lp.logMessage("BEGIN EXPORT");
                        lp.setRunning(true);
                        for (ExporterConfigTable ect : ec.getTableConfig()) {
                            File outputFile = new File(ec.getBaseConfig().getBasePath(),
                                                       ect.getFilename());
                            try (Statement s = c.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                                    CSVWriter out = new CSVWriter(
                                                new OutputStreamWriter(
                                                    new BufferedOutputStream(
                                                        new FileOutputStream(outputFile)),
                                                    ec.getBaseConfig().getCharset()),
                                            ec.getBaseConfig().getSeperator(),
                                            ec.getBaseConfig().getQuoteChar(),
                                            ec.getBaseConfig().getEscapeChar(),
                                            ec.getBaseConfig().getLineEnd().getEncoding()
                                    )) {
                                
                                s.setFetchSize(10);

                                String tableName = buildTableName(c, ect);

                                lp.logMessage("Exporting: " + tableName);

                                final int LIMIT = 100;
                                int rowCount = 0;

                                long start = System.currentTimeMillis();

                                do {
                                    try (ResultSet rs = s.executeQuery(buildQuerySQL(c, tableName, rowCount, LIMIT))) {
                                        if (rowCount == 0
                                                && ec.getBaseConfig().isIncludeColumnTitle()) {
                                            String[] header = new String[ect.getColumns().size()];
                                            for (int i = 0; i
                                                    < ect.getColumns().size(); i++) {
                                                header[i] = ect.getColumns().get(i).getTitle();
                                            }
                                            out.writeNext(header, ec.getBaseConfig().isAlwaysQuote());
                                        }

                                        while (rs.next()) {
                                            String[] row = new String[ect.getColumns().size()];
                                            for (int i = 0; i
                                                    < ect.getColumns().size(); i++) {
                                                row[i] = ect.getColumns().get(i).getCSVConverter().getValue(rs, i
                                                        + 1);
                                            }
                                            out.writeNext(row, ec.getBaseConfig().isAlwaysQuote());
                                            rowCount++;
                                            lp.setCurrent(rowCount);

                                            if (Thread.interrupted()) {
                                                rs.close();
                                                throw new InterruptedException();
                                            }
                                        }
                                    }
                                } while (rowCount % LIMIT == 0);

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

    private String buildTableName(Connection c, ExporterConfigTable ect) throws SQLException {
        String tableName = "";
        if (ect.getCatalog() != null) {
            tableName = tableName
                    + JDBC.quoteIdentifier(c, ect.getCatalog())
                    + c.getMetaData().getCatalogSeparator();
        }
        if (ect.getSchema() != null) {
            tableName = tableName
                    + JDBC.quoteIdentifier(c, ect.getSchema())
                    + ".";
        }
        tableName += JDBC.quoteIdentifier(c, ect.getTable());
        return tableName;
    }

    @Override
    protected boolean enable(Node[] activatedNodes) {
        return true;
    }

    @Override
    public String getName() {
        return "Export Tables";
    }
}
