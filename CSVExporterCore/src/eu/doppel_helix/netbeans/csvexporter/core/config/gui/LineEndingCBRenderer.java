
package eu.doppel_helix.netbeans.csvexporter.core.config.gui;

import eu.doppel_helix.netbeans.csvexporter.core.config.LineEnding;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class LineEndingCBRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value instanceof LineEnding) {
            value = ((LineEnding) value).getTitle();
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }

}
