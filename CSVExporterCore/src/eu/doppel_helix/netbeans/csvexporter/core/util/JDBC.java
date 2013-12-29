
package eu.doppel_helix.netbeans.csvexporter.core.util;

import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfig;
import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfigTable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.openide.util.Exceptions;

/**
 * Most methods provided here are to work around jdbc implementors insanity
 * 
 * @author matthias
 */
public class JDBC {
    private final static Map<String,Integer> sqlTypes;
    private final static Map<Integer,String> revSqlTypes;

    static {
        sqlTypes = new TreeMap<>();
        revSqlTypes = new HashMap<>();
        for(Field f: java.sql.Types.class.getFields()) {
            try {
                Object value = f.get(null);
                if(value instanceof Integer) {
                    sqlTypes.put( f.getName(), (Integer) value);
                    revSqlTypes.put( (Integer) value, f.getName() );
                } else {
                    sqlTypes.put( f.getName(), Integer.valueOf((int) value));
                    revSqlTypes.put( Integer.valueOf((int) value), f.getName() );
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }        
    }
    
    public static Map<String, Integer> getSqlTypes() {
        return Collections.unmodifiableMap(sqlTypes);
    }

    public static Map<Integer, String> getRevSqlTypes() {
        return Collections.unmodifiableMap(revSqlTypes);
    }
    
    /**
     * Retrieve catalog name from connection if available
     * 
     * @return catalog name or null if not available
     **/
    public static String getCatalog(Connection c) {
        try {
            return c.getCatalog();
        } catch (RuntimeException | AbstractMethodError | SQLException ex) {
            return null;
        }
    }

    /**
     * Retrieve schema name from connection if available
     * 
     * @return schema name or null if not available
     **/
    public static String getSchema(Connection c) {
        try {
            return c.getSchema();
        } catch (RuntimeException | AbstractMethodError | SQLException ex) {
            return null;
        }
    }
    
    /**
     * Try to find correct table for supplied input.
     * 
     * Reads metadata and tries to find matching table
     * 
     * @param input
     * @return
     * @throws Exception 
     */
    public static String getDBTableName(Connection c, String input) throws Exception {
        ResultSet rs = c.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
        String inExactMatch = null;
        while (rs.next()) {
            String tableName = rs.getString(3);
            if (input.equals(tableName)) {
                return input;
            } else if (input.equalsIgnoreCase(tableName) && inExactMatch == null) {
                inExactMatch = tableName;
            }
        }
        if (inExactMatch == null) {
            return input;
        } else {
            return inExactMatch;
        }
    }
    
    public static ExporterConfigTable getColumnsForExporter(Connection c, ExporterConfig config, String catalog, String schema, String table) throws SQLException {
        ExporterConfigTable ect = config.addTableConfig(catalog, schema, table);

        ResultSet md = c.getMetaData().getColumns(
                catalog,
                schema,
                table,
                "%");

        while (md.next()) {
            ect.addColumn(md.getString(4), md.getString(4), md.getInt(5));
        }

        md.close();

        return ect;
    }

    public static String quoteIdentifier(Connection c, String identifier) {
        try {
            DatabaseMetaData md = c.getMetaData();
            String quotetString = md.getIdentifierQuoteString();
            if(quotetString != null && (! quotetString.equals(" "))) {
                return quotetString + identifier + quotetString;
            } else {
                return identifier;
            }
        } catch (SQLException ex) {
            return identifier;
        }
    }
}
