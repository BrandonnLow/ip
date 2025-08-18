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
            } else if (input.startsWith("todo ")) {
                String description = input.substring(5);
                Task newTask = new ToDo(description);
                tasks.add(newTask);

                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + newTask);
                System.out.println(" Now you have " + tasks.size() + " tasks in the list.");

            } else if (input.startsWith("deadline ")) {
                String[] parts = input.substring(9).split(" /by ");

                String description = parts[0];
                String by = parts[1];
                Task newTask = new Deadline(description, by);
                tasks.add(newTask);

                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + newTask);
                System.out.println(" Now you have " + tasks.size() + " tasks in the list.");


            } else if (input.startsWith("event ")) {

                String part1 = input.substring(6);

                String[] part2 = part1.split(" /from ");

                String[] part3 = part2[1].split(" /to ");

                String description = part2[0];
                String start = part3[0];
                String end = part3[1];

                Task newTask = new Event(description, start, end);
                tasks.add(newTask);

                System.out.println(" Got it. I've added this task:");
                System.out.println("   " + newTask);
                System.out.println(" Now you have " + tasks.size() + " tasks in the list.");

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