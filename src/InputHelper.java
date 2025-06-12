import java.util.Scanner;

public class InputHelper {

    // Helper to get valid user input within a range
    protected static int getValidatedInput(Scanner scanner, int min, int max, String prompt, String errorMessage) {
        int choice = -1;

        while (true) {
            System.out.print(prompt);
            
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();

                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.println("ERROR: " + errorMessage);
                }

            } else {
                System.out.println("ERROR: Please enter a valid number.");
                scanner.next();
            }
        }
    }
}
