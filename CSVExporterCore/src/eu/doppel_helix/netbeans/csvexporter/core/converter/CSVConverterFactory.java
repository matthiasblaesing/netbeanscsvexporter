package eu.doppel_helix.netbeans.csvexporter.core.converter;

import eu.doppel_helix.netbeans.csvexporter.core.config.CSVConverterConfig;
import eu.doppel_helix.netbeans.csvexporter.core.config.CSVConverterConfigType;
import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfig;
import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfigColumn;
import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfigTable;
import eu.doppel_helix.netbeans.csvexporter.core.converter.ConfigDescriptor.Source;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import static org.openide.filesystems.FileUtil.getConfigRoot;

public class CSVConverterFactory {

    private static final Logger LOG = Logger.getLogger(CSVConverterFactory.class.getName());
    private static final String CONFIG_DIRECTORY = "CSVConverter/Config"; // NOI18N
    private static final String BUILD_IN_DIRECTORY = "/eu/doppel_helix/netbeans/csvexporter/core/config/builtin/"; // NOI18N

    private static CSVConverterFactory INSTANCE;

    private JAXBContext ctx;
    
    public synchronized static CSVConverterFactory getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new CSVConverterFactory();
        }
        return INSTANCE;
    }

    private JAXBContext getCtx() throws JAXBException {
        if (ctx == null) {
            ctx = JAXBContext.newInstance("eu.doppel_helix.netbeans.csvexporter.core.config", CSVConverterFactory.class.getClassLoader());
        }
        return ctx;
    }

    private CSVConverterConfig readConfig(InputStream is) throws JAXBException {
        Unmarshaller m = getCtx().createUnmarshaller();
        CSVConverterConfig converterConfig = (CSVConverterConfig) m.unmarshal(is);
        for (CSVConverterConfigType ccpt : converterConfig.getConverters().values()) {
            ccpt.setConfig(converterConfig);
        }
        return converterConfig;
    }

    public CSVConverterConfig readConfig(String filename) throws JAXBException, IOException {
        if (filename == null) {
            return new CSVConverterConfig();
        }
        return readConfig(new FileInputStream(filename));
    }

    public CSVConverterConfig readSystemConfig(ConfigDescriptor config) throws JAXBException, IOException {
        if (config == null) {
            config = getBuiltInConfigs().get(0);
        }
        if (config.getSource() == Source.SYSTEM) {
            File f = new File(FileUtil.toFile(getConfigDir()), config.getIdentifier());
            return readConfig(f.getAbsolutePath());
        } else {
            return readConfig(getClass().getResourceAsStream(BUILD_IN_DIRECTORY
                    + config.getIdentifier()));
        }
    }

    public ConfigDescriptor writeSystemConfig(CSVConverterConfig config, String filename) throws JAXBException, IOException {
        if (filename == null) {
            filename = UUID.randomUUID().toString() + ".xml";
        }
        File targetFile = new File(FileUtil.toFile(getConfigDir()), filename);
        writeConfig(config, targetFile.getAbsolutePath());
        return new ConfigDescriptor(Source.SYSTEM, config.getName(), filename);
    }

    public void deleteSystemConfig(String filename) throws IOException {
        if (filename == null) {
            filename = UUID.randomUUID().toString() + ".xml";
        }
        File targetFile = new File(FileUtil.toFile(getConfigDir()), filename);
        targetFile.delete();
    }

    public void writeConfig(CSVConverterConfig config, String filename) throws JAXBException {
        Marshaller m = getCtx().createMarshaller();
        m.setProperty("jaxb.formatted.output", true);
        m.marshal(config, new File(filename));
    }

    public ExporterConfig readExporterConfig(String filename) throws JAXBException {
        Unmarshaller m = getCtx().createUnmarshaller();
        ExporterConfig ec = (ExporterConfig) m.unmarshal(new File(filename));
        for (ExporterConfigTable ect : ec.getTableConfig()) {
            ect.setConfig(ec);
            for (ExporterConfigColumn ecpc : ect.getColumns()) {
                ecpc.setConfig(ect);
            }
        }
        return ec;
    }

    public void writeExporterConfig(ExporterConfig config, String filename) throws JAXBException {
        Marshaller m = getCtx().createMarshaller();
        m.setProperty("jaxb.formatted.output", true);
        m.marshal(config, new File(filename));;
    }

    public FileObject getConfigDir() throws IOException {
        FileObject historyRootDir = getConfigRoot().getFileObject(CONFIG_DIRECTORY);
        if (historyRootDir == null) {
            historyRootDir = FileUtil.createFolder(getConfigRoot(), CONFIG_DIRECTORY);
        }
        return historyRootDir;
    }

    public List<ConfigDescriptor> listSystemConfigs() {
        try {
            List<ConfigDescriptor> result = new ArrayList<>();
            for (FileObject fo : getConfigDir().getChildren()) {
                if (fo.isData() && fo.getExt().equals("xml")) {
                    try {
                        CSVConverterConfig ccc = readConfig(FileUtil.toFile(fo).getAbsolutePath());
                        result.add(new ConfigDescriptor(Source.SYSTEM, ccc.getName(), fo.getNameExt()));
                    } catch (JAXBException ex) {
                        if (ex.getCause() instanceof java.io.FileNotFoundException) {
                            LOG.log(Level.FINE, "Failed to read config file", ex);
                        } else {
                            LOG.log(Level.INFO, "Failed to read config file", ex);
                        }
                    }
                }
            }
            result.addAll(getBuiltInConfigs());
            Collections.sort(result);
            return result;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private final List<ConfigDescriptor> builtInConfig = new ArrayList<>();

    {
        String[] builtins = new String[]{"default.xml"};
        for (String builtin : builtins) {
            try {
                CSVConverterConfig ccc = readConfig(getClass().getResourceAsStream(BUILD_IN_DIRECTORY
                        + builtin));
                builtInConfig.add(new ConfigDescriptor(Source.BUILTIN, ccc.getName(), builtin));
            } catch (JAXBException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public CSVConverterConfig getDefaultConfig() {
        try {
            return readSystemConfig(builtInConfig.get(0));
        } catch (JAXBException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private List<ConfigDescriptor> getBuiltInConfigs() {
        return Collections.unmodifiableList(builtInConfig);
    }
}
