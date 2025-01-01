package ie.atu.sw;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;
 
public class EmbeddingsParser {

		//The HashMap to store the word and its embeddings
		private Map<String, double[]> embeddingsMap;
		
		public EmbeddingsParser() {
			this.embeddingsMap = new ConcurrentHashMap<>();
		}
		
		/**
		 * Load embeddings method to parse embeddings in and print progress
		 * @param filePath
		 * @throws IOException
		 */
		public void loadEmbeddings(String filePath)throws IOException {
			File file = new File(filePath);
			long totalLines = countLines(file);
			long linesProcessed = 0;
		
			try(BufferedReader br = new BufferedReader(new FileReader(file))){
				String line;
				while((line = br.readLine()) != null) {
				//Parse the line and add to HashMap
				parseLineAndAddToMap(line);
				linesProcessed++;
				if(linesProcessed % 100 == 0  || linesProcessed == totalLines) {
				    ProgressReporter.printProgress((int) linesProcessed, (int) totalLines);
				}
			}
		}
		ProgressReporter.printProgress(100, 100);
		System.out.println();//move to a newline after finishing progress update
	}

	/**
	 * Parsing the line and adding to map
	 * @param line
	 * O(n) parses over and reads in n number of lines
	 */
	private void parseLineAndAddToMap(String line) {
		//Split line into parts
		String[] parts = line.split(",\\s+");
		
		//The first part is the word
		String word = parts[0];
		
		//The rest is the embedding values
		double[] values = new double[parts.length -1];
		for(int i = 1; i<parts.length; i++) {
			values[i-1]=Double.parseDouble(parts[i].trim());
		}
		embeddingsMap.put(word.toLowerCase(), values);//to ignore the case entered
	}
	
	private long countLines(File file) throws IOException{
		try(var lineIterator = new LineNumberReader(new FileReader(file))){
			lineIterator.skip(Long.MAX_VALUE);
			return lineIterator.getLineNumber()+1;//because line nos start at 0
		}
	}
	
	/**
	 * Get embedding
	 * @param word
	 * @return word from the HashMap
	 * O(1) retrieving from a HashMap is constant time
	 */
	public double[] getEmbedding(String word) {
	return embeddingsMap.get(word.toLowerCase());//ignore case
	}
	
	/**
	 * Dot Product
	 * @param word1
	 * @param word2
	 * @return sum
	 * O(n) reads in n number of lines - 1 loop
	 */
	public double dotProduct(String word1, String word2) {
	    double[] embeddings1 = embeddingsMap.get(word1);
	    double[] embeddings2 = embeddingsMap.get(word2);
	    if (embeddings1 == null || embeddings2 == null) {
	        return Double.NaN; // Indicate that at least one word was not found
	    }
	    double sum = 0.0;
	    for (int i = 0; i < embeddings1.length; i++) {
	        sum += embeddings1[i] * embeddings2[i];
	    }
	    return sum;
	}
	
    /**
     * Provide access to the embeddings map
     * @return embeddingsMap
     */
    public Map<String, double[]> getEmbeddingsMap() {
        return embeddingsMap;
    }
    
    /**
     * Euclidean Distance
     * @param word1
     * @param word2
     * @return Math.sqrt(sum)
     * O(n) reads in n number of lines - 1 loop
     */
	public double euclideanDistance(String word1, String word2) {
	    double[] embeddings1 = embeddingsMap.get(word1);
	    double[] embeddings2 = embeddingsMap.get(word2);
	    if (embeddings1 == null || embeddings2 == null) {
	        return Double.NaN; // Indicate that at least one word was not found
	    }
	    double sum = 0.0;
	    for (int i = 0; i < embeddings1.length; i++) {
	        sum += Math.pow(embeddings1[i] - embeddings2[i], 2);
	    }
	    return Math.sqrt(sum);
	}
	
	
	//check to see if the words are being loaded
	public void printRandomSample(int sampleSize) {
	    List<String> keys = new ArrayList<>(embeddingsMap.keySet());
	    Collections.shuffle(keys);
	    for (int i = 0; i < Math.min(sampleSize, keys.size()); i++) {
	        String key = keys.get(i);
	        double[] embedding = embeddingsMap.get(key);
	        System.out.print("Word: " + key + " => [");
	        for (int j = 0; j < embedding.length; j++) {
	            System.out.print(embedding[j] + (j < embedding.length - 1 ? ", " : ""));
	        }
	        System.out.println("]");
	    }
	}
}