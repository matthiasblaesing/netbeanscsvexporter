/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.doppel_helix.netbeans.csvexporter.core.converter.impl;

import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverter;
import eu.doppel_helix.netbeans.csvexporter.core.config.CSVConverterConfig;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SimpleDateTimeConverter extends CSVConverter {

    private final static Set<Integer> supportedTypes = new HashSet<>();

    static {
        supportedTypes.add(java.sql.Types.TIME);
        supportedTypes.add(java.sql.Types.DATE);
        supportedTypes.add(java.sql.Types.TIMESTAMP);
    }

    private String format;
    private SimpleDateFormat cachedFormatter;
    private CSVConverterConfig config;

    @Override
    public void setCSVExporterConfig(CSVConverterConfig config) {
        this.config = config;
    }

    public void setFormat(String formatString) {
        format = formatString;
        cachedFormatter = null;
    }

    public String getFormat() {
        return format;
    }

    private SimpleDateFormat getFormatter() {
        if (cachedFormatter == null && getFormat() != null) {
            cachedFormatter = new SimpleDateFormat(getFormat());
        }
        return cachedFormatter;
    }

    @Override
    public String getValue(ResultSet rs, int i) throws SQLException, IOException {
        int sqlType = getSqlType();
        SimpleDateFormat format = getFormatter();
        if (sqlType == 0) {
            throw new IllegalStateException("sqlType undefined");
        }
        if (format == null) {
            throw new IllegalStateException("format undefined");
        }
        switch (sqlType) {
            case (java.sql.Types.TIME):
                java.sql.Time time = rs.getTime(i);
                if (time == null) {
                    return "";
                }
                return getFormatter().format(time);
            case (java.sql.Types.DATE):
                java.sql.Date date = rs.getDate(i);
                if (date == null) {
                    return "";
                }
                return getFormatter().format(date);
            case (java.sql.Types.TIMESTAMP):
                java.sql.Timestamp ts = rs.getTimestamp(i);
                if (ts == null) {
                    return "";
                }
                return getFormatter().format(ts);
            default:
                throw new IllegalArgumentException("Unsupported SQLType");
        }
    }

    @Override
    public void setValue(PreparedStatement ps, int position, String input) throws Exception {
        if (format == null || sqlType == 0) {
            throw new IllegalStateException("sqlType or format undefined");
        }
        if (input == null || input.isEmpty()) {
            ps.setNull(position, sqlType);
        }
        switch (sqlType) {
            case (java.sql.Types.TIME):
                ps.setTime(position, new java.sql.Time(getFormatter().parse(input).getTime()));
                break;
            case (java.sql.Types.DATE):
                ps.setDate(position, new java.sql.Date(getFormatter().parse(input).getTime()));
                break;
            case (java.sql.Types.TIMESTAMP):
                ps.setTimestamp(position, new java.sql.Timestamp(getFormatter().parse(input).getTime()));
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
        result.put("format", format);
        return result;
    }

    @Override
    public void setProperties(Map<String, String> map) {
        super.setProperties(map);
        if (map.containsKey("format") && map.get("format") != null) {
            format = map.get("format");
        }
    }
}
