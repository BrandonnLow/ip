package pingpong.command.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import pingpong.PingpongException;

/**
 * Handles parsing of date and datetime strings.
 */
public class DateTimeParser {
    // Date format strings
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATETIME_FORMAT_HHMM = "yyyy-MM-dd HHmm";
    private static final String DATETIME_FORMAT_COLON = "yyyy-MM-dd HH:mm";
    private static final String DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}";
    private static final String DATETIME_HHMM_REGEX = "\\d{4}-\\d{2}-\\d{2} \\d{4}";
    private static final String DATETIME_COLON_REGEX = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}";

    /**
     * Parses a date string in yyyy-MM-dd format into a LocalDate object.
     *
     * @param dateStr the date string to parse
     * @return the parsed LocalDate
     * @throws PingpongException if the date format is invalid
     */
    public static LocalDate parseDate(String dateStr) throws PingpongException {
        assert dateStr != null : "Date string should not be null";
        assert !dateStr.trim().isEmpty() : "Date string should not be empty";

        try {
            LocalDate parsed = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(DATE_FORMAT));
            assert parsed != null : "Parsed date should not be null";
            return parsed;
        } catch (DateTimeParseException e) {
            throw new PingpongException("Invalid date format. Please use yyyy-MM-dd format (e.g., 2019-12-02)");
        }
    }

    /**
     * Parses a date-time string into a LocalDateTime object.
     * Supports multiple formats:
     * - "yyyy-MM-dd HHmm" (e.g., "2019-12-02 1800")
     * - "yyyy-MM-dd HH:mm" (e.g., "2019-12-02 18:00")
     * - "yyyy-MM-dd" (treated as start of day)
     *
     * @param dateTimeStr the date-time string to parse
     * @return the parsed LocalDateTime
     * @throws PingpongException if the date-time format is invalid
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) throws PingpongException {
        assert dateTimeStr != null : "DateTime string should not be null";
        assert !dateTimeStr.trim().isEmpty() : "DateTime string should not be empty";

        try {
            LocalDateTime parsed;
            if (dateTimeStr.matches(DATETIME_HHMM_REGEX)) {
                parsed = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(DATETIME_FORMAT_HHMM));
            } else if (dateTimeStr.matches(DATETIME_COLON_REGEX)) {
                parsed = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(DATETIME_FORMAT_COLON));
            } else if (dateTimeStr.matches(DATE_REGEX)) {
                parsed = LocalDate.parse(dateTimeStr, DateTimeFormatter.ofPattern(DATE_FORMAT)).atStartOfDay();
            } else {
                throw new DateTimeParseException("Unsupported format", dateTimeStr, 0);
            }

            assert parsed != null : "Parsed datetime should not be null";
            return parsed;
        } catch (DateTimeParseException e) {
            throw new PingpongException("Invalid datetime format."
                    + "Please use formats like: 2019-12-02 1800, 2019-12-02 18:00, or 2019-12-02");
        }
    }
}
