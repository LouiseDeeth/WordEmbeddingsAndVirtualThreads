package ie.atu.sw;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.*;

/**
 * The VirtualThreadProcessor class processes text files using word embeddings.
 * 
 * It simplifies text by replacing words with similar ones from a list.
 * 
 */
public class VirtualThreadProcessor {
	private final Map<String, double[]> embeddingsMap; // Map for word embeddings
	private final List<String> googleWords = new CopyOnWriteArrayList<>(); // List of Google 1000 words
	private final List<String> simplifiedLines = new CopyOnWriteArrayList<>(); // List of simplified lines
	private final AtomicLong processedLines = new AtomicLong(); // Tracks processed lines
	private static final double SIMILARITY_THRESHOLD = 0.4; // Threshold for cosine similarity

	/** Constructor that takes EmbeddingsParser
	 * 
	 * @param embeddingsMap A map containing word embeddings.
	 * 
	 * O(1) constant time
	 */
	public VirtualThreadProcessor(Map<String, double[]> embeddingsMap) {
		// Create a new ConcurrentHashMap with the passed embeddings
		this.embeddingsMap = new ConcurrentHashMap<>(embeddingsMap);
	}

	/**
	 * Increments processed lines counter. 
	 * 
	 * O(1) constant time
	 */
	private void incrementProcessedLines() {
		processedLines.incrementAndGet();
	}

	/**
	 * Loads Google 1000 words into a list. 
	 * 
	 * O(n) Reads n lines from the file.
	 */
	public void loadGoogleWords() throws IOException {
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			List<Future<?>> futures = Files.lines(Paths.get("./google-1000.txt")).filter(line -> !line.trim().isEmpty())
					.<Future<?>>map(line -> executor.submit(() -> googleWords.add(line.trim().toLowerCase()))).toList();

			// Wait for all tasks to complete
			for (Future<?> future : futures) {
				try {
					future.get(); // Ensure all tasks complete successfully
				} catch (ExecutionException e) {
					System.err.println("Error loading Google words: " + e.getCause());
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new IOException("Loading interrupted", e);
				}
			}
		}
		System.out.println("Google 1000 words loaded successfully.");
	}

	/**
	 * Simplifies the text file by replacing words with the most similar ones.
	 * 
	 * @param inputPath  The path to the input file.
	 * @param outputPath The path to the output file.
	 * 
	 * O(n) for each
	 */
	public void simplifyTextFile(String inputPath, String outputPath) throws IOException {
		processedLines.set(0); // Reset counter before starting
		List<String> lines = Files.readAllLines(Paths.get(inputPath));
		long totalLines = lines.size();

		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			List<Future<?>> futures = lines.stream().<Future<?>>map(line -> executor.submit(() -> {
				simplifyLine(line);
				incrementProcessedLines();
				if (processedLines.get() % 10 == 0 || processedLines.get() == totalLines) {
					ProgressReporter.printProgress((int) processedLines.get(), (int) totalLines);
				}
			})).toList();

			for (Future<?> future : futures) {
				try {
					future.get();
				} catch (ExecutionException e) {
					System.err.println("Error simplifying text: " + e.getCause());
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new IOException("Simplification interrupted", e);
				}
			}
			// Write simplified lines to the output file
			Files.write(Paths.get(outputPath), simplifiedLines);
		}
		System.out.println("\nText file simplified successfully!\n");
	}

	/**
	 * Simplifies a line by replacing words.
	 * 
	 * @param line The line to simplify.
	 * 
	 * O(n) number of words in a line
	 */
	private void simplifyLine(String line) {
		String[] words = line.split("\\s+");
		String simplifiedLine = Arrays.stream(words).map(this::findMostSimilarWord).collect(Collectors.joining(" "));
		simplifiedLines.add(simplifiedLine);
	}

	/**
	 * Finds the most similar word using cosine similarity.
	 * 
	 * @param word The word to find a similar match for.
	 * @return The most similar word or the original word if no match is found.
	 * 
	 * O(n) number of words in Google list
	 */
	private String findMostSimilarWord(String word) {
		if (word == null || word.trim().isEmpty()) {
			return word;
		}

		// Preserve punctuation
		String originalWord = word;
		String cleanWord = word.replaceAll("[^a-zA-Z]", "").toLowerCase();

		// If word is empty or already in Google 1000, keep it
		if (cleanWord.isEmpty() || googleWords.contains(cleanWord)) {
			return originalWord;
		}

		double[] wordEmbedding = embeddingsMap.get(cleanWord);
		if (wordEmbedding == null) {
			// System.out.println("\nDEBUG: No embedding found for word: " + cleanWord);
			return originalWord;
		}
		// Find the most similar word from Google words
		List<Map.Entry<String, Double>> similarities = googleWords.stream()
				.filter(googleWord -> embeddingsMap.containsKey(googleWord))
				.map(googleWord -> Map.entry(googleWord,
						cosineSimilarity(wordEmbedding, embeddingsMap.get(googleWord))))
				.sorted(Map.Entry.<String, Double>comparingByValue().reversed()).limit(5).collect(Collectors.toList());

		// Debug: Print top 5 most similar words and their scores
		/*
		 * System.out.println("\nDEBUG: Top 5 similar words for '" + cleanWord + "':");
		 * similarities.forEach(entry -> System.out.printf("%s: %.4f%n", entry.getKey(),
		 * entry.getValue()));
		 */

		if (similarities.isEmpty()) {
			return originalWord;
		}

		String bestMatch = similarities.get(0).getKey();
		double bestScore = similarities.get(0).getValue();

		if (bestScore < SIMILARITY_THRESHOLD) {
			// System.out.println("DEBUG: Match too low, keep original word");
			return originalWord;
		}
		return Character.isUpperCase(originalWord.charAt(0)) ? capitalize(bestMatch) : bestMatch;
	}

	/**
	 * Capitalizes the first letter of a string.
	 * 
	 * @param str The string to capitalize.
	 * @return The capitalized string.
	 * 
	 * O(1) constant time
	 */
	private String capitalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * Computes the cosine similarity between two vectors.
	 * 
	 * @param vectorA The first vector.
	 * @param vectorB The second vector.
	 * @return The cosine similarity between the vectors.
	 * 
	 * O(n) 
	 */
	private double cosineSimilarity(double[] vectorA, double[] vectorB) {
		double dotProduct = 0.0;
		double normA = 0.0;
		double normB = 0.0;

		for (int i = 0; i < vectorA.length; i++) {
			dotProduct += vectorA[i] * vectorB[i];
			normA += Math.pow(vectorA[i], 2);
			normB += Math.pow(vectorB[i], 2);
		}
		return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}
}