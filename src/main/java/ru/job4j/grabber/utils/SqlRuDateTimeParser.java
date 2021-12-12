package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {

    private static final Map<String, String> MONTHS = Map.ofEntries(
            Map.entry("01", "янв"),
            Map.entry("02", "фев"),
            Map.entry("03", "мар"),
            Map.entry("04", "апр"),
            Map.entry("05", "май"),
            Map.entry("06", "июн"),
            Map.entry("07", "июл"),
            Map.entry("08", "авг"),
            Map.entry("09", "сен"),
            Map.entry("10", "окт"),
            Map.entry("11", "ноя"),
            Map.entry("12", "дек")
    );
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public SqlRuDateTimeParser() {
    }

    @Override
    public LocalDateTime parse(String parse) {
        String rsl;
        String[] parseSplit = parse.split("[, ]+");
        if ("сегодня".equals(parseSplit[0])) {
            String[] today = LocalDateTime.now()
                    .format(formatter)
                    .split(" ");
            rsl = today[0] + " " + parseSplit[1];
        } else if ("вчера".equals(parseSplit[0])) {
            String[] yesterday = LocalDateTime.now()
                    .minusDays(1)
                    .format(formatter)
                    .split(" ");
            rsl = yesterday[0] + " " + parseSplit[1];
        } else {
            for (Map.Entry<String, String> entry : MONTHS.entrySet()) {
                if (parseSplit[1].equals(entry.getValue())) {
                    parseSplit[1] = entry.getKey();
                }
            }
            if (parseSplit[0].length() == 1) {
                parseSplit[0] = "0" + parseSplit[0];
            }
            rsl = "20" + parseSplit[2]
                    + "-" + parseSplit[1]
                    + "-" + parseSplit[0]
                    + " " + parseSplit[3];
        }
        return LocalDateTime.parse(rsl, formatter);
    }
}
