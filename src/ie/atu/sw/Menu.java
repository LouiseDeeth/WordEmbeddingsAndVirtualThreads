package ie.atu.sw;

import java.util.*;

public class Menu {
	private final Scanner s;
	private final Runner r;

	public Menu(Scanner s, Runner r) {
		this.s = s;
		this.r = r;
	}

	/**
	 * Displays the Menu for user input
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
			System.out.println("(2) Specify Google 1000 File");
			System.out.println("(3) Specify an Output File (default: ./out.txt)");
			System.out.println("(4) Execute, Analyse and Report");
			System.out.println("(5) Configure Options");
			System.out.println("(6) Quit\n");

			// Output a menu of options and solicit text from the user
			System.out.print("Select Option [1-6]> ");
			
			int choice = -1;
			if(s.hasNextLine()) {
				choice = s.nextInt();
		    	s.nextLine();
		    } else {
		        System.out.println("Invalid option. Please enter a number between 1 and 6: \n");
		    	s.nextLine();
		    	continue;
		    }

			switch (choice) {
			case 1: // Specify Embedding File
				r.specifyEmbeddingFile(s);
				break;
				
			case 2: // Specify Embedding File
				r.specifyGoogle1000File(s);
				break;

			case 3: // Specify an Output File (default: ./out.txt)
				r.specifyOutputFile(s);
				break;

			case 4: // Enter a Word
				//r.enterWordOrText(s);
				break;

			case 5: // Configure Options
				r.configureOptions(s);
				break;

			case 6: // Quit
				r.stopRunning();// to exit loop
				System.out.println("Exiting");
				break;

			default:// Default
		        System.out.println("Invalid option. Please enter a number between 1 and 6: \n");
				break;
			}
		}
	}
}
