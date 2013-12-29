package eu.doppel_helix.netbeans.csvexporter.core.config;

import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverter;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

public class ExporterConfigColumn {

    private class CSVConverterConfigTypeInvalidator extends CSVConverterConfigType {
        private CSVConverterConfigType delegate;

        public CSVConverterConfigTypeInvalidator(CSVConverterConfigType delegate) {
            this.delegate = delegate;
        }

        @Override
        public int getSqlType() {
            return delegate.getSqlType();
        }

        @Override
        public void setSqlType(int sqlType) {
            delegate.setSqlType(sqlType);
        }

        @Override
        public String getConverter() {
            return delegate.getConverter();
        }

        @Override
        public void setConverter(String converter) {
            delegate.setConverter(converter);
            ExporterConfigColumn.this.converter = null;
        }

        @Override
        public Map<String, String> getParameters() {
            return delegate.getParameters();
        }

        @Override
        public CSVConverterConfig getConfig() {
            return delegate.getConfig();
        }

        @Override
        public void setConfig(CSVConverterConfig config) {
            delegate.setConfig(config);
            ExporterConfigColumn.this.converter = null;
        }

        @Override
        public void setParameters(Map<String, String> parameters) {
            delegate.setParameters(parameters);
            ExporterConfigColumn.this.converter = null;
        }

        @Override
        public CSVConverterConfigType withParameter(String parameter, String value) {
            ExporterConfigColumn.this.converter = null;
            return delegate.withParameter(parameter, value);
        }

        @Override
        public CSVConverter create(String fileNamePrefix) {
            return delegate.create(fileNamePrefix);
        }

        @Override
        public int hashCode() {
            return delegate.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return delegate.equals(obj);
        }

        @Override
        public String toString() {
            return delegate.toString();
        }
    }
        
        
    private ExporterConfigTable ect;
    private String title;
    private String name;
    private int sqlType;
    private CSVConverterConfigType converterConfig;
    private CSVConverter converter = null;

    public ExporterConfigColumn(ExporterConfigTable ect, String title, int sqlType, String columnName, CSVConverterConfigType config) {
        this.title = title;
        this.sqlType = sqlType;
        this.ect = ect;
        this.name = columnName;
        this.converterConfig = new CSVConverterConfigType(config);
        this.ect = ect;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSqlType() {
        return sqlType;
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    @XmlTransient
    public ExporterConfigTable getConfig() {
        return ect;
    }

    public void setConfig(ExporterConfigTable ect) {
        this.ect = ect;
    }

    public CSVConverterConfigType getConverterConfig() {
        return converterConfig;
    }

    public void setConverterConfig(CSVConverterConfigType converterConfig) {
        this.converterConfig = new CSVConverterConfigTypeInvalidator(
                new CSVConverterConfigType(converterConfig));
        this.converterConfig.setConfig(ect.getConfig().getBaseConfig());
        converter = null;
    }

    /**
     * @todo Implement  sensible caching
     */
    public synchronized CSVConverter getCSVConverter() {
        if(converter == null) {
            String tableFilename = getConfig().getFilename();
            converter = converterConfig.create(tableFilename.substring(0, tableFilename.lastIndexOf(".")));
        }
        return converter;
    }
}
