
package eu.doppel_helix.netbeans.csvexporter.core.converter.impl;

import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverterBeanInfo;
import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

public class BooleanConverterBeanInfo  extends CSVConverterBeanInfo {

    public BooleanConverterBeanInfo() {
        super(BooleanConverter.class);
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = super.getBeanDescriptor(); 
        bd.setDisplayName("Boolean Converter");
        return bd;
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] pds = super.getPropertyDescriptors();
        for(PropertyDescriptor pd: pds) {
            if(pd.getName().equals("booleanFalseString")) {
                pd.setDisplayName("Boolean 'false' string");
            } else if (pd.getName().equals("booleanTrueString")) {
                pd.setDisplayName("Boolean 'true' string");
            }
        }
        return pds;
    }

}
