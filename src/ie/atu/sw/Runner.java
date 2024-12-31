package ie.atu.sw;
import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.nio.file.Files;

/**
 * @author Louise Deeth
 * @version 1.0
 */

public class Runner {
    private static EmbeddingsParser embeddingsParser = new EmbeddingsParser();
    private static PrintWriter outputFile = null; 
    private boolean running = true;//control the loop   
    
    // main - O(1) constant time
	public static void main(String[] args) throws Exception {
		Runner r = new Runner();		
		try (Scanner s = new Scanner(System.in)) {
			Menu menu = new Menu(s, r);
			while(r.isRunning()) {
				menu.displayMenu();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			r.cleanup();
		}
	}
	
	/**
	 * stopRunning method
	 */
	public void stopRunning() {
       this.running = false;
    }
    
	/**
	 * isRunning method
	 * @return current state of running variable
	 */
	public boolean isRunning() {
        return this.running;
    }

	/**
	 * The method to show menu is called from here
	 * @param s
	 */
    public void showMenu(Scanner s) {
        Menu menu = new Menu(s, this);
        menu.displayMenu();
    }
	
    /**
     * specify embedding file method
     * @param s
     */
	public void specifyEmbeddingFile(Scanner s) {
	    
		String filePath = "";
	    File file = null;

	    // Loop until a valid file path is entered
	    while (true) {
	        System.out.print("\nPlease enter the embedding file path (eg ./word-embeddings.txt): ");
	        filePath = s.nextLine().trim();

	        if (filePath.isEmpty()) {
	            System.out.println("File path cannot be empty. Please enter a valid file path.");
	        } else {
	            file = new File(filePath);
	            if (!file.exists()) {
	                System.out.println("File does not exist: " + filePath + ". Please enter a valid file path.");
	            } else {
	                break; // Valid file path entered; exit loop
	            }
	        }
	    }		
		//Used to speed up debugging with a default path
		/*System.out.print("\nPlease enter the embedding path (default: ./word-embeddings.txt): ");
		String filePath = s.nextLine().trim(); //.trim()is used to remove whitespaces from a string
		if(filePath.isEmpty()) {
			filePath = "./word-embeddings.txt";
		}
		//check if file exists
		File file = new File(filePath);
		if (!file.exists()) {
			System.out.println("File does not exist: " + filePath);
			return;
		}*/
	    
		try {
			embeddingsParser.loadEmbeddings(filePath);
			System.out.println("Embeddings loaded successfully");		
		//embeddingsParser.printRandomSample(5); used to output 5 random words for debugging
		}catch(IOException e) {
			System.out.println("Failed to load: " + e.getMessage());
		}
	}
	
	
    /**
     * specify embedding file method
     * @param s
     */
	public void specifyGoogle1000File(Scanner s) {
		String filePath = "";
	    File file = null;

	    // Loop until a valid file path is entered
	    while (true) {
	        System.out.print("\nPlease enter the google 100 file path (eg ./google-1000.txt): ");
	        filePath = s.nextLine().trim();

	        if (filePath.isEmpty()) {
	            System.out.println("File path cannot be empty. Please enter a valid file path.");
	        } else {
	            file = new File(filePath);
	            if (!file.exists()) {
	                System.out.println("File does not exist: " + filePath + ". Please enter a valid file path.");
	            } else {
	                break; // Valid file path entered; exit loop
	            }
	        }
	    }	
	    
	    try {
	    	List<String> googleWords = Files.readAllLines(file.toPath())
	    			.stream()
	    			.map(String::trim)
	    			.filter(word -> !word.isEmpty())
	    			.collect(Collectors.toList());
	    	System.out.println("Google-100 loaded successfully");
	    } catch (IOException e){ 
	    	System.out.println("Google-100 load failed: " + e.getMessage());
	    }
	}
	
	
	/**
	 * specify output file method
	 * @param s
	 * O(1) constant time - no reading in of data
	 */
	public void specifyOutputFile(Scanner s) {	
		System.out.print("\nEnter the output file path (press Enter for default: ./out.txt): ");
		String outputPath = s.nextLine().trim();
		if(outputPath.isEmpty()) {
			outputPath = "./out.txt";//default path
		}
        initializeOutputFile(outputPath);
    }

	/**
	 * Initialize the Output File & check that it has been initialised
	 * @param path
	 * O(1) constant time
	 */
	private void initializeOutputFile(String path) {
        try {
            if (outputFile != null) {
                outputFile.close(); // Close existing file writer if open
            }
            outputFile = new PrintWriter(new FileWriter(path, true));
            System.out.println("Output file specified: " + path);
        } catch (IOException e) {
            System.err.println("Error opening the output file: " + e.getMessage());
        }
    }
	private void ensureOutputFileInitialized(Scanner s) {
	    if (outputFile == null) {
	        System.out.println("Output file not specified. Please specify an output file path (press Enter for default: ./out.txt):");
	        String outputPath = s.nextLine().trim();
	        initializeOutputFile(outputPath.isEmpty() ? "./out.txt" : outputPath);
	    }
	}

	/**
	 * cleanup method
	 * O(1) constant time
	 */
    private void cleanup() {
        if (outputFile != null) {
            outputFile.close();
        }
    }     

	/**
	 * Configure options method to allow user to choose background & font colours
	 * @param s
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
	    
	    while(true) {
		    System.out.print("Select an option (1-6): ");
		    if(s.hasNextLine()) {
		    	option = s.nextInt();
		    	s.nextLine();
		    	if(option >= 1 && option <=6) {
		    		break; // valid option, exit the loop
		    	} else {
		            System.out.println("Invalid option. Please enter a number between 1 and 6: \n");
		    	}
		    } else {
	            System.out.println("Invalid option. Please enter a number between 1 and 6: \n");
	            s.nextLine(); // Consume newline	   
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