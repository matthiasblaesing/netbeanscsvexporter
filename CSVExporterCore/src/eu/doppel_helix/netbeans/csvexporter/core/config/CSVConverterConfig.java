package eu.doppel_helix.netbeans.csvexporter.core.config;


import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CSVConverterConfig {
    private String name = "";
    private char seperator = ',';
    private char quoteChar = '"';
    private char escapeChar = '"';
    private LineEnding lineEnd = LineEnding.LF;
    private boolean includeColumnTitle = true;
    private boolean alwaysQuote = false;
    private String basePath = null;
    private String charset = "UTF-8";
    private Map<Integer, CSVConverterConfigType> converters = new HashMap<>();
    
    public CSVConverterConfig() {
    }

    /**
     * Clone-Constructor - creates a copy of the supplied CSVConverterConfig.
     * 
     * @param ccc 
     */
    public CSVConverterConfig(CSVConverterConfig ccc) {
        this.name = ccc.name;
        this.seperator = ccc.seperator;
        this.quoteChar = ccc.quoteChar;
        this.lineEnd = ccc.lineEnd;
        this.includeColumnTitle = ccc.includeColumnTitle;
        this.alwaysQuote = ccc.alwaysQuote;
        this.basePath = ccc.basePath;
        this.charset = ccc.charset;
        for(Entry<Integer,CSVConverterConfigType> converter: ccc.converters.entrySet()) {
            this.converters.put(converter.getKey(), new CSVConverterConfigType(converter.getValue()));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public char getSeperator() {
        return seperator;
    }

    public void setSeperator(char seperator) {
        this.seperator = seperator;
    }

    public char getQuoteChar() {
        return quoteChar;
    }
    
    public void setQuoteChar(char quoteChar) {
        this.quoteChar = quoteChar;
    }

    public char getEscapeChar() {
        return escapeChar;
    }

    public void setEscapeChar(char escapeChar) {
        this.escapeChar = escapeChar;
    }

    public LineEnding getLineEnd() {
        return lineEnd;
    }

    public void setLineEnd(LineEnding lineEnd) {
        this.lineEnd = lineEnd;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public boolean isAlwaysQuote() {
        return alwaysQuote;
    }

    public void setAlwaysQuote(boolean alwaysQuote) {
        this.alwaysQuote = alwaysQuote;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public boolean isIncludeColumnTitle() {
        return includeColumnTitle;
    }

    public void setIncludeColumnTitle(boolean includeColumnTitle) {
        this.includeColumnTitle = includeColumnTitle;
    }

    public Map<Integer, CSVConverterConfigType> getConverters() {
        return converters;
    }

    public void setConverters(Map<Integer, CSVConverterConfigType> converts) {
        this.converters = converts;
    }
    
    public CSVConverterConfigType getConverterConfig(int sqlType) {
        CSVConverterConfigType result = converters.get(sqlType);
        if(result == null) {
            result = new CSVConverterConfigType( this, sqlType, "SkipConverter");
            converters.put(sqlType, result);
        }
        return result;
    }
}
