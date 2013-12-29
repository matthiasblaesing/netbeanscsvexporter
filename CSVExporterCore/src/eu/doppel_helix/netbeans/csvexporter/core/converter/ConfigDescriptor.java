
package eu.doppel_helix.netbeans.csvexporter.core.converter;

import java.util.Objects;

public class ConfigDescriptor implements Comparable<ConfigDescriptor>{
    public static enum Source {
        BUILTIN,
        SYSTEM
    }
    
    private Source source;
    private String name;
    private String identifier;

    public ConfigDescriptor() {
        this(Source.BUILTIN, "", "");
    }

    public ConfigDescriptor(Source source, String name, String identifier) {
        this.source = source;
        this.name = name;
        this.identifier = identifier;
    }

    public boolean isWriteable() {
        return ! (source == Source.BUILTIN);
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.source);
        hash = 37 * hash + Objects.hashCode(this.identifier);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConfigDescriptor other = (ConfigDescriptor) obj;
        if (this.source != other.source) {
            return false;
        }
        if (!Objects.equals(this.identifier, other.identifier)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(ConfigDescriptor o) {
        int writeable1 = isWriteable() ? 1 : 0;
        int writeable2 = o.isWriteable() ? 1 : 0;
        if(writeable1 != writeable2) {
            return writeable1 - writeable2;
        }
        return o.getName().compareToIgnoreCase(o.getName());
    }

    
    
}
