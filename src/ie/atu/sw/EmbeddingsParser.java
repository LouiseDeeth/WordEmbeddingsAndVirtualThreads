package ie.atu.sw;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

/**
 * The EmbeddingsParser class handles parsing word embeddings from a file 
 * and storing them in a ConcurrentHashMap for thread-safe operations.
 */
public class EmbeddingsParser {

	// The HashMap to store the word and its embeddings
	private Map<String, double[]> embeddingsMap;

	/**
	 * Constructor initializes an empty ConcurrentHashMap to store embeddings.
	 * 
	 * O(1) constant time
	 */
	public EmbeddingsParser() {
		this.embeddingsMap = new ConcurrentHashMap<>();
	}

	/**
	 * Load embeddings from a file into the embeddings map and print progress
	 * 
	 * @param filePath
	 * @throws IOException
	 * 
	 *  O(n) n is the number of lines in the file
	 */
	public void loadEmbeddings(String filePath) throws IOException {
		File file = new File(filePath);
		long totalLines = countLines(file);
		long linesProcessed = 0;

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				// Parse the line and add to HashMap
				parseLineAndAddToMap(line);
				linesProcessed++;
				if (linesProcessed % 100 == 0 || linesProcessed == totalLines) {
					ProgressReporter.printProgress((int) linesProcessed, (int) totalLines);
				}
			}
		}
		ProgressReporter.printProgress(100, 100);
		System.out.println();// move to a newline after finishing progress update
	}

	/**
	 * Parsing the line and adding to map the word and its embeddings
	 * 
	 * @param line The line to parse
	 * 
	 * O(n) parses over and reads in n number of lines
	 */
	private void parseLineAndAddToMap(String line) {
		// Split line into parts
		String[] parts = line.split(",\\s+");

		// The first part is the word
		String word = parts[0];

		// The rest is the embedding values
		double[] values = new double[parts.length - 1];
		for (int i = 1; i < parts.length; i++) {
			values[i - 1] = Double.parseDouble(parts[i].trim());
		}
		// Store the word and its embeddings in the map (case-insensitive)
		embeddingsMap.put(word.toLowerCase(), values);// to ignore the case entered
	}

	/**
	 * Counts the number of lines in a file.
	 * 
	 * @param file the file to count lines in
	 * @return the total number of lines in the file
	 * 
	 * O(n),where n is the number of lines in the file
	 */
	private long countLines(File file) throws IOException {
		try (var lineIterator = new LineNumberReader(new FileReader(file))) {
			lineIterator.skip(Long.MAX_VALUE);
			return lineIterator.getLineNumber() + 1;// because line nos start at 0
		}
	}

	/**
	 * Get embedding for a word
	 * 
	 * @param word the word whose embedding is to be retrieved
	 * @return word from the HashMap 
	 * 
	 * O(1) retrieving from a HashMap is constant time
	 */
	public double[] getEmbedding(String word) {
		if (word == null || word.trim().isEmpty()) {
			throw new IllegalArgumentException("Word cannot be null or empty.");
		}
		return embeddingsMap.get(word.toLowerCase());// ignore case
	}

	/**
	 * Provide access to the embeddings map
	 * 
	 * @return embeddingsMap
	 * 
	 * 0(1) constant time
	 */
	public Map<String, double[]> getEmbeddingsMap() {
		return Collections.unmodifiableMap(embeddingsMap);
	}

}