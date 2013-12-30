package eu.doppel_helix.netbeans.csvexporter.core.actions;

import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfig;
import static eu.doppel_helix.netbeans.csvexporter.core.util.JDBC.getColumnsForExporter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.netbeans.api.db.explorer.DatabaseConnection;

import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.actions.NodeAction;

abstract class BaseAction extends NodeAction {
    @Override
    public boolean asynchronous() {
        return false;
    }
    
    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx("eu.doppel_helix.netbeans.csvexporter.core.actions");
    }
    
    protected DatabaseConnection findConnection(Node[] nodes) {
        List<Node> toScan = new ArrayList<>(Arrays.asList(nodes));
        while(toScan.size() > 0) {
            Node n = toScan.remove(0);
            DatabaseConnection dc = n.getLookup().lookup(DatabaseConnection.class);
            if(dc != null) {
                return dc;
            }
            if(n.getParentNode() != null) {
                toScan.add(n.getParentNode());
            }
        }
        return null;
    }
    
    protected void extractTablesFromNodes(Node[] activatedNodes, final Connection c, final ExporterConfig ec) throws RuntimeException {
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
    }
}
