package ru.javaops.masterjava.xml.util;

/**
 * https://gist.github.com/2sbsbsb/2951464
 */
public class HTMLTableBuilder {
    private int columns;
    private final StringBuilder table = new StringBuilder();
    public static String HTML_START = "<html>";
    public static String HTML_END = "</html>";
    public static String TABLE_START_BORDER = "<table border=\"1\">";
    public static String TABLE_START = "<table>";
    public static String TABLE_END = "</table>";
    public static String HEADER_START = "<th>";
    public static String HEADER_END = "</th>";
    public static String ROW_START = "<tr>";
    public static String ROW_END = "</tr>";
    public static String COLUMN_START = "<td>";
    public static String COLUMN_END = "</td>";

    /**
     * @param columns
     */
    public HTMLTableBuilder(int columns) {
        this.columns = columns;
        table.append(TABLE_START);
        table.append(TABLE_END);
    }

    /**
     * @param values
     */
    public void addTableHeader(Object... values) {
        int lastIndex = table.lastIndexOf(TABLE_END);
        if (lastIndex <= 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(ROW_START);
        if (values.length != columns) {
            for (int i = 0; i < columns; i++) {
                sb.append(HEADER_START);
                sb.append("values size error");
                sb.append(HEADER_END);
            }
        } else {
            for (Object value : values) {
                sb.append(HEADER_START);
                sb.append(value == null ? "" : value.toString());
                sb.append(HEADER_END);
            }
        }
        sb.append(ROW_END);
        table.insert(lastIndex, sb.toString());
    }

    /**
     * @param values
     */
    public void addRowValues(Object... values) {
        int lastIndex = table.lastIndexOf(TABLE_END);
        if (lastIndex <= 0) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(ROW_START);

        if (values.length != columns) {
            for (int i = 0; i < columns; i++) {
                sb.append(COLUMN_START);
                sb.append("values size error");
                sb.append(COLUMN_END);
            }
        } else {
            for (Object value : values) {
                sb.append(COLUMN_START);
                sb.append(value == null ? "" : value.toString());
                sb.append(COLUMN_END);
            }
        }
        sb.append(ROW_END);
        table.insert(lastIndex, sb.toString());
    }

    /**
     * @return
     */
    public String build() {
        return table.toString();
    }
}
