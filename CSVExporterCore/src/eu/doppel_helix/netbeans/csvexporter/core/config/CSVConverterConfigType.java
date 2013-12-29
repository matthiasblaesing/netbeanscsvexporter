
package eu.doppel_helix.netbeans.csvexporter.core.config;

import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverter;
import eu.doppel_helix.netbeans.csvexporter.core.converter.NamePrefixReceiver;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlTransient;

public class CSVConverterConfigType {
    private int sqlType = 0;
    private String converter = "";
    private Map<String,String> parameters = new HashMap<>();
    private CSVConverterConfig config;

    public CSVConverterConfigType() {
    }

    public CSVConverterConfigType(CSVConverterConfig config, int sqlType, String converter) {
        this.sqlType = sqlType;
        this.converter = converter;
        this.config = config;
    }
    
    /**
     * Clone-Constructor - creates a copy of the supplied CSVConverterConfigType.
     * 
     * The CSVConverterConfig is not deeply copied!
     * 
     * @param ccct 
     */
    public CSVConverterConfigType(CSVConverterConfigType ccct) {
        this.sqlType = ccct.sqlType;
        this.converter = ccct.converter;
        this.parameters.putAll(ccct.parameters);
        this.config = ccct.config;
    }

    public int getSqlType() {
        return sqlType;
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    public String getConverter() {
        return converter;
    }

    public void setConverter(String converter) {
        this.converter = converter;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    @XmlTransient
    public CSVConverterConfig getConfig() {
        return config;
    }

    public void setConfig(CSVConverterConfig config) {
        this.config = config;
    }
    
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
    
    public CSVConverterConfigType withParameter(String parameter, String value) {
        this.parameters.put(parameter, value);
        return this;
    }
    
    public CSVConverter create(String fileNamePrefix) {
        try {
            Class csvConverterClass = Class.forName(getConverter());
            CSVConverter csvconverter = (CSVConverter) csvConverterClass.newInstance();
            
            csvconverter.setSqlType(sqlType);
            csvconverter.setCSVExporterConfig(config);
            csvconverter.setProperties(getParameters());
            
            if (csvconverter instanceof NamePrefixReceiver) {
                ((NamePrefixReceiver) csvconverter).setNamePrefix(fileNamePrefix);
            }
            
            return csvconverter;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}