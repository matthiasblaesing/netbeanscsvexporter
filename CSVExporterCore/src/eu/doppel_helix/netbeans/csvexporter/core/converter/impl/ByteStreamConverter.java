
package eu.doppel_helix.netbeans.csvexporter.core.converter.impl;

import eu.doppel_helix.netbeans.csvexporter.core.config.external.ByteExportFormat;
import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverter;
import eu.doppel_helix.netbeans.csvexporter.core.config.CSVConverterConfig;
import eu.doppel_helix.netbeans.csvexporter.core.converter.NamePrefixReceiver;
import eu.doppel_helix.netbeans.csvexporter.core.config.ExternalizationFormat;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;

public class ByteStreamConverter extends CSVConverter implements NamePrefixReceiver {
    private final static int transferBufferSize = 10240;
    private final static Set<Integer> supportedTypes = new HashSet<>();
    private final static Pattern informixLobPattern = Pattern.compile("(?:(\\d+),(\\d+),)?(.*)");
    
    static {
        supportedTypes.add(java.sql.Types.LONGVARBINARY);
        supportedTypes.add(java.sql.Types.VARBINARY);
        supportedTypes.add(java.sql.Types.BLOB);
        supportedTypes.add(java.sql.Types.BINARY);
    }
    
    private CSVConverterConfig config;
    private String namePrefix = "";
    private ByteExportFormat format = ByteExportFormat.HEXADECIMAL;
    private boolean externalizeLobs = true;
    private ExternalizationFormat externalizationFormat = ExternalizationFormat.INFORMIX;
    
    @Override
    public Set<Integer> getSupportedTypes() {
        return Collections.unmodifiableSet(supportedTypes);
    }

    public boolean isExternalizeLobs() {
        return externalizeLobs;
    }

    public void setExternalizeLobs(boolean externalizeBlobs) {
        this.externalizeLobs = externalizeBlobs;
    }

    public ExternalizationFormat getExternalizationFormat() {
        return externalizationFormat;
    }

    public void setExternalizationFormat(ExternalizationFormat externalizationFormat) {
        this.externalizationFormat = externalizationFormat;
    }
    
    public ByteExportFormat getFormat() {
        return format;
    }

    public void setFormat(ByteExportFormat format) {
        this.format = format;
    }
    
    @Override
    public String getNamePrefix() {
        return namePrefix;
    }
    
    @Override
    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }
    
    @Override
    public void setCSVExporterConfig(CSVConverterConfig config) {
        this.config = config;
    }

    @Override
    public String getValue(ResultSet rs, int i) throws SQLException, IOException {
        InputStream is = rs.getBinaryStream(i);
        if (is == null) {
            return "";
        }

        if (isExternalizeLobs()) {
            String uuid = UUID.randomUUID().toString();
            String fileName;
            if (getNamePrefix() == null || getNamePrefix().isEmpty()) {
                fileName = uuid;
            } else {
                fileName = getNamePrefix() + "_" + uuid;
            }
            File targetFile = new File(config.getBasePath(), fileName);
            FileOutputStream fos = new FileOutputStream(targetFile);
            
            transferData(is, fos);

            is.close();
            fos.close();
            // @todo: Only supports Informix-Format
            return targetFile.getName();
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            transferData(is, baos);
            switch (getFormat()) {
                case BASE64:
                    return printBase64Binary(baos.toByteArray());
                case HEXADECIMAL:
                    byte[] byteArray = baos.toByteArray();
                    StringBuilder sb = new StringBuilder(byteArray.length * 2);
                    Formatter f = new Formatter(sb);
                    for (byte b : byteArray) {
                        f.format("%02X", b);
                    }
                    return sb.toString();
                default:
                    assert false : "unreachable";
                    throw new IllegalStateException("Unknown encoding scheme for byte-Array detected");
            }
        }
    }
    
    private void transferData(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[transferBufferSize];
        int read;
        while ((read = is.read(buffer)) > 0) {
            os.write(buffer, 0, read);
        }
    }

    @Override
    public void setValue(PreparedStatement ps, int position, String input) throws Exception {
        if (input == null) {
            ps.setNull(position, sqlType);
        } else {
            if (isExternalizeLobs()) {
                // @todo: Only supports Informix-Format
                Matcher filenameMatcher = informixLobPattern.matcher(input);
                if(! filenameMatcher.matches()) {
                    throw new IOException(String.format("Illegal Externalization () Format found!", input));
                } else if (filenameMatcher.group(1) != null || filenameMatcher.group(2) != null) {
                    throw new IOException(String.format("Extended Informix Format (subindex) found and not supported!", input));
                }
                String filename = filenameMatcher.group(3);
                File sourceFile = new File(config.getBasePath(), filename);
                FileInputStream fis = new FileInputStream(sourceFile);
                ps.setBinaryStream(position, fis);
            } else {
                switch (getFormat()) {
                    case BASE64:
                        ps.setBinaryStream(position, new ByteArrayInputStream(parseBase64Binary(input)));
                        break;
                    case HEXADECIMAL:
                        byte[] bytes = new byte[input.length() / 2];
                        for (int i = 0; i < (input.length() / 2); i++) {
                            int offset = i * 2;
                            bytes[i] = (byte) Integer.parseInt(input.substring(offset, offset
                                    + 2), 16);
                        }
                        ps.setBinaryStream(position, new ByteArrayInputStream(bytes));
                        break;
                    default:
                        assert false : "unreachable";
                        throw new IllegalStateException("Unknown encoding scheme for byte-Array detected");
                }
            }
        }
    }
    
    @Override
    public Map<String, String> getProperties() {
        Map<String,String> result = super.getProperties();
        result.put("format", format.name());
        result.put("externalizeLobs", Boolean.toString(externalizeLobs));
        result.put("externalizationFormat", externalizationFormat.name());
        result.put("namePrefix", namePrefix);
        return result;
    }

    @Override
    public void setProperties(Map<String, String> map) {
        super.setProperties(map);
        if (map.containsKey("format") && map.get("format") != null) {
            format = ByteExportFormat.valueOf(map.get("format"));
        }
        if (map.containsKey("externalizeLobs") && map.get("externalizeLobs") != null) {
            externalizeLobs = Boolean.valueOf(map.get("externalizeLobs"));
        }
        if (map.containsKey("externalizationFormat") && map.get("externalizationFormat") != null) {
            externalizationFormat = ExternalizationFormat.valueOf(map.get("externalizationFormat"));
        }
        if (map.containsKey("namePrefix") && map.get("namePrefix") != null) {
            namePrefix = map.get("namePrefix");
        }
    }
}
