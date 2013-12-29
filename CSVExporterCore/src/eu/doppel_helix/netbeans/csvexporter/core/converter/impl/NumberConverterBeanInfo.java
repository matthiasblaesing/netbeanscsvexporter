package eu.doppel_helix.netbeans.csvexporter.core.converter.impl;

import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverterBeanInfo;
import java.beans.BeanDescriptor;

public class NumberConverterBeanInfo extends CSVConverterBeanInfo {

    public NumberConverterBeanInfo() {
        super(NumberConverter.class);
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = super.getBeanDescriptor();
        bd.setDisplayName("Number Converter");
        return bd;
    }

}
