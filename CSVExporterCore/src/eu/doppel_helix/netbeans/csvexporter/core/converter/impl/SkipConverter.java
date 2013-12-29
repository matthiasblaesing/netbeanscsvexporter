

package eu.doppel_helix.netbeans.csvexporter.core.converter.impl;

import eu.doppel_helix.netbeans.csvexporter.core.config.ExternalizationFormat;
import eu.doppel_helix.netbeans.csvexporter.core.config.external.ByteExportFormat;
import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SkipConverter extends CSVConverter {
    @Override
    public Set<Integer> getSupportedTypes() {
        // @todo: This filter works everywhere ...
        return null;
    }

    @Override
    public String getValue(ResultSet rs, int i) throws SQLException, IOException {
        return "";
    }

    @Override
    public void setValue(PreparedStatement ps, int position, String input) throws Exception {
        ps.setNull(position, sqlType);
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
