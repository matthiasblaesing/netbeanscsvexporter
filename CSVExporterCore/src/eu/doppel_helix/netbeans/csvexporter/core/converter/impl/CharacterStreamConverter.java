package eu.doppel_helix.netbeans.csvexporter.core.converter.impl;

import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverter;
import eu.doppel_helix.netbeans.csvexporter.core.config.CSVConverterConfig;
import eu.doppel_helix.netbeans.csvexporter.core.config.ExternalizationFormat;
import eu.doppel_helix.netbeans.csvexporter.core.converter.NamePrefixReceiver;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharacterStreamConverter extends CSVConverter implements NamePrefixReceiver {

    private final static int transferBufferSize = 10240;
    private final Pattern informixLobPattern = Pattern.compile("(?:(\\d+),(\\d+),)?(.*)");

    private final static Set<Integer> supportedTypes = new HashSet<>();

    static {
        supportedTypes.add(java.sql.Types.CLOB);
        supportedTypes.add(java.sql.Types.NCLOB);
        supportedTypes.add(java.sql.Types.LONGNVARCHAR);
        supportedTypes.add(java.sql.Types.LONGVARCHAR);
    }

    private CSVConverterConfig config;
    private String charset = "UTF-8";
    private String namePrefix = null;
    private boolean externalizeLobs = false;
    private ExternalizationFormat externalizationFormat = ExternalizationFormat.INFORMIX;

    @Override
    public void setCSVExporterConfig(CSVConverterConfig config) {
        this.config = config;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String encoding) {
        this.charset = encoding;
    }

    @Override
    public String getNamePrefix() {
        return namePrefix;
    }

    @Override
    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
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
                Reader r = new InputStreamReader(new FileInputStream(sourceFile), getCharset());
                ps.setCharacterStream(position, r);
            } else {
                ps.setCharacterStream(position, new CharArrayReader(input.toCharArray()));
            }
        }
    }

    @Override
    public String getValue(ResultSet rs, int i) throws SQLException, IOException {
        Reader r = rs.getCharacterStream(i);
        if (r == null) {
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
            Writer w = new OutputStreamWriter(new FileOutputStream(targetFile), getCharset());

            transferData(r, w);

            // @todo: Only supports Informix-Format
            return targetFile.getName();
        } else {
            StringWriter sbw = new StringWriter();
            transferData(r, sbw);
            return sbw.toString();
        }
    }

    @Override
    public Set<Integer> getSupportedTypes() {
        return Collections.unmodifiableSet(supportedTypes);
    }

    private void transferData(Reader is, Writer os) throws IOException {
        char[] buffer = new char[transferBufferSize];
        int read;
        while ((read = is.read(buffer)) > 0) {
            os.write(buffer, 0, read);
        }
        is.close();
        os.close();
    }
    
    @Override
    public Map<String, String> getProperties() {
        Map<String,String> result = super.getProperties();
        result.put("charset", charset);
        result.put("externalizeLobs", Boolean.toString(externalizeLobs));
        result.put("externalizationFormat", externalizationFormat.name());
        result.put("namePrefix", namePrefix);
        return result;
    }

    @Override
    public void setProperties(Map<String, String> map) {
        super.setProperties(map);
        if (map.containsKey("charset") && map.get("charset") != null) {
            charset = map.get("charset");
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
