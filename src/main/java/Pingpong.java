import java.util.ArrayList;
import java.util.Scanner;

public class Pingpong {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> tasks = new ArrayList<>();

        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Pingpong");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");

        String input;
        while (!(input = scanner.nextLine()).equals("bye")) {
            System.out.println("____________________________________________________________");

            if (input.equals("list")) {
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println(" " + (i + 1) + ". " + tasks.get(i));
                }
            } else {
                tasks.add(input);
                System.out.println(" added: " + input);
            }

            System.out.println("____________________________________________________________");
        }

        System.out.println("____________________________________________________________");
        System.out.println(" Bye, hope to see you again soon!");
        System.out.println("____________________________________________________________");

        scanner.close();
    }
}