import java.util.ArrayList;
import java.util.Scanner;

public class Pingpong {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Pingpong");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");

        String input;
        while (!(input = scanner.nextLine()).equals("bye")) {
            System.out.println("____________________________________________________________");

            if (input.equals("list")) {
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println(" " + (i + 1) + "." + tasks.get(i));
                }
            } else if (input.startsWith("mark ")) {
                try {
                    int taskNum = Integer.parseInt(input.substring(5)) - 1;
                    if (taskNum >= 0 && taskNum < tasks.size()) {
                        Task task = tasks.get(taskNum);
                        task.markAsDone();
                        System.out.println(" Nice! I've marked this task as done:");
                        System.out.println("  " + task);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" Please provide a valid task number!");
                }
            } else if (input.startsWith("unmark ")) {
                try {
                    int taskNum = Integer.parseInt(input.substring(7)) - 1;
                    if (taskNum >= 0 && taskNum < tasks.size()) {
                        Task task = tasks.get(taskNum);
                        task.markAsUndone();
                        System.out.println(" OK, I've marked this task as not done yet:");
                        System.out.println("  " + task);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" Please provide a valid task number!");
                }
            } else {
                Task newTask = new Task(input);
                tasks.add(newTask);
                System.out.println(" added: " + input);
            }

            System.out.println("____________________________________________________________");
        }

        System.out.println("____________________________________________________________");
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");

        scanner.close();
    }
}