package ie.atu.sw;

import java.util.*;

/**
 * This class is the user interface for the Virtual Threaded Text Simplifier.
 */
public class Menu {
	private final Scanner s;
	private final Runner r;

	/**
	 * Constructor to initialize the Menu with a Scanner and Runner instance.
	 *
	 * @param s Scanner for user input
	 * @param r Runner for executing operations
	 * 
	 * O(n) constant time as depends on number of iterations of menu loop
	 */
	public Menu(Scanner s, Runner r) {
		this.s = s;
		this.r = r;
	}

	/**
	 * Displays the Menu for user input and handles user choices Runs in a loop until user exits
	 */
	public void displayMenu() {
		while (r.isRunning()) {// keep showing the menu until user chooses to exit
			System.out.println("************************************************************");
			System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
			System.out.println("*                                                          *");
			System.out.println("*             Virtual Threaded Text Simplifier             *");
			System.out.println("*                                                          *");
			System.out.println("************************************************************");
			System.out.println("(1) Specify Embeddings File");
			System.out.println("(2) Specify Text File to Simplify");
			System.out.println("(3) Specify an Output File");
			System.out.println("(4) Execute, Analyse and Report");
			System.out.println("(5) Configure Options");
			System.out.println("(6) Quit\n");

			// Output a menu of options and solicit text from the user
			System.out.print("Select Option [1-6]> ");

			int choice = -1;
			try {
			    choice = s.nextInt();
			    s.nextLine(); // Consume the newline character
			} catch (InputMismatchException e) {
			    System.out.println("Invalid option. Please enter a number between 1 and 6.");
			    s.nextLine(); // Consume the invalid input
			    continue;
			}

			// Handle user choices using a switch statement
			switch (choice) {
			case 1: // Specify Embedding File
				r.specifyEmbeddingFile(s);
				break;

			case 2: // Specify an Input File to Simplify
				r.specifyInputFile(s);
				break;

			case 3: // Specify an Output File
				r.specifyOutputFile(s);
				break;

			case 4: // Execute, Analyse and Report
				r.executeAndAnalyse(s);
				break;

			case 5: // Configure Options
				r.configureOptions(s);
				break;

			case 6: // Quit
				r.stopRunning(); // Exit loop
				System.out.println("Exiting");
				break;

			default:// Default
				System.out.println("Invalid option. Please enter a number between 1 and 6: \n");
				break;
			}
		}
	}
}
