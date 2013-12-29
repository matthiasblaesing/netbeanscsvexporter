
package eu.doppel_helix.netbeans.csvexporter.core.config.node;

import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfig;
import org.openide.nodes.AbstractNode;

public class ExporterConfigNode extends AbstractNode {
    
    
    public ExporterConfigNode(ExporterConfig ec) {
        super(new ExporterConfigChildren(ec));
    }
    
    
}
