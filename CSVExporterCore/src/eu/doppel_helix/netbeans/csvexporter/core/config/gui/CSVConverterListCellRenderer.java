

package eu.doppel_helix.netbeans.csvexporter.core.config.gui;

import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverter;
import java.awt.Component;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class CSVConverterListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value instanceof CSVConverter) {
            try {
                value = Introspector
                        .getBeanInfo(value.getClass())
                        .getBeanDescriptor()
                        .getDisplayName();
            } catch (IntrospectionException ex) {
                throw new RuntimeException(ex);
            }
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); 
    }
    
}
