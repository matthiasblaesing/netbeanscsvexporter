

package eu.doppel_helix.netbeans.csvexporter.core.config;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class ExporterConfigTable {
    private String schema;
    private String catalog;
    private String table;
    private String filename;

    private ExporterConfig config;
    private final List<ExporterConfigColumn> columns = new ArrayList<>();

    public ExporterConfigTable() {
    }

    public ExporterConfigTable(ExporterConfig config, String catalog, String schema, String table) {
        this.table = table;
        this.schema = schema;
        this.catalog = catalog;
        this.filename = toFileName(table);
        this.config = config;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
        setFilename(toFileName(table));
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filenameBase) {
        this.filename = filenameBase;
    }

    @XmlTransient
    public ExporterConfig getConfig() {
        return config;
    }

    public void setConfig(ExporterConfig config) {
        this.config = config;
    }
    
    public void setColumns(List<ExporterConfigColumn> columns) {
        this.columns.clear();
        this.columns.addAll(columns);
    }
    
    public List<ExporterConfigColumn> getColumns() {
        return columns;
    }
    
    public ExporterConfigColumn addColumn(String title, String name, int sqlType) {
        ExporterConfigColumn ecpc = new ExporterConfigColumn(this, title, sqlType, name, config.getBaseConfig().getConverterConfig(sqlType));
        columns.add(ecpc);
        return ecpc;
    }
    
    private static String toFileName(String table) {
        return table.toUpperCase().replaceAll("[^A-Z0-9]", "") + ".csv";
    }
}
