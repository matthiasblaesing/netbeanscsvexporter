package eu.doppel_helix.netbeans.csvexporter.core.util;

import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverter;
import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;

public class ConverterPropertyEditor extends PropertyEditorSupport {
    private final List<String> names = new ArrayList<>();
    private final List<String> classNames = new ArrayList<>();

    public ConverterPropertyEditor(int sqlType) {
        for(CSVConverter c: CSVConverter.enumerateConverters()) {
            if (c.isSupportedType(sqlType)) {
                classNames.add(c.getClass().getName());
                names.add(c.getName());
            }
        }
    }

    @Override
    public String[] getTags() {
        return names.toArray(new String[names.size()]);
    }

    @Override
    public String getAsText() {
        String value = (String) getValue();
        for(int i = 0; i < classNames.size(); i++) {
            if(classNames.get(i).equals(value)) {
                return names.get(i);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text.length() > 0) {
            for (int i = 0; i < names.size(); i++) {
                if (names.get(i).equals(text)) {
                    setValue(classNames.get(i));
                }
            }
        } else {
            setValue(null);
        }
    }

}
