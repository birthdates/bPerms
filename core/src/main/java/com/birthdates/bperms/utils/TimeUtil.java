package com.birthdates.bperms.utils;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Map;

/**
 * Time utility class
 */
public class TimeUtil {

    /**
     * Our map of time characters to their millisecond multiplier
     */
    private static Map<Character, Long> TIME_MAP =
            ImmutableMap.of('m', 60L, 'h', 3600L, 'd', 86400L, 'w', 604800L, 'y', 31536000L);

    /**
     * Parse a time in milliseconds from a string like "1h30m" -> 5400000L
     *
     * @param str Target time string
     * @return The time that was found or -1 if no time was found.
     */
    public static long getTime(String str) {
        if (str.equalsIgnoreCase("forever") || str.equalsIgnoreCase("permanent")) return -1L;
        long time = -1;
        char[] chars = str.toCharArray();
        StringBuilder currentNumber = new StringBuilder();
        for (char aChar : chars) {
            // If this character is a digit, add it to our string builder to parse later
            if (NumberUtils.isDigits(Character.toString(aChar))) {
                currentNumber.append(aChar);
                continue;
            }

            // If the string is empty, we haven't found digits yet and this will cause an error with parseInt
            String numStr = currentNumber.toString();
            if (StringUtils.isEmpty(numStr)) continue;

            long num = Integer.parseInt(numStr) * 1000L * TIME_MAP.getOrDefault(aChar, 1L); // Seconds aren't in the map because it defaults to * 1
            currentNumber.setLength(0); // Reset string builder
            if (time == -1) time = 0;
            time += num;
        }

        return time;
    }

}
