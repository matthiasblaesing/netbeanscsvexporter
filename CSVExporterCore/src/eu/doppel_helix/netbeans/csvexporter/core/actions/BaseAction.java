package eu.doppel_helix.netbeans.csvexporter.core.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.netbeans.api.db.explorer.DatabaseConnection;

import org.openide.nodes.Node;
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
    
    protected static DatabaseConnection findConnection(Node[] nodes) {
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
}
