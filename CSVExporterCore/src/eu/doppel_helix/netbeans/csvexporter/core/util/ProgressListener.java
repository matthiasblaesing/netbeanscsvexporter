
package eu.doppel_helix.netbeans.csvexporter.core.util;

public interface ProgressListener {
    void setRunning(boolean running);
    void setCurrent(int currentCount);
    void setTotal(Integer totalCount);
}
