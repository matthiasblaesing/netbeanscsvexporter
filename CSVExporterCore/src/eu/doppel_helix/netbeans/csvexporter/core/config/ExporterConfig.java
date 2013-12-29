

package eu.doppel_helix.netbeans.csvexporter.core.config;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExporterConfig {
    private CSVConverterConfig baseConfig;
    private final List<ExporterConfigTable> tableConfig = new ArrayList<>();

    public ExporterConfig() {
        baseConfig = new CSVConverterConfig();
    }

    /**
     * Construct new ExportConfig based on an existing CSVConverterConfig.
     * 
     * The supplied CSVConverterConfig is cloned.
     * 
     * @param baseConfig 
     */
    public ExporterConfig(CSVConverterConfig baseConfig) {
        this.baseConfig = new CSVConverterConfig(baseConfig);
    }

    public CSVConverterConfig getBaseConfig() {
        return baseConfig;
    }

    /**
     * Set the BaseConfig of the ExporterConfig - the CSVConvertConfig is cloned
     * 
     * @param baseConfig 
     */
    public void setBaseConfig(CSVConverterConfig baseConfig) {
        this.baseConfig = new CSVConverterConfig(baseConfig);
    }

    public void setTableConfig(List<ExporterConfigTable> tableConfig) {
        this.tableConfig.clear();
        this.tableConfig.addAll(tableConfig);
    }
    
    public List<ExporterConfigTable> getTableConfig() {
        return tableConfig;
    }
    
    public ExporterConfigTable getTableConfig(String catalog, String schema, String table) {
        ExporterConfigTable inexactMatch = null;        
        for(ExporterConfigTable ect: tableConfig) {
            if(ect.getTable().equals(table) 
                    && ect.getSchema().equals(schema) 
                    && ect.getCatalog().equals(catalog)) {
                return ect;
            } else if (ect.getTable().equalsIgnoreCase(table) 
                    && ect.getSchema().equalsIgnoreCase(schema) 
                    && ect.getCatalog().equalsIgnoreCase(catalog)) {
                inexactMatch = ect;
            }
        }
        return inexactMatch;
    }
    
    public ExporterConfigTable addTableConfig(String catalog, String schema, String table) {
        ExporterConfigTable ect = new ExporterConfigTable(this, catalog, schema, table);
        tableConfig.add(ect);
        return ect;
    }
}
