package eu.doppel_helix.netbeans.csvexporter.core.converter.impl;

import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class XMLConverter extends CSVConverter {

    private final static Set<Integer> supportedTypes = new HashSet<>();

    static {
        supportedTypes.add(java.sql.Types.SQLXML);
    }
    
    @Override
    public void setValue(PreparedStatement ps, int position, String input) throws Exception {
        if(input == null) {
            ps.setNull(position, sqlType);
        }
        switch (sqlType) {
            case java.sql.Types.SQLXML:
                SQLXML xml = ps.getConnection().createSQLXML();
                xml.setString(input);
                ps.setSQLXML(position, xml);
                break;
            default:
                throw new IllegalArgumentException("Unsupported SQLType: " + sqlType);
        }
    }
    
    @Override
    public String getValue(ResultSet rs, int i) throws SQLException, IOException {
        switch (sqlType) {
            case java.sql.Types.SQLXML:
                return rs.getSQLXML(i).getString();
            default:
                throw new IllegalArgumentException("Unsupported SQLType");
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
