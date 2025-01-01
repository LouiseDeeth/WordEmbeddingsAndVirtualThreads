package ie.atu.sw;

import java.util.*;
import java.io.*;

/**
 * @author Louise Deeth
 * @version 1.0
 * @since 20.0.1
 */

/**
 * Runner class for managing the application lifecycle.
 * 
 * Handles user input, menu navigation, and execution of the application.
 */
public class Runner {
	private static EmbeddingsParser embeddingsParser = new EmbeddingsParser();
	private String inputFilePath;
	private String outputFilePath;
	private boolean running = true;// control the loop

	/**
	 * Main method to run the application.
	 * 
	 * @param args Command-line arguments
	 * @throws Exception if an error occurs
	 * 
	 * O(1) constant time
	 */
	public static void main(String[] args) throws Exception {
		Runner r = new Runner();
		try (Scanner s = new Scanner(System.in)) {
			Menu menu = new Menu(s, r);
			while (r.isRunning()) {
				menu.displayMenu();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * stopRunning - stops the application
	 */
	public void stopRunning() {
		this.running = false;
	}

	/**
	 * isRunning - checks if the application is still running
	 * 
	 * @return true if running, false otherwise
	 */
	public boolean isRunning() {
		return this.running;
	}

	/**
	 * Prompts the user for a file path.
	 * 
	 * @param s             Scanner for user input
	 * @param promptMessage Message to display to the user
	 * @param mustExist     The file must already exist
	 * @return The entered file path
	 */
	private String promptFilePath(Scanner s, String promptMessage, boolean mustExist) {
		while (true) {
			System.out.print(promptMessage);
			String filePath = s.nextLine().trim();
			File file = new File(filePath);

			if (mustExist && !file.exists()) {
			    System.out.println("File does not exist. Please enter a valid file path.");
			    continue;
			}
			return filePath;
		}
	}

	/**
	 * Prompts the user to specify the embeddings file path.
	 * 
	 * @param s Scanner for user input
	 */
	public void specifyEmbeddingFile(Scanner s) {
		String filePath = promptFilePath(s, "\nPlease enter the embedding file path (e.g., ./word-embeddings.txt): ",
				true);
		try {
			embeddingsParser.loadEmbeddings(filePath);
			System.out.println("Embeddings loaded successfully.");
		} catch (IOException e) {
			System.out.println("Failed to load: " + e.getMessage());
		}
	}

	/**
	 * Prompts the user to specify the input text file path.
	 * 
	 * @param s Scanner for user input
	 */
	public void specifyInputFile(Scanner s) {
		inputFilePath = promptFilePath(s, "\nEnter the path to the text file to simplify (e.g., ./input.txt): ", true);
		System.out.println("Text file set to: " + inputFilePath);
	}

	/**
	 * Prompts the user to specify the output file path.
	 * 
	 * @param s Scanner for user input
	 * 
	 * O(1) constant time - no reading in of data
	 */
	public void specifyOutputFile(Scanner s) {
		outputFilePath = promptFilePath(s, "\nEnter the output file path (e.g., ./out.txt): ", false);
		System.out.println("Output file set to: " + outputFilePath);
	}

	/**
	 * Executes the processing and analysis of files.
	 * 
	 * @param s Scanner for user input
	 */
	public void executeAndAnalyse(Scanner s) {
	    VirtualThreadProcessor processor = new VirtualThreadProcessor();

	    try {
	        if (inputFilePath == null || outputFilePath == null) {
	            System.err.println("Input and output file paths must be specified before execution.");
	            return;
	        }
	        if (inputFilePath.equals(outputFilePath)) {
	            System.err.println("Input and output file paths cannot be the same. Please specify a different output file.");
	            return;
	        }
	        if (embeddingsParser.getEmbeddingsMap().isEmpty()) {
	            System.err.println("Embeddings file must be loaded before execution.");
	            return;
	        }

	        System.out.println("Processing embeddings file...");
	        processor.processEmbeddingsFile(inputFilePath); 

	        System.out.println("Loading Google 1000 words...");
	        processor.loadGoogleWords(); 

	        System.out.println("Simplifying text file...");
	        processor.simplifyTextFile(inputFilePath, outputFilePath); 

	        System.out.println("Execution and analysis complete.");
	    } catch (Exception e) {
	        System.err.println("An error occurred: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	/**
	 * Configure options method to allow user to choose background & font colours
	 * 
	 * @param s Scanner for user input
	 * 
	 * O(1) constant time - no reading in of data
	 */
	public void configureOptions(Scanner s) {
		System.out.println("\nConfigure Options:");
		System.out.println("1. Background: Black.  Font: White");
		System.out.println("2. Background: White.  Font: Black");
		System.out.println("3. Background: Blue.   Font: Black");
		System.out.println("4. Background: Green.  Font: Black");
		System.out.println("5. Background: Cyan.   Font: Black");
		System.out.println("6. Reset to Default");

		int option = 0;

		while (true) {
		    System.out.print("Select an option (1-6): ");
		    String input = s.nextLine().trim();
		    try {
		        option = Integer.parseInt(input);
		        if (option >= 1 && option <= 6) {
		            break; // Valid option
		        } else {
		            System.out.println("Invalid option. Please enter a number between 1 and 6.");
		        }
		    } catch (NumberFormatException e) {
		        System.out.println("Invalid input. Please enter a number between 1 and 6.");
		    }
		}

		switch (option) {
		case 1:
			System.out.print(ConsoleColour.BLACK_BACKGROUND);
			System.out.print(ConsoleColour.WHITE);
			break;
		case 2:
			System.out.print(ConsoleColour.WHITE_BACKGROUND);
			System.out.print(ConsoleColour.BLACK_BOLD);
			break;
		case 3:
			System.out.print(ConsoleColour.BLUE_BACKGROUND);
			System.out.print(ConsoleColour.BLACK_BOLD);
			break;
		case 4:
			System.out.print(ConsoleColour.GREEN_BACKGROUND);
			System.out.print(ConsoleColour.BLACK_BOLD);
			break;
		case 5:
			System.out.print(ConsoleColour.CYAN_BACKGROUND);
			System.out.print(ConsoleColour.BLACK_BOLD);
			break;
		case 6:
			System.out.print(ConsoleColour.RESET);
			System.out.println("Colours reset to default.\n");
			break;
		default:
			break;
		}
	}
}