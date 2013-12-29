package eu.doppel_helix.netbeans.csvexporter.core.converter;

import eu.doppel_helix.netbeans.csvexporter.core.config.CSVConverterConfig;
import eu.doppel_helix.netbeans.csvexporter.core.converter.impl.BooleanConverter;
import eu.doppel_helix.netbeans.csvexporter.core.converter.impl.ByteArrayConverter;
import eu.doppel_helix.netbeans.csvexporter.core.converter.impl.ByteStreamConverter;
import eu.doppel_helix.netbeans.csvexporter.core.converter.impl.CharacterStreamConverter;
import eu.doppel_helix.netbeans.csvexporter.core.converter.impl.NumberConverter;
import eu.doppel_helix.netbeans.csvexporter.core.converter.impl.SimpleDateTimeConverter;
import eu.doppel_helix.netbeans.csvexporter.core.converter.impl.SkipConverter;
import eu.doppel_helix.netbeans.csvexporter.core.converter.impl.StringConverter;
import eu.doppel_helix.netbeans.csvexporter.core.converter.impl.XMLConverter;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class CSVConverter {
    protected int sqlType;

    /**
     * This method needs to be overriden, if the concrete implementation depends
     * on info from CSVExporterConfig.
     *
     * @param config
     */
    public void setCSVExporterConfig(CSVConverterConfig config) {
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }
    
    public int getSqlType() {
        return this.sqlType;
    }

    abstract public Set<Integer> getSupportedTypes();

    public String getName() {
        try {
            return Introspector.getBeanInfo(getClass()).getBeanDescriptor().getDisplayName();
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean isSupportedType(int sqlType) {
        Set<Integer> supportedTypes = getSupportedTypes();
        if (supportedTypes == null) {
            return true;
        } else {
            return getSupportedTypes().contains(Integer.valueOf(sqlType));
        }
    }

    abstract public String getValue(ResultSet rs, int i) throws SQLException, IOException;

    abstract public void setValue(PreparedStatement ps, int position, String input) throws Exception;

    public Map<String,String> getProperties() {
        Map<String,String> result = new HashMap<>();
        return result;
    }

    public void setProperties(Map<String, String> map) {
    }

    public static List<? extends CSVConverter> enumerateConverters() {
        return Arrays.asList(
                new BooleanConverter(),
                new ByteArrayConverter(),
                new ByteStreamConverter(),
                new CharacterStreamConverter(),
                new NumberConverter(),
                new SimpleDateTimeConverter(),
                new SkipConverter(),
                new StringConverter(),
                new XMLConverter());
    }
}
