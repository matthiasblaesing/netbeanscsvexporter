

package eu.doppel_helix.netbeans.csvexporter.core.config.gui;

import java.awt.Component;
import java.util.Map.Entry;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;


public class SQLTypeRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value instanceof Entry) {
            value = ((Entry) value).getKey();
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); 
    }
    
}
