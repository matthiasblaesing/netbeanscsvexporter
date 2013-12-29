
package eu.doppel_helix.netbeans.csvexporter.core.converter;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class CSVConverterBeanInfo extends SimpleBeanInfo {
    private Class targetClass;

    public CSVConverterBeanInfo(Class targetClass) {
        this.targetClass = targetClass;
    }
    
    @Override
    public PropertyDescriptor[] getPropertyDescriptors()  {
        try {
            BeanInfo bi = Introspector.getBeanInfo(targetClass, Introspector.IGNORE_ALL_BEANINFO);
            PropertyDescriptor[] pds = bi.getPropertyDescriptors();
            for(PropertyDescriptor pd: pds) { 
                if(pd.getName().equals("sqlType")) {
                    pd.setWriteMethod(null);
                    pd.setExpert(true);
                    pd.setDisplayName("SQL Type");
                } else if (pd.getName().equals("supportedTypes")) {
                    pd.setHidden(true);
                } else if (pd.getName().equals("class")) {
                    pd.setHidden(true);
                } else if (pd.getName().equals("CSVExporterConfig")) {
                    pd.setHidden(true);
                } else if (pd.getName().equals("name")) {
                    pd.setDisplayName("Name");
                } else if (pd.getName().equals("properties")) {
                    pd.setDisplayName("Properties");
                    pd.setHidden(true);
                }
            }
            return pds;
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        try {
            BeanDescriptor bc = Introspector.getBeanInfo(targetClass, Introspector.IGNORE_ALL_BEANINFO).getBeanDescriptor();
            bc.setDisplayName(targetClass.getSimpleName());
            return bc;
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex);
        }
    }

    
}
