package eu.doppel_helix.netbeans.csvexporter.core.config.gui;

import eu.doppel_helix.netbeans.csvexporter.core.converter.ConfigDescriptor;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;

public class ConfigDescriptorRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String tooltip = null;
        if (value instanceof ConfigDescriptor) {
            ConfigDescriptor config = (ConfigDescriptor) value;
            value = config.getName();
            tooltip = config.getIdentifier();
        }
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (c instanceof JComponent) {
            ((JComponent) c).setToolTipText(tooltip);
        }
        return c;
    }

}
