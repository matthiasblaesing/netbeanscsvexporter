package eu.doppel_helix.netbeans.csvexporter.core.converter.impl;

import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverterBeanInfo;
import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

public class SkipConverterBeanInfo extends CSVConverterBeanInfo {

    public SkipConverterBeanInfo() {
        super(SkipConverter.class);
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = super.getBeanDescriptor();
        bd.setDisplayName("Skip column");
        return bd;
    }
}
