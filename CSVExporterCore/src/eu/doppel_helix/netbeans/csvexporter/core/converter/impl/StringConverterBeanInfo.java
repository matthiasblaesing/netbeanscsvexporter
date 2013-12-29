package eu.doppel_helix.netbeans.csvexporter.core.converter.impl;

import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverterBeanInfo;
import java.beans.BeanDescriptor;

public class StringConverterBeanInfo extends CSVConverterBeanInfo {

    public StringConverterBeanInfo() {
        super(StringConverter.class);
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = super.getBeanDescriptor();
        bd.setDisplayName("String Converter");
        return bd;
    }
}
