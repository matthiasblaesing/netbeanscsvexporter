
package eu.doppel_helix.netbeans.csvexporter.core.validator;

import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfig;
import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfigTable;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ExportValidator {
    private static ExportValidator INSTANCE;
    
    public static synchronized ExportValidator getInstance()  {
        if(INSTANCE == null) {
            INSTANCE = new ExportValidator();
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
            if(tablePath.exists() && (! tablePath.canWrite())) {
                errors.add(String.format("File '%s' exists and is not writable.", tablePath.toString()));
            }
            if(! tablePath.getParentFile().canWrite()) {
                errors.add(String.format("Directory '%s' needs to be writable.", tablePath.getParentFile().toString()));
            }
        }
    }
}
