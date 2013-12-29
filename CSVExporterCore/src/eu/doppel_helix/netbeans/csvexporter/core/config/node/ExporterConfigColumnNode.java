package eu.doppel_helix.netbeans.csvexporter.core.config.node;

import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfigColumn;
import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverter;
import eu.doppel_helix.netbeans.csvexporter.core.util.ConverterPropertyEditor;
import eu.doppel_helix.netbeans.csvexporter.core.util.JDBC;
import eu.doppel_helix.netbeans.csvexporter.core.util.MonitorableProperty;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;

public class ExporterConfigColumnNode extends AbstractNode {
    private BeanNode backingBeanNode = null;
    private CSVConverter c = null;
    private final ExporterConfigColumn ecc;

    private PropertyChangeListener pcl = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(c != null) {
                ecc.getConverterConfig().setParameters(c.getProperties());
            }
        }
    };
    
    private void updateConverterProperties() {
        try {
            if(backingBeanNode != null) {
                PropertySet[] pss = backingBeanNode.getPropertySets();
                for(PropertySet ps : pss) {
                    for(Property p: ps.getProperties()) {
                        if(p instanceof MonitorableProperty) {
                            ((MonitorableProperty) p).removePropertyChangeListener(pcl);
                        }
                    }
                    getSheet().remove(ps.getName());
                }
            }
            String className = ecc.getConverterConfig().getConverter();
            c = (CSVConverter) Class.forName(className).newInstance();
            c.setProperties(ecc.getConverterConfig().getParameters());
            c.setSqlType(ecc.getSqlType());
            backingBeanNode = new BeanNode(c);
            for(PropertySet ps: backingBeanNode.getPropertySets()) {
                Sheet.Set ss = new Sheet.Set();
                ss.setName(ps.getName());
                ss.setDisplayName("Converter - " + ps.getDisplayName());
                for(Property p: ps.getProperties()) {
                    MonitorableProperty mp = new MonitorableProperty(p);
                    mp.addPropertyChangeListener(pcl);
                    ss.put(mp);
                }
                getSheet().put(ss);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    public ExporterConfigColumnNode(ExporterConfigColumn ecc) {
        super(Children.LEAF);
        this.ecc = ecc;
        setIconBaseWithExtension("eu/doppel_helix/netbeans/csvexporter/core/resources/column.gif");
        Sheet s = createSheet();
        Sheet.Set ss = new Sheet.Set();
        ss.setName("internal");
        ss.setDisplayName("Properties");
        ss.put(new PropertySupport.ReadOnly<String>("Name", String.class, "Name", "") {

            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                return ExporterConfigColumnNode.this.ecc.getName();
            }
        });
        ss.put(new PropertySupport.ReadOnly<Integer>("SQL Type", Integer.class, "SQL Type", "") {

            @Override
            public Integer getValue() throws IllegalAccessException, InvocationTargetException {
                return ExporterConfigColumnNode.this.ecc.getSqlType();
            }
        });
        ss.put(new PropertySupport.ReadOnly<String>("SQL Type (String)", String.class, "SQL Type (String)", "") {

            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                return JDBC.getRevSqlTypes().get(ExporterConfigColumnNode.this.ecc.getSqlType());
            }
        });
        ss.put(new PropertySupport.ReadWrite<String>("Title", String.class, "Title", "") {

            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                return ExporterConfigColumnNode.this.ecc.getTitle();
            }

            @Override
            public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                ExporterConfigColumnNode.this.ecc.setTitle(val);
            }
        });
        ss.put(new PropertySupport.ReadWrite<String>("Converter", String.class, "Converter", "") {

            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                return ExporterConfigColumnNode.this.ecc.getConverterConfig().getConverter();
            }

            @Override
            public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
                ExporterConfigColumnNode.this.ecc.getConverterConfig().setConverter(val);
                updateConverterProperties();
            }

            @Override
            public PropertyEditor getPropertyEditor() {
                return new ConverterPropertyEditor(ExporterConfigColumnNode.this.ecc.getSqlType());
            }
        });
        s.put(ss);
        setSheet(s);
        updateConverterProperties();
    }

    @Override
    public String getName() {
        return ecc.getName();
    }

}
