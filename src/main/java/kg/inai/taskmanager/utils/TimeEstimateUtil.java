package kg.inai.taskmanager.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Утилитарный класс для парсинга и форматирования оценки времени.
 * Поддерживаемые единицы: w (недели), d (дни), h (часы).
 * 1 неделя = 5 рабочих дней, 1 день = 8 часов.
 */
public class TimeEstimateUtil {

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

        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);
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
        return totalMinutes;
    }

    /**
     * Преобразует минуты в строку формата "xw yd zh".
     *
     * @param minutes количество минут
     * @return строка с оценкой времени
     */
    public static String formatFromMinutes(long minutes) {
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
}

