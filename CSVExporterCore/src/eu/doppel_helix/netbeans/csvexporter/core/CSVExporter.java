package eu.doppel_helix.netbeans.csvexporter.core;

import eu.doppel_helix.netbeans.csvexporter.core.config.CSVConverterConfig;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import eu.doppel_helix.netbeans.csvexporter.core.config.CSVConverterConfigType;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.*;
import java.util.Arrays;
import static eu.doppel_helix.netbeans.csvexporter.core.util.JDBC.*;
import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfig;
import eu.doppel_helix.netbeans.csvexporter.core.config.ExporterConfigTable;
import eu.doppel_helix.netbeans.csvexporter.core.converter.CSVConverterFactory;
import eu.doppel_helix.netbeans.csvexporter.core.converter.impl.BooleanConverter;
import eu.doppel_helix.netbeans.csvexporter.core.converter.impl.ByteArrayConverter;
import eu.doppel_helix.netbeans.csvexporter.core.converter.impl.ByteStreamConverter;
import eu.doppel_helix.netbeans.csvexporter.core.converter.impl.CharacterStreamConverter;
import eu.doppel_helix.netbeans.csvexporter.core.converter.impl.NumberConverter;
import eu.doppel_helix.netbeans.csvexporter.core.converter.impl.SimpleDateTimeConverter;
import eu.doppel_helix.netbeans.csvexporter.core.converter.impl.StringConverter;
import eu.doppel_helix.netbeans.csvexporter.core.converter.impl.XMLConverter;
import java.util.Map;

public class CSVExporter {

    
    private Connection c;

    

//    private static Map<String, String> parametersFromConverter(CSVConverter convert) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
//        HashMap<String, String> params = new HashMap<String, String>(BeanUtils.describe(convert));
//        
//        
//        params.remove("class");
//        params.remove("supportedTypes");
//        params.remove("namePrefix");
//        params.put("converter", convert.getClass().getName());
//        return params;
//    }

    private void run() throws Exception {        
//        c = DriverManager.getConnection(
//                "jdbc:h2:file:/home/matthias/Kolping/jameica/jverein/h2db/jverein",
//                "jverein",
//                "jverein");
//
//        String tableName = "MITGLIED";
//
//        CSVConverterFactory fact = new CSVConverterFactory();
//        
//        CSVConverterConfig ccc = fact.readConfig("/home/matthias/test.xml");
//
//        ccc.setBasePath("/home/matthias/t");
//
//        Logger.getLogger("nampf").info("START");
//
//        ExporterConfig ec = prepareFullExport(ccc);
//
//        doExport(ec);
//
//        c.close();
        
        CSVConverterConfig ccc = new CSVConverterConfig();
        Map<Integer,CSVConverterConfigType> converters = ccc.getConverters();
        converters.put(Types.CLOB, new CSVConverterConfigType( ccc, Types.CLOB, CharacterStreamConverter.class.getName()));
        converters.put(Types.NCLOB, new CSVConverterConfigType( ccc, Types.NCLOB, CharacterStreamConverter.class.getName()));
        converters.put(Types.BOOLEAN, new CSVConverterConfigType( ccc, Types.BOOLEAN, BooleanConverter.class.getName())
            .withParameter("booleanFalseString", Boolean.FALSE.toString())
            .withParameter("booleanTrueString", Boolean.TRUE.toString()));
        converters.put(Types.TIME, new CSVConverterConfigType( ccc, Types.TIME, SimpleDateTimeConverter.class.getName())
            .withParameter("format", "HH:mm:ss"));
        converters.put(Types.DATE, new CSVConverterConfigType( ccc, Types.DATE, SimpleDateTimeConverter.class.getName())
            .withParameter("format", "yyyy-MM-dd"));
        converters.put(Types.TIMESTAMP, new CSVConverterConfigType( ccc, Types.TIMESTAMP, SimpleDateTimeConverter.class.getName())
            .withParameter("format", "yyyy-MM-dd'T'HH:mm:ss"));
        converters.put(Types.VARBINARY, new CSVConverterConfigType( ccc, Types.VARBINARY, ByteArrayConverter.class.getName()));
        converters.put(Types.BINARY, new CSVConverterConfigType( ccc, Types.BINARY, ByteArrayConverter.class.getName()));
        converters.put(Types.LONGVARBINARY, new CSVConverterConfigType( ccc, Types.LONGVARBINARY, ByteStreamConverter.class.getName()));
        converters.put(Types.BLOB, new CSVConverterConfigType( ccc, Types.BLOB, ByteStreamConverter.class.getName()));
        converters.put(Types.CHAR, new CSVConverterConfigType( ccc, Types.CHAR, StringConverter.class.getName()));
        converters.put(Types.VARCHAR, new CSVConverterConfigType( ccc, Types.VARCHAR, StringConverter.class.getName()));
        converters.put(Types.LONGVARCHAR, new CSVConverterConfigType( ccc, Types.LONGVARCHAR, StringConverter.class.getName()));
        converters.put(Types.NCHAR, new CSVConverterConfigType( ccc, Types.NCHAR, StringConverter.class.getName()));
        converters.put(Types.NVARCHAR, new CSVConverterConfigType( ccc, Types.NVARCHAR, StringConverter.class.getName()));
        converters.put(Types.LONGNVARCHAR, new CSVConverterConfigType( ccc, Types.LONGNVARCHAR, StringConverter.class.getName()));
        converters.put(Types.BIT, new CSVConverterConfigType( ccc, Types.BIT, NumberConverter.class.getName()));
        converters.put(Types.TINYINT, new CSVConverterConfigType( ccc, Types.TINYINT, NumberConverter.class.getName()));
        converters.put(Types.SMALLINT, new CSVConverterConfigType( ccc, Types.SMALLINT, NumberConverter.class.getName()));
        converters.put(Types.INTEGER, new CSVConverterConfigType( ccc, Types.INTEGER, NumberConverter.class.getName()));
        converters.put(Types.BIGINT, new CSVConverterConfigType( ccc, Types.BIGINT, NumberConverter.class.getName()));
        converters.put(Types.NUMERIC, new CSVConverterConfigType( ccc, Types.NUMERIC, NumberConverter.class.getName()));
        converters.put(Types.DECIMAL, new CSVConverterConfigType( ccc, Types.DECIMAL, NumberConverter.class.getName()));
        converters.put(Types.REAL, new CSVConverterConfigType( ccc, Types.REAL, NumberConverter.class.getName()));
        converters.put(Types.DOUBLE, new CSVConverterConfigType( ccc, Types.DOUBLE, NumberConverter.class.getName()));
        converters.put(Types.FLOAT, new CSVConverterConfigType( ccc, Types.FLOAT, NumberConverter.class.getName()));
        converters.put(Types.SQLXML, new CSVConverterConfigType( ccc, Types.SQLXML, XMLConverter.class.getName()));
        CSVConverterFactory.getInstance().writeConfig(ccc, "output.xml");
    }

