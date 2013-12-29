package eu.doppel_helix.netbeans.csvexporter.core.config.gui;

import eu.doppel_helix.netbeans.csvexporter.core.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class EntryTableModel<E, V> implements TableModel {

    private final Class<? extends E> keyClass;
    private final Class<? extends V> valueClass;
    private final List<Pair> backingList = new ArrayList<>();
    private final List<TableModelListener> tmls = new ArrayList<>();

    public EntryTableModel(Class<? extends E> keyClass, Class<? extends V> valueClass) {
        this.keyClass = keyClass;
        this.valueClass = valueClass;
    }

    @Override
    public int getRowCount() {
        return backingList.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Property";
            case 1:
                return "Value";
        }
        return "";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return keyClass;
            case 1:
                return valueClass;
        }
        return Object.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return backingList.get(rowIndex).getElement1();
            case 1:
                return backingList.get(rowIndex).getElement2();
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                backingList.get(rowIndex).setElement1((E) aValue);
            case 1:
                backingList.get(rowIndex).setElement2((V) aValue);
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        tmls.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        tmls.remove(l);
    }

    public void setMap(Map<? extends E,? extends V> input) {
        backingList.clear();
        for(Entry<? extends E, ? extends V> e: input.entrySet()) {
            backingList.add(new Pair(e.getKey(), e.getValue()));
        }
        TableModelEvent tme = new TableModelEvent(this);
        for(TableModelListener tml: tmls) {
            tml.tableChanged(tme);
        }
    }
    
    public Map<? super E,? super V> getMap() {
        Map<E,V> result = new HashMap<>();
        for(Pair p: backingList) {
            result.put( (E) p.getElement1(), (V) p.getElement2());
        }
        return result;
    }
}
