
package eu.doppel_helix.netbeans.csvexporter.core.config.gui;

import eu.doppel_helix.netbeans.csvexporter.core.config.SeparatorPreset;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public class SeparatorPresetCBRenderer extends BasicComboBoxRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value instanceof SeparatorPreset) {
            value = ((SeparatorPreset) value).getTitle();
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }

}
