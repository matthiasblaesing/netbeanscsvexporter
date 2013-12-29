package eu.doppel_helix.netbeans.csvexporter.core.config.node;

import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfigColumn;
import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfigTable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

public class ExporterConfigTableChildren extends Children.Array {

    private ExporterConfigTable ect;

    public ExporterConfigTableChildren(ExporterConfigTable ect) {
        this.ect = ect;
    }

    @Override
    protected Collection<Node> initCollection() {
        List<ExporterConfigColumn> eccs = ect.getColumns();
        List<Node> nodes = new ArrayList<>(eccs.size());
        for (ExporterConfigColumn ecc : eccs) {
            nodes.add(new ExporterConfigColumnNode(ecc));
        }
        return nodes;
    }

}
