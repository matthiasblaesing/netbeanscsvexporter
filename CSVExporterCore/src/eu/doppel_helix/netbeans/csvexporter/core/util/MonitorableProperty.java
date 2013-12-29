

package eu.doppel_helix.netbeans.csvexporter.core.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import org.openide.nodes.Node.Property;


public class MonitorableProperty extends Property {
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    Property delegate;

    public MonitorableProperty(Property delegate) {
        super(delegate.getValueType());
        this.delegate = delegate;
    }

    @Override
    public Class getValueType() {
        return delegate.getValueType();
    }

    @Override
    public boolean canRead() {
        return delegate.canRead();
    }

    @Override
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return delegate.getValue();
    }

    @Override
    public boolean canWrite() {
        return delegate.canWrite();
    }

    @Override
    public void setValue(Object val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object oldValue = delegate.getValue();
        delegate.setValue(val);
        pcs.firePropertyChange(null, oldValue, val);
    }

    @Override
    public boolean supportsDefaultValue() {
        return delegate.supportsDefaultValue();
    }

    @Override
    public void restoreDefaultValue() throws IllegalAccessException, InvocationTargetException {
        delegate.restoreDefaultValue();
    }

    @Override
    public boolean isDefaultValue() {
        return delegate.isDefaultValue();
    }

    @Override
    public PropertyEditor getPropertyEditor() {
        return delegate.getPropertyEditor();
    }

    @Override
    public boolean equals(Object property) {
        return delegate.equals(property);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public String getHtmlDisplayName() {
        return delegate.getHtmlDisplayName();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public void setName(String name) {
        delegate.setName(name);
    }

    @Override
    public String getDisplayName() {
        return delegate.getDisplayName();
    }

    @Override
    public void setDisplayName(String displayName) {
        delegate.setDisplayName(displayName);
    }

    @Override
    public boolean isExpert() {
        return delegate.isExpert();
    }

    @Override
    public void setExpert(boolean expert) {
        delegate.setExpert(expert);
    }

    @Override
    public boolean isHidden() {
        return delegate.isHidden();
    }

    @Override
    public void setHidden(boolean hidden) {
        delegate.setHidden(hidden);
    }

    @Override
    public boolean isPreferred() {
        return delegate.isPreferred();
    }

    @Override
    public void setPreferred(boolean preferred) {
        delegate.setPreferred(preferred);
    }

    @Override
    public String getShortDescription() {
        return delegate.getShortDescription();
    }

    @Override
    public void setShortDescription(String text) {
        delegate.setShortDescription(text);
    }

    @Override
    public void setValue(String attributeName, Object value) {
        delegate.setValue(attributeName, value);
    }

    @Override
    public Object getValue(String attributeName) {
        return delegate.getValue(attributeName);
    }

    @Override
    public Enumeration<String> attributeNames() {
        return delegate.attributeNames();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
