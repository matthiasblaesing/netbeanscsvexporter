package eu.doppel_helix.netbeans.csvexporter.core.converter.impl;

import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverterBeanInfo;
import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

public class SimpleDateTimeConverterBeanInfo extends CSVConverterBeanInfo {

    public SimpleDateTimeConverterBeanInfo() {
        super(SimpleDateTimeConverter.class);
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = super.getBeanDescriptor();
        bd.setDisplayName("Datetime Converter");
        return bd;
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] pds = super.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            if (pd.getName().equals("format")) {
                pd.setDisplayName("Formatting String");
                pd.setShortDescription("See documentation of java.text.SimpleDateFormat");
            }
        }
        return pds;
    }
}
