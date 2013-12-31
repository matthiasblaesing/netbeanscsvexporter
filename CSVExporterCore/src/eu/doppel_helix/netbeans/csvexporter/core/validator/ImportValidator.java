
package eu.doppel_helix.netbeans.csvexporter.core.validator;

import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfig;
import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfigTable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImportValidator {
    private static ImportValidator INSTANCE;
    
    public static synchronized ImportValidator getInstance()  {
        if(INSTANCE == null) {
            INSTANCE = new ImportValidator();
        }
        return INSTANCE;
    }
    
    public List<String> validate(ExporterConfig ec) {
        List<String> errors = new ArrayList<>();
        validatePaths(errors, ec);
        return errors;
    }
    
    private void validatePaths(List<String> errors, ExporterConfig ec) {
        File basePath = new File(ec.getBaseConfig().getBasePath());
        for (ExporterConfigTable ect : ec.getTableConfig()) {
            File tablePath = new File(basePath, ect.getFilename());
            if(! tablePath.canRead()) {
                errors.add(String.format("File '%s' exists or can not be read.", tablePath.toString()));
            }
        }
    }
}
