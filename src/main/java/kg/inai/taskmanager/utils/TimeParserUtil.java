package kg.inai.taskmanager.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Утилитарный класс для парсинга и форматирования оценки времени.
 * Поддерживаемые единицы: w (недели), d (дни), h (часы).
 * 1 неделя = 5 рабочих дней, 1 день = 8 часов.
 */
public class TimeParserUtil {

    /**
     * Преобразует строку вида "xw yd zh" в минуты. Оценка времени
     * может содержать любое сочетание, разделенное пробелами: "2w 1d 4h"
     *
     * @param input строка с оценкой времени
     * @return общее количество минут
     */
    public static long parseToMinutes(String input) {
        long totalMinutes = 0;
        Pattern pattern = Pattern.compile("(\\d+)\\s*(w|d|h)");
        Matcher matcher = pattern.matcher(input);
        int matchedLength = 0;

        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);
            matchedLength += matcher.group().length();

            switch (unit) {
                case "w":
                    totalMinutes += (long) value * 5 * 8 * 60;
                    break;
                case "d":
                    totalMinutes += (long) value * 8 * 60;
                    break;
                case "h":
                    totalMinutes += value * 60L;
                    break;
            }
        }

        if (matchedLength != input.replaceAll("\\s+", "").length()) {
            throw new IllegalArgumentException("Некорректный формат времени: " + input);
        }
        return totalMinutes;
    }

    /**
     * Преобразует минуты в строку формата "xw yd zh".
     *
     * @param minutes количество минут
     * @return строка с оценкой времени
     */
    public static String formatFromMinutes(long minutes) {
        if (minutes <= 0) {
            return "Не определено";
        }
        
        long totalHours = minutes / 60;
        long weeks = totalHours / (5 * 8);
        totalHours %= 5 * 8;

        long days = totalHours / 8;
        long hours = totalHours % 8;

        StringBuilder sb = new StringBuilder();
        if (weeks > 0) sb.append(weeks).append("w ");
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h");
        return sb.toString().trim();
    }

    public static String formatWithLocalization(long minutes) {
        if (minutes <= 0) {
            return "Не определено";
        }

        long totalHours = minutes / 60;
        long weeks = totalHours / (5 * 8);
        totalHours %= (5 * 8);

        long days = totalHours / 8;
        long hours = totalHours % 8;

        StringBuilder result = new StringBuilder();

        long[] values = {weeks, days, hours};
        String[][] units = {
                {"неделя", "недели", "недель"},
                {"день", "дня", "дней"},
                {"час", "часа", "часов"}
        };

        for (int i = 0; i < values.length; i++) {
            long value = values[i];
            if (value > 0) {
                result.append(value)
                        .append(" ")
                        .append(pluralize(value, units[i][0], units[i][1], units[i][2]))
                        .append(" ");
            }
        }
        return result.toString().trim();
    }

    private static String pluralize(long count, String form1, String form2, String form5) {
        long n = Math.abs(count) % 100;
        long n1 = n % 10;
        if (n > 10 && n < 20) return form5;
        if (n1 > 1 && n1 < 5) return form2;
        if (n1 == 1) return form1;
        return form5;
    }
}

