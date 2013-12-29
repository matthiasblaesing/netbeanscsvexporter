package eu.doppel_helix.netbeans.csvexporter.core.converter.impl;

import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverterBeanInfo;
import java.beans.BeanDescriptor;

public class XMLConverterBeanInfo extends CSVConverterBeanInfo {

    public XMLConverterBeanInfo() {
        super(XMLConverter.class);
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = super.getBeanDescriptor();
        bd.setDisplayName("XML Converter");
        return bd;
    }
}
