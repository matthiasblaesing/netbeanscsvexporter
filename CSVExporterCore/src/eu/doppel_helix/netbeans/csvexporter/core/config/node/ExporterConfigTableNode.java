
package eu.doppel_helix.netbeans.csvexporter.core.config.node;

import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfigTable;
import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;

public class ExporterConfigTableNode extends AbstractNode {
    private final ExporterConfigTable ect;
    
    public ExporterConfigTableNode(ExporterConfigTable ect) {
        super(new ExporterConfigTableChildren(ect));
        this.ect = ect;
        setIconBaseWithExtension("eu/doppel_helix/netbeans/csvexporter/core/resources/table.gif");
        Sheet s = createSheet();
        Sheet.Set ss = new Sheet.Set();
        ss.put(new PropertySupport.ReadOnly<String>("Catalog", String.class, "Catalog", "") {

            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                return ExporterConfigTableNode.this.ect.getCatalog();
            }
        });
        ss.put(new PropertySupport.ReadOnly<String>("Schema", String.class, "Schema", "") {

            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                return ExporterConfigTableNode.this.ect.getSchema();
            }
        });
        ss.put(new PropertySupport.ReadOnly<String>("Table", String.class, "Table", "") {

            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                return ExporterConfigTableNode.this.ect.getTable();
            }
        });
        ss.put(new PropertySupport.ReadWrite<String>("Filename", String.class, "Filename", "") {

            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                return ExporterConfigTableNode.this.ect.getFilename();
            }

            @Override
            public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                ExporterConfigTableNode.this.ect.setFilename(val);
            }
        });
        s.put(ss);
        setSheet(s);
    }

    @Override
    public String getName() {
        return ect.getTable();
    }
    
    
 
}
