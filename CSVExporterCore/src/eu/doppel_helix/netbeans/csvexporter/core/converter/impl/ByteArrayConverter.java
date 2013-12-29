
package eu.doppel_helix.netbeans.csvexporter.core.converter.impl;

import eu.doppel_helix.netbeans.csvexporter.core.config.external.ByteExportFormat;
import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

public class ByteArrayConverter extends CSVConverter {
    private final static Set<Integer> supportedTypes = new HashSet<>();
    
    static {
        supportedTypes.add(java.sql.Types.LONGVARBINARY);
        supportedTypes.add(java.sql.Types.VARBINARY);
        supportedTypes.add(java.sql.Types.BINARY);
    }
    
    private ByteExportFormat format = ByteExportFormat.HEXADECIMAL;
    
    @Override
    public Set<Integer> getSupportedTypes() {
        return Collections.unmodifiableSet(supportedTypes);
    }

    public ByteExportFormat getFormat() {
        return format;
    }

    public void setFormat(ByteExportFormat format) {
        this.format = format;
    }

    @Override
    public String getValue(ResultSet rs, int i) throws SQLException, IOException {
        byte[] byteArray = rs.getBytes(i);
        if (byteArray == null) {
            return "";
        }

        switch(getFormat()) {
            case BASE64:
                return printBase64Binary(byteArray);
            case HEXADECIMAL:
                StringBuilder sb = new StringBuilder(byteArray.length * 2);
                Formatter f = new Formatter(sb);
                for(byte b: byteArray) {
                    f.format("%02X", b);
                }
                return sb.toString();
            default:
                assert false: "unreachable";
                throw new IllegalStateException("Unknown encoding scheme for byte-Array detected");
        }
        
    }

    @Override
    public void setValue(PreparedStatement ps, int position, String input) throws Exception {
        if (input == null) {
            ps.setNull(position, sqlType);
        } else {
            switch (getFormat()) {
                case BASE64:
                    ps.setBytes(position, parseBase64Binary(input));
                    break;
                case HEXADECIMAL:
                    byte[] bytes = new byte[input.length() / 2];
                    for(int i = 0; i < (input.length() / 2); i++) {
                        int offset = i * 2;
                        bytes[i] = (byte) Integer.parseInt(input.substring(offset, offset + 2), 16);
                    }
                    ps.setBytes(position, bytes);
                    break;
                default:
                    assert false : "unreachable";
                    throw new IllegalStateException("Unknown encoding scheme for byte-Array detected");
            }
        }
    }
    
    @Override
    public Map<String, String> getProperties() {
        Map<String,String> result = super.getProperties();
        result.put("format", format.name());
        return result;
    }

    @Override
    public void setProperties(Map<String, String> map) {
        super.setProperties(map);
        if(map.containsKey("format") && map.get("format") != null) {
            format = ByteExportFormat.valueOf(map.get("format"));
        }
    }
}
