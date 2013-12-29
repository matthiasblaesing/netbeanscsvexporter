package eu.doppel_helix.netbeans.csvexporter.core.converter.impl;

import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BooleanConverter extends CSVConverter {
    private final static Set<Integer> supportedTypes = new HashSet<>();
    
    static {
        supportedTypes.add(java.sql.Types.BOOLEAN);        
    }
    
    private String booleanFalseString = null;
    private String booleanTrueString = null;



    public String getBooleanFalseString() {
        if(booleanFalseString == null) {
            throw new IllegalStateException("BooleanFalseString not defined");
        }
        return booleanFalseString;
    }

    public void setBooleanFalseString(String booleanFalseString) {
        this.booleanFalseString = booleanFalseString;
    }

    public String getBooleanTrueString() {
        if(booleanTrueString == null) {
            throw new IllegalStateException("BooleanTrueString not defined");
        }
        return booleanTrueString;
    }

    public void setBooleanTrueString(String booleanTrueString) {
        this.booleanTrueString = booleanTrueString;
    }
    
    @Override
    public String getValue(ResultSet rs, int i) throws SQLException, IOException {
        if(sqlType == 0) {
            throw new IllegalStateException("sqlType or format undefined");
        }
        switch (sqlType) {
            case java.sql.Types.BOOLEAN:
                boolean boolVal = rs.getBoolean(i);
                return rs.wasNull() ? "" : (boolVal ? getBooleanTrueString() : getBooleanFalseString());
            default:
                throw new IllegalArgumentException("Unsupported SQLType");
        }
    }
    
    @Override
    public void setValue(PreparedStatement ps, int position, String input) throws Exception {
        if (sqlType == 0) {
            throw new IllegalStateException("sqlType undefined");
        }
        
        String trueString = getBooleanTrueString();
        String falseString = getBooleanFalseString();
        
        if (input == null || input.isEmpty() || !(trueString.equalsIgnoreCase(input) || falseString.equalsIgnoreCase(input) )) {
            ps.setNull(position, sqlType);
        }
        
        switch (sqlType) {
            case java.sql.Types.BOOLEAN:
                ps.setBoolean(position, trueString.equalsIgnoreCase(input));
                break;
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
        result.put("booleanFalseString", booleanFalseString);
        result.put("booleanTrueString", booleanTrueString);
        return result;
    }

    @Override
    public void setProperties(Map<String, String> map) {
        super.setProperties(map);
        if(map.containsKey("booleanFalseString") && map.get("booleanFalseString") != null) {
            booleanFalseString = map.get("booleanFalseString");
        }
        if (map.containsKey("booleanTrueString") && map.get("booleanTrueString") != null) {
            booleanTrueString = map.get("booleanTrueString");
        }
    }
}
