
package eu.doppel_helix.netbeans.csvexporter.core.config;

public enum LineEnding {
    LF("Unix, Linux, Android, Mac OS X (LF)", "\n"),
    CR_LF("Windows, DOS, OS/2, CP/M (CR/LF)", "\r\n"),
    CR("Mac OS bis Version 9, Apple II, C64 (CR)", "\r");
    
    private final String encoding;
    private final String title;
    
    LineEnding(String title, String encoding) {
        this.encoding = encoding;
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getEncoding() {
        return encoding;
    }
}
