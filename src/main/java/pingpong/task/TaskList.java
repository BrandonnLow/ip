package pingpong.task;

import pingpong.PingpongException;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Contains the task list and operations to add/delete tasks
 */
public class TaskList {

    private ArrayList<Task> tasks;


    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public Task deleteTask(int index) throws PingpongException {
        if (index < 0 || index >= tasks.size()) {
            throw new PingpongException("Task number " + (index + 1) + " does not exist.");
        }
        return tasks.remove(index);
    }

    public Task markTask(int index) throws PingpongException {
        if (index < 0 || index >= tasks.size()) {
            throw new PingpongException("Task number " + (index + 1) + " does not exist.");
        }
        Task task = tasks.get(index);
        task.markAsDone();
        return task;
    }


    public Task unmarkTask(int index) throws PingpongException {
        if (index < 0 || index >= tasks.size()) {
            throw new PingpongException("Task number " + (index + 1) + " does not exist.");
        }
        Task task = tasks.get(index);
        task.markAsUndone();
        return task;
    }

    public Task getTask(int index) throws PingpongException {
        if (index < 0 || index >= tasks.size()) {
            throw new PingpongException("Task number " + (index + 1) + " does not exist.");
        }
        return tasks.get(index);
    }

    public ArrayList<Task> getAllTasks() {
        return tasks;
    }

    public int size() {
        return tasks.size();
    }

    public Task addTodo(String description) {
        Task task = new Todo(description);
        addTask(task);
        return task;
    }

    public Task addDeadline(String description, LocalDate by) {
        Task task = new Deadline(description, by);
        addTask(task);
        return task;
    }

    public Task addEvent(String description, LocalDateTime start, LocalDateTime end) {
        Task task = new Event(description, start, end);
        addTask(task);
        return task;
    }


    public ArrayList<Task> findTasksOnDate(LocalDate targetDate) {
        ArrayList<Task> matchingTasks = new ArrayList<>();

        for (Task task : tasks) {
            boolean matches = false;

            if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                if (deadline.getBy().equals(targetDate)) {
                    matches = true;
                }
            } else if (task instanceof Event) {
                Event event = (Event) task;
                LocalDate startDate = event.getStart().toLocalDate();
                LocalDate endDate = event.getEnd().toLocalDate();
                if (!targetDate.isBefore(startDate) && !targetDate.isAfter(endDate)) {
                    matches = true;
                }
            }

            if (matches) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}