    public static void main(String[] args) throws Exception {
//        for(Handler h: Logger.getLogger("").getHandlers()) {
//            h.setLevel(Level.FINE);
//        }
//        Logger.getLogger("org.apache.commons.beanutils").setLevel(Level.ALL);
        new CSVExporter().run();
    }

    private ExporterConfig prepareFullExport(CSVConverterConfig ccc) throws SQLException {
        ExporterConfig ec = new ExporterConfig(ccc);
        ResultSet rs = c.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
        while (rs.next()) {
            prepareTableExport(ec, rs.getString("TABLE_NAME"));
        }
        rs.close();
        return ec;
    }

    private ExporterConfigTable prepareTableExport(ExporterConfig ec, String table) throws SQLException {
        ExporterConfigTable ect = ec.addTableConfig(null, null, table);
        ResultSet rs = c.getMetaData().getColumns(null, null, table, "%");
        while (rs.next()) {
            ect.addColumn(rs.getString("COLUMN_NAME"),
                    rs.getString("COLUMN_NAME"),
                    rs.getInt("DATA_TYPE"));
        }
        rs.close();
        return ect;
    }

    private void doExport(ExporterConfig ec) throws Exception {
        Statement s = c.createStatement();

        for (ExporterConfigTable ect : ec.getTableConfig()) {
            System.out.println("Exporting: " + ect.getTable());
            ResultSet rs = s.executeQuery("SELECT * FROM " + ect.getTable());

            CSVWriter out = new CSVWriter(new OutputStreamWriter(
                    new BufferedOutputStream(
                            new FileOutputStream(new File(ec.getBaseConfig().getBasePath(),
                                            ect.getFilename() + ".csv"
                                    ))), ec.getBaseConfig().getCharset()),
                    ec.getBaseConfig().getSeperator(),
                    ec.getBaseConfig().getQuoteChar(),
                    ec.getBaseConfig().getEscapeChar(),
                    ec.getBaseConfig().getLineEnd().getEncoding()
            );

            long start = System.currentTimeMillis();
            int rowCount = 0;
            if (ec.getBaseConfig().isIncludeColumnTitle()) {
                String[] header = new String[ect.getColumns().size()];
                for (int i = 0; i < ect.getColumns().size(); i++) {
                    header[i] = ect.getColumns().get(i).getTitle();
                }
                out.writeNext(header, ec.getBaseConfig().isAlwaysQuote());
            }
            while (rs.next()) {
                String[] row = new String[ect.getColumns().size()];
                for (int i = 0; i < ect.getColumns().size(); i++) {
                    row[i] = ect.getColumns().get(i).getCSVConverter().getValue(rs, i
                            + 1);
                }
                out.writeNext(row, ec.getBaseConfig().isAlwaysQuote());
                rowCount++;
            }
            long end = System.currentTimeMillis();
            long diff = end - start;
            if (diff == 0) {
                diff = 1;
            }
            System.out.println(String.format("%d rows in %d ms: %d rows/s", rowCount, end
                    - start, (rowCount * 1000) / diff));
            out.close();
            rs.close();
        }
        s.close();
    }

//    private void exportTable(CSVConverterConfig config, String tableName) throws Exception {        
//        Statement s = c.createStatement();
//       
////        s.setFetchSize(Integer.MIN_VALUE);
//        
//        ResultSet rs = s.executeQuery("SELECT * FROM " + tableName);
//
//        ExporterConfig ec = new ExporterConfig(config);
//        ExporterConfigTable ect = ec.addTableConfig(tableName);
//        
//        ResultSetMetaData rsmd = rs.getMetaData();
//        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
//            ExporterConfigColumn ecpc = ect.addColumn(
//                    rsmd.getColumnLabel(i),
//                    rsmd.getColumnLabel(i),
//                    rsmd.getColumnType(i));
//        }
//        
//        JAXBContext ctx = JAXBContext.newInstance(ExporterConfig.class);
//        Marshaller m = ctx.createMarshaller();
//        m.setProperty("jaxb.formatted.output", true);
//        m.marshal(ec, new File("/home/matthias/test2.xml"));
//        
//        if(true)  {
//            return;
//        }
//        
//        CSVWriter out = new CSVWriter(new OutputStreamWriter(
//                new BufferedOutputStream(
//                new FileOutputStream(new File(config.getBasePath(), 
//                        tableName + ".csv"
//                ))), ec.getBaseConfig().getDefaultEncoding()),
//                ec.getBaseConfig().getSeperator(),
//                ec.getBaseConfig().getQuoteChar(),
//                ec.getBaseConfig().getEscapeChar(),
//                ec.getBaseConfig().getLineEnd().getEncoding()
//                );
//        
//        long start = System.currentTimeMillis();
//        int rowCount = 0;
//        if (config.isIncludeColumnTitle()) {
//            String[] header = new String[ect.getColumns().size()];
//            for (int i = 0; i < ect.getColumns().size(); i++) {
//                header[i] = ect.getColumns().get(i).getTitle();
//            }
//            out.writeNext(header, ec.getBaseConfig().isAlwaysQuote());
//        }
//        while (rs.next()) {
//            String[] row = new String[ect.getColumns().size()];
//            for (int i = 0; i < ect.getColumns().size(); i++) {
//                row[i] = ect.getColumns().get(i).getCSVConverter().getValue(rs, i + 1);
//            }
//            out.writeNext(row, config.isAlwaysQuote());
//            rowCount++;
//            if(rowCount >= 10000) {
//                break;
//            }
//        }
//        long end = System.currentTimeMillis();
//        System.out.println(String.format("%d rows in %d ms: %d rows/s", rowCount, end
//                - start, (rowCount * 1000) / (end - start)));
//        out.close();
//        rs.close();
//        s.close();
//    }




//    private String buildInsertSQL(String targetTable, int columnCount) {
//        StringBuilder sb = new StringBuilder("INSERT INTO ");
//        sb.append(targetTable);
//        sb.append(" VALUES (");
//        for (int i = 0; i < columnCount; i++) {
//            if (i != 0) {
//                sb.append(", ");
//            }
//            sb.append(" ?");
//        }
//        sb.append(")");
//        return sb.toString();
//    }

//    private void importTable(CSVConverterConfig config, String tableName) throws Exception {
//        String targetTable = getDBTableName(tableName);
//        ExporterConfig ec = new ExporterConfig(config);
//        ExporterConfigTable ect = getColumns(ec, targetTable);
//        String insertSQL = buildInsertSQL(targetTable, ect.getColumns().size());
//
//        Statement s = c.createStatement();
//        s.execute("DELETE FROM " + targetTable);
//        s.close();
//
//        PreparedStatement insert = c.prepareStatement(insertSQL);
//
//        CSVReader in = new CSVReader(new InputStreamReader(
//                new BufferedInputStream(
//                        new FileInputStream(new File(ec.getBaseConfig().getBasePath(),
//                                        "tableName.csv"))),
//                ec.getBaseConfig().getCharset()),
//                ec.getBaseConfig().getSeperator(),
//                ec.getBaseConfig().getQuoteChar());
//        
//        System.out.println(Arrays.asList(in.readNext()));
//
//        long start = System.currentTimeMillis();
//
//        String[] row;
//        int rowCount = 0;
//        c.setAutoCommit(false);
//        while ((row = in.readNext()) != null) {
//            for (int i = 0; i < row.length; i++) {
//                ect.getColumns().get(i).getCSVConverter().setValue(insert, i + 1, row[i]);
//            }
//            rowCount++;
//            insert.execute();
//        }
//        c.commit();
//        c.rollback();
//
//        long end = System.currentTimeMillis();
//        System.out.println(String.format("%d rows in %d ms: %d rows/s", rowCount, end
//                - start, (rowCount * 1000) / (end - start)));
//    }
}
