package eu.doppel_helix.netbeans.csvexporter.core.config;

public enum SeparatorPreset {

    TAB('\t', "Tab"),
    COMMA(',', "Comma"),
    SEMICOLON(';', "Semicolon"),
    SPACE(' ', "Space"),
    PIPE('|', "Pipe"),
    OTHER(null, "Other");
    private final Character symbol;
    private final String title;

    private SeparatorPreset(Character symbol, String title) {
        this.symbol = symbol;
        this.title = title;
    }

    public char getSymbol() {
        return symbol;
    }

    public String getTitle() {
        return title;
    }
}
