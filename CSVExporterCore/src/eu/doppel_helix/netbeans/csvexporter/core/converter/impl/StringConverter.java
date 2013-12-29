package eu.doppel_helix.netbeans.csvexporter.core.converter.impl;

import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StringConverter extends CSVConverter {

    private final static Set<Integer> supportedTypes = new HashSet<>();

    static {
        supportedTypes.add(java.sql.Types.CHAR);
        supportedTypes.add(java.sql.Types.VARCHAR);
        supportedTypes.add(java.sql.Types.LONGVARCHAR);
        supportedTypes.add(java.sql.Types.NCHAR);
        supportedTypes.add(java.sql.Types.NVARCHAR);
        supportedTypes.add(java.sql.Types.LONGNVARCHAR);
    }
    
    @Override
    public void setValue(PreparedStatement ps, int position, String input) throws Exception {
        if(input == null) {
            ps.setNull(position, sqlType);
        }
        switch (sqlType) {
            case java.sql.Types.LONGNVARCHAR:
            case java.sql.Types.NCHAR:
            case java.sql.Types.NVARCHAR:
                ps.setNString(position, input);
                break;
            case java.sql.Types.LONGVARCHAR:
            case java.sql.Types.CHAR:
            case java.sql.Types.VARCHAR:
                ps.setString(position, input);
                break;
            default:
                throw new IllegalArgumentException("Unsupported SQLType: " + sqlType);
        }
    }
    
    @Override
    public String getValue(ResultSet rs, int i) throws SQLException, IOException {
        switch (sqlType) {
            case java.sql.Types.LONGNVARCHAR:
            case java.sql.Types.NCHAR:
            case java.sql.Types.NVARCHAR:
                return rs.getNString(i);
            case java.sql.Types.LONGVARCHAR:
            case java.sql.Types.CHAR:
            case java.sql.Types.VARCHAR:
                return rs.getString(i);
            default:
                throw new IllegalArgumentException("Unsupported SQLType: " + sqlType);
        }         
    }
    
    @Override
    public Set<Integer> getSupportedTypes() {
        return Collections.unmodifiableSet(supportedTypes);
    }
    
    @Override
    public Map<String, String> getProperties() {
        Map<String,String> result = super.getProperties();
        return result;
    }

    @Override
    public void setProperties(Map<String, String> map) {
        super.setProperties(map);
    }
}
