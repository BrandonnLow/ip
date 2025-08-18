import java.util.Scanner;

public class Pingpong {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Pingpong");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");

        while (!(input = scanner.nextLine()).equals("bye")) {
            System.out.println("____________________________________________________________");
            System.out.println(" " + input);
            System.out.println("____________________________________________________________");
        }

        System.out.println("____________________________________________________________");
        System.out.println(" Bye, hope to see you again soon!");
        System.out.println("____________________________________________________________");

        scanner.close();
    }
}