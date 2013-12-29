
package eu.doppel_helix.netbeans.csvexporter.core.config.node;

import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfig;
import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfigTable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

public class ExporterConfigChildren extends Children.Array {
    private final ExporterConfig ec;

    public ExporterConfigChildren(ExporterConfig ec) {
        this.ec = ec;

    }
    
    @Override
    protected Collection<Node> initCollection() {
        List<ExporterConfigTable> ects = ec.getTableConfig();
        List<Node> nodes = new ArrayList<>(ects.size());
        for(ExporterConfigTable ect: ects) {
            nodes.add(new ExporterConfigTableNode(ect));
        }
        return nodes;
    }
}
