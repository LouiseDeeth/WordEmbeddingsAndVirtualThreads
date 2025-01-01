package ie.atu.sw;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

public class EmbeddingsParser {

	// The HashMap to store the word and its embeddings
	private Map<String, double[]> embeddingsMap;

	public EmbeddingsParser() {
		this.embeddingsMap = new ConcurrentHashMap<>();
	}

	/**
	 * Load embeddings method to parse embeddings in and print progress
	 * 
	 * @param filePath
	 * @throws IOException
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
	 * Parsing the line and adding to map
	 * 
	 * @param line O(n) parses over and reads in n number of lines
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
		embeddingsMap.put(word.toLowerCase(), values);// to ignore the case entered
	}

	private long countLines(File file) throws IOException {
		try (var lineIterator = new LineNumberReader(new FileReader(file))) {
			lineIterator.skip(Long.MAX_VALUE);
			return lineIterator.getLineNumber() + 1;// because line nos start at 0
		}
	}

	/**
	 * Get embedding
	 * 
	 * @param word
	 * @return word from the HashMap O(1) retrieving from a HashMap is constant time
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
	 */
	public Map<String, double[]> getEmbeddingsMap() {
	    return Collections.unmodifiableMap(embeddingsMap);
	}

}