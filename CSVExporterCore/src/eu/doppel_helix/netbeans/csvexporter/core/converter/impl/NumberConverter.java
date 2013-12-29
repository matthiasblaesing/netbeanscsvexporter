package eu.doppel_helix.netbeans.csvexporter.core.converter.impl;

import eu.doppel_helix.netbeans.csvexporter.core.config.ExternalizationFormat;
import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NumberConverter extends CSVConverter {

    private final static Set<Integer> supportedTypes = new HashSet<>();

    static {
        supportedTypes.add(java.sql.Types.BIT);
        supportedTypes.add(java.sql.Types.TINYINT);
        supportedTypes.add(java.sql.Types.SMALLINT);
        supportedTypes.add(java.sql.Types.INTEGER);
        supportedTypes.add(java.sql.Types.BIGINT);
        supportedTypes.add(java.sql.Types.NUMERIC);
        supportedTypes.add(java.sql.Types.DECIMAL);
        supportedTypes.add(java.sql.Types.REAL);
        supportedTypes.add(java.sql.Types.DOUBLE);
        supportedTypes.add(java.sql.Types.FLOAT);

    }

    @Override
    public String getValue(ResultSet rs, int i) throws SQLException, IOException {
        if (sqlType == 0) {
            throw new IllegalStateException("sqlType or format undefined");
        }
        switch (sqlType) {
            case java.sql.Types.BIT:
            case java.sql.Types.TINYINT:
            case java.sql.Types.SMALLINT:
            case java.sql.Types.INTEGER:
                int intVal = rs.getInt(i);
                return rs.wasNull() ? "" : Integer.toString(intVal);
            case java.sql.Types.BIGINT:
                long longVal = rs.getLong(i);
                return rs.wasNull() ? "" : Long.toString(longVal);
            case java.sql.Types.NUMERIC:
            case java.sql.Types.DECIMAL:
                BigDecimal val = rs.getBigDecimal(i);
                return val == null ? "" : val.toString();
            case java.sql.Types.REAL:
            case java.sql.Types.DOUBLE:
            case java.sql.Types.FLOAT:
                double doubleVal = rs.getDouble(i);
                return rs.wasNull() ? "" : Double.toString(doubleVal);
            default:
                throw new IllegalArgumentException("Unsupported SQLType");
        }
    }

    @Override
    public void setValue(PreparedStatement ps, int position, String input) throws Exception {
        if (sqlType == 0) {
            throw new IllegalStateException("sqlType undefined");
        }
        if (input == null || input.isEmpty()) {
            ps.setNull(position, sqlType);
        }
        switch (sqlType) {
            case java.sql.Types.BIT:
            case java.sql.Types.TINYINT:
            case java.sql.Types.SMALLINT:
            case java.sql.Types.INTEGER:
                ps.setInt(position, Integer.parseInt(input));
                break;
            case java.sql.Types.BIGINT:
                ps.setLong(position, Long.parseLong(input));
                break;
            case java.sql.Types.NUMERIC:
            case java.sql.Types.DECIMAL:
                ps.setBigDecimal(position, new BigDecimal(input));
                break;
            case java.sql.Types.REAL:
            case java.sql.Types.DOUBLE:
            case java.sql.Types.FLOAT:
                ps.setDouble(position, Double.parseDouble(input));
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
        return result;
    }

    @Override
    public void setProperties(Map<String, String> map) {
        super.setProperties(map);
    }
}
