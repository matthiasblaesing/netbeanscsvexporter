package eu.doppel_helix.netbeans.csvexporter.core.converter.impl;

import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverterBeanInfo;
import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

public class ByteArrayConverterBeanInfo extends CSVConverterBeanInfo {

    public ByteArrayConverterBeanInfo() {
        super(ByteArrayConverter.class);
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = super.getBeanDescriptor();
        bd.setDisplayName("Byte-Array Converter");
        return bd;
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] pds = super.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            if (pd.getName().equals("format")) {
                pd.setDisplayName("Inline Binary Format");
            }
        }
        return pds;
    }
}
