package eu.doppel_helix.netbeans.csvexporter.core.converter.impl;

import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverterBeanInfo;
import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

public class CharacterStreamConverterBeanInfo extends CSVConverterBeanInfo {

    public CharacterStreamConverterBeanInfo() {
        super(CharacterStreamConverter.class);
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = super.getBeanDescriptor();
        bd.setDisplayName("Character-Stream Converter");
        return bd;
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] pds = super.getPropertyDescriptors();
        for (PropertyDescriptor pd : pds) {
            if (pd.getName().equals("externalizeLobs")) {
                pd.setDisplayName("Externalize LOBs");
            } else if (pd.getName().equals("externalizationFormat")) {
                pd.setDisplayName("LOB Externalization Format");
            } else if (pd.getName().equals("charset")) {
                pd.setDisplayName("Characterset for Export");
            } else if (pd.getName().equals("namePrefix")) {
                pd.setDisplayName("Prefix for LOB-File");
                pd.setHidden(true);
            }
        }
        return pds;
    }
}
