package pingpong.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Handles search operations for tasks.
 */
public class TaskSearcher {

    /**
     * Finds all tasks that occur on the specified date.
     *
     * @param tasks the list of tasks to search
     * @param targetDate the date to search for
     * @return a list of tasks occurring on the specified date
     */
    public static ArrayList<Task> findTasksOnDate(ArrayList<Task> tasks, LocalDate targetDate) {
        assert targetDate != null : "Target date should not be null";
        assert tasks != null : "Task list should not be null";

        return tasks.stream()
                .filter(task -> isTaskOnDate(task, targetDate))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Finds all tasks that contain the specified keyword in their description.
     *
     * @param tasks the list of tasks to search
     * @param keyword the keyword to search for
     * @return a list of tasks whose descriptions contain the keyword
     */
    public static ArrayList<Task> findTasksByKeyword(ArrayList<Task> tasks, String keyword) {
        assert keyword != null : "Keyword should not be null";
        assert !keyword.trim().isEmpty() : "Keyword should not be empty";
        assert tasks != null : "Task list should not be null";

        String keywordLower = keyword.toLowerCase();

        return tasks.stream()
                .filter(task -> containsKeyword(task, keywordLower))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Finds all tasks that contain any of the specified keywords.
     *
     * @param tasks the list of tasks to search
     * @param keywords the keywords to search for
     * @return a list of tasks whose descriptions contain any of the keywords
     */
    public static ArrayList<Task> findTasksByKeywords(ArrayList<Task> tasks, String... keywords) {
        assert keywords != null : "Keywords array should not be null";
        assert tasks != null : "Task list should not be null";

        if (keywords.length == 0) {
            return new ArrayList<>();
        }

        return tasks.stream()
                .filter(task -> taskMatchesAnyKeyword(task, keywords))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private static boolean isTaskOnDate(Task task, LocalDate targetDate) {
        if (task instanceof Deadline) {
            return isDeadlineOnDate((Deadline) task, targetDate);
        } else if (task instanceof Event) {
            return isEventOnDate((Event) task, targetDate);
        }
        return false;
    }

    private static boolean isDeadlineOnDate(Deadline deadline, LocalDate targetDate) {
        assert deadline != null : "Deadline should not be null";
        assert deadline.getBy() != null : "Deadline date should not be null";
        assert targetDate != null : "Target date should not be null";

        return deadline.getBy().equals(targetDate);
    }

    private static boolean isEventOnDate(Event event, LocalDate targetDate) {
        assert event != null : "Event should not be null";
        assert event.getStart() != null : "Event start time should not be null";
        assert event.getEnd() != null : "Event end time should not be null";
        assert targetDate != null : "Target date should not be null";

        LocalDate startDate = event.getStart().toLocalDate();
        LocalDate endDate = event.getEnd().toLocalDate();
        return !targetDate.isBefore(startDate) && !targetDate.isAfter(endDate);
    }

    private static boolean containsKeyword(Task task, String keywordLower) {
        assert task != null : "Task should not be null";
        assert task.getDescription() != null : "Task description should not be null";
        assert keywordLower != null : "Keyword should not be null";

        return task.getDescription().toLowerCase().contains(keywordLower);
    }

    private static boolean taskMatchesAnyKeyword(Task task, String... keywords) {
        assert task != null : "Task should not be null";
        assert task.getDescription() != null : "Task description should not be null";
        assert keywords != null : "Keywords array should not be null";

        String descriptionLower = task.getDescription().toLowerCase();
        return Arrays.stream(keywords)
                .anyMatch(keyword -> descriptionLower.contains(keyword.toLowerCase()));
    }
}
