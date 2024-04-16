package ie.atu.sw;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
public class Runner {

	
	private static Scanner scanner = new Scanner(System.in);
	private static Map<String,double[]> wordEmbeddings = new HashMap<>();
	private static String embeddingFilePath ;
	static int option =1;   
	static String fileName;
	// Running Time: O(1) - Constant time complexity. It only calls the menu method.
	public static void main(String[] args) throws Exception {
		
            System.out.print("Enter the path to the word embeddings file: ");
            embeddingFilePath = scanner.nextLine();
            System.out.println("Enter the file name to output");
		    fileName = scanner.nextLine();
            //call the menu class
	        menu();
	}
	
	// Running Time: O(n) - Linear time complexity. It iterates through the menu options until the user chooses to quit.
	private static void menu() throws InterruptedException{
		 	System.out.println(ConsoleColour.WHITE);
			System.out.println("************************************************************");
			System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
			System.out.println("*                                                          *");
			System.out.println("*          Similarity Search with Word Embeddings          *");
			System.out.println("*                                                          *");
			System.out.println("************************************************************");
			System.out.println("(1)Enter a Word or Text");
			System.out.println("(2)Setting for Comparison algorithms");
			System.out.println("(3)Quit");
		
			
			//Output a menu of options and solicit text from the user
			System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
			System.out.print("Select Option [1-3]>");
			int choice = scanner.nextInt();
			scanner.nextLine();
		
			//going to the loop
			while (choice!=3) {
					if(choice ==1){
							specifyEmbeddingFile();
							parseEmbeddingsFile();
							searchMap();
						}
					else if (choice == 2) {
							System.out.println("Enter (1) for Dot Product Algorithms");
							System.out.println("Enter (2) for Euclidean Distance");
							System.out.println("Enter (3) for Cosine Distance");
							option = scanner.nextInt();
			}
					System.out.print(ConsoleColour.YELLOW);	//Change the colour of the console text
					int size = 100;							//The size of the meter. 100 equates to 100%
					for (int i =0 ; i < size ; i++) {		//The loop equates to a sequence of processing steps
						printProgress(i + 1, size); 		//After each (some) steps, update the progress meter
						Thread.sleep(10);					//Slows things down so the animation is visible 
					}
					
				
			
					System.out.println(ConsoleColour.WHITE);
					System.out.println("************************************************************");
					System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
					System.out.println("*                                                          *");
					System.out.println("*          Similarity Search with Word Embeddings          *");
					System.out.println("*                                                          *");
					System.out.println("************************************************************");
					System.out.println("(1)Enter a Word or Text");
					System.out.println("(2)More Option");
					System.out.println("(3)Quit");
	
		
		//Output a menu of options and solicit text from the user
					System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
					System.out.print("Select Option [1-3]>");
					System.out.println();
					choice = scanner.nextInt();
					scanner.nextLine();
			}
		}
	/*
	 *  Terminal Progress Meter
	 *  -----------------------
	 *  You might find the progress meter below useful. The progress effect 
	 *  works best if you call this method from inside a loop and do not call
	 *  System.out.println(....) until the progress meter is finished.
	 *  
	 *  Please note the following carefully:
	 *  
	 *  1) The progress meter will NOT work in the Eclipse console, but will
	 *     work on Windows (DOS), Mac and Linux terminals.
	 *     
	 *  2) The meter works by using the line feed character "\r" to return to
	 *     the start of the current line and writes out the updated progress
	 *     over the existing information. If you output any text between 
	 *     calling this method, i.e. System.out.println(....), then the next
	 *     call to the progress meter will output the status to the next line.
	 *      
	 *  3) If the variable size is greater than the terminal width, a new line
	 *     escape character "\n" will be automatically added and the meter won't
	 *     work properly.  
	 *  
	 * 
	 */
		public static void printProgress(int index, int total) {
		if (index > total) return;	//Out of range
        int size = 50; 				//Must be less than console width
	    char done = '█';			//Change to whatever you like.
	    char todo = '░';			//Change to whatever you like.
	    
	    //Compute basic metrics for the meter
        int complete = (100 * index) / total;
        int completeLen = size * complete / 100;
        
        /*
         * A StringBuilder should be used for string concatenation inside a 
         * loop. However, as the number of loop iterations is small, using
         * the "+" operator may be more efficient as the instructions can
         * be optimized by the compiler. Either way, the performance overhead
         * will be marginal.  
         */
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
        	sb.append((i < completeLen) ? done : todo);
        }
        
        /*
         * The line feed escape character "\r" returns the cursor to the 
         * start of the current line. Calling print(...) overwrites the
         * existing line and creates the illusion of an animation.
         */
        System.out.print("\r" + sb + "] " + complete + "%");
        
        //Once the meter reaches its max, move to a new line.
        if (done == total) System.out.println("\n");
    }
		
		// Running Time: O(1) - Constant time complexity. It only displays the path of the embedding file.
		private static void specifyEmbeddingFile() {
		 System.out.println("Embedding file path set to: " + embeddingFilePath);
	    }

		// Running Time: O(n * m) - Linear time complexity where n is the number of lines in the file and m is the average number of embeddings per line. It reads each line and splits it to parse embeddings.
	 	//Embedding Files
		private static void parseEmbeddingsFile() {
	        try (BufferedReader br = new BufferedReader(new FileReader(embeddingFilePath))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                // Split the line into word and embedding values
	                String[] parts = line.split(",\\s*");

	                // Extract word and parse embedding values
	                String word = parts[0];
	                double[] embeddings = new double[parts.length - 1]; // Assuming embeddings start from index 1

	                // Parse embedding values
	                for (int i = 1; i < parts.length; i++) {
	                    embeddings[i - 1] = Double.parseDouble(parts[i]);
	                }

	                // Store word and its corresponding embedding array in the map
	                wordEmbeddings.put(word, embeddings);
	            }
	            System.out.println("Word embeddings parsed successfully.");
	        } catch (IOException | NumberFormatException e) {
	            System.out.println("Error parsing word embeddings: " + e.getMessage());
	        }
	    }
	
	 	// Running Time: O(n) - Linear time complexity where n is the number of top similar words to print to the file. It writes each similar word to the output file.
	 	//output file function
		private static void outputAllResultsToFile(List<Map.Entry<String, Double>> sortedSimilarities) {
		    // Prompt the user to choose the number of top similar words to print to file
		    System.out.print("Enter the number of top similar words to print to file: ");
		    int topN = scanner.nextInt();
		    scanner.nextLine();

		    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
		        // Write a header indicating the number of top similar words
		        writer.write("Top " + topN + " similar words:\n");

		        // Iterate over the sortedSimilarities list and print to the file
		        for (int i = 0; i < Math.min(topN, sortedSimilarities.size()); i++) {
		            Map.Entry<String, Double> entry = sortedSimilarities.get(i);
		            writer.write(entry.getKey() + " - Similarity: " + entry.getValue() + "\n");
		        }

		        // Print a message indicating the successful write operation
		        System.out.println("Top " + topN + " similar words appended to " + fileName);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}



	 	// Running Time: O(1) - Constant time complexity. It waits for user input.
		//user input word for searching
	 	private static String enterWordOrText() {
	        System.out.println("Enter a Word or Pharse: ");
	        String userInput = scanner.nextLine();
	        System.out.println("You entered: "+userInput);
			return userInput;
	       
	    }
		
		// Running Time: O(n * m * k) - Cubic time complexity where n is the number of words entered by the user, m is the average number of embeddings per word, and k is the number of unique words in the word embeddings map. It searches for similar words for each word entered by the user.
		//search the word in Map
		private static void searchMap() {
		    
		    String userInput = enterWordOrText();
		    //As the user can input a short sentence so I seperate words and put them to an array
		    String[] words = userInput.split("\\s+");
		    Map<String, Double> aggregateSimilarityScores = new HashMap<>();

		    for (String word : words) {
		        //get value for input word
		    	double[] inputEmbedding = wordEmbeddings.get(word);
		        if (inputEmbedding != null) {
		            Map<String, Double> similarityScores = new HashMap<>();

		            for (Map.Entry<String, double[]> entry : wordEmbeddings.entrySet()) {
		                String mapWord = entry.getKey();
		                double[] embedding = entry.getValue();
		                
		                //user option to choose similarity algorithms
		                if (option == 1) {
		                    double similarityScore = calculateDotProduct(inputEmbedding, embedding);
		                    similarityScores.put(mapWord, similarityScore);
		                } else if (option == 2) {
		                    double similarityScore = calculateEuclideanDistance(inputEmbedding, embedding);
		                    similarityScores.put(mapWord, -similarityScore);
		                } else if (option == 3) {
		                    double similarityScore = calculateCosineDistance(inputEmbedding, embedding);
		                    similarityScores.put(mapWord, -similarityScore);
		                }

		            }
		      
		            for (Map.Entry<String, Double> entry : similarityScores.entrySet()) {
		                aggregateSimilarityScores.merge(entry.getKey(), entry.getValue(), Double::sum);
		            }

				    List<Map.Entry<String, Double>> sortedSimilarities = new ArrayList<>(aggregateSimilarityScores.entrySet());
				    sortedSimilarities.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
				    outputTopNResults(sortedSimilarities,1);	   
				    outputAllResultsToFile(sortedSimilarities);
		        } else {
		            System.out.println("Word '" + word + "' not found in the word embeddings map.");
		        }
		        
		    }
		 
		}

		// Running Time: O(1) - Constant time complexity. It only outputs the top similar word.
		//output the result after searching
	    private static void outputTopNResults(List<Entry<String, Double>> sortedList, int n2) {
	        if (!sortedList.isEmpty()) {
	            Entry<String, Double> topEntry = sortedList.get(0);
	            System.out.println("Top similar word: " + topEntry.getKey() + " - Similarity: " + topEntry.getValue());
	        } else {
	            System.out.println("No similar words found.");
	        }
	    }

	    //Running Time: O(d) - Linear time complexity where d is the dimensionality of the embeddings. It iterates through each dimension to compute the dot product.
		//Dot Product Algorithms
	    public static double calculateDotProduct(double[] v1, double[] v2) {
	        if (v1.length != v2.length) {
	            throw new IllegalArgumentException("Vectors must have the same length");
	        }
	        
	        double dotProduct = 0.0;
	        
	        for (int i = 0; i < v1.length; i++) {
	            dotProduct += v1[i] * v2[i];
	        }
	        
	        return dotProduct;
	    }

		// Running Time: O(d) - Linear time complexity where d is the dimensionality of the embeddings. It iterates through each dimension to compute the cosine distance.
		//Cosine Distance Algorithms
		public static double calculateCosineDistance(double[] v1, double[] v2) {
		    if (v1.length != v2.length) {
		        throw new IllegalArgumentException("Vectors must have the same length");
		    }
		    
		    double dotProduct = 0.0;
		    double magnitudeV1 = 0.0;
		    double magnitudeV2 = 0.0;
		    
		    for (int i = 0; i < v1.length; i++) {
		        dotProduct += v1[i] * v2[i];
		        magnitudeV1 += Math.pow(v1[i], 2);
		        magnitudeV2 += Math.pow(v2[i], 2);
		    }
		    
		    magnitudeV1 = Math.sqrt(magnitudeV1);
		    magnitudeV2 = Math.sqrt(magnitudeV2);
		    
		    return 1 - (dotProduct / (magnitudeV1 * magnitudeV2));
		}
		
		// Running Time: O(d) - Linear time complexity where d is the dimensionality of the embeddings. It iterates through each dimension to compute the Euclidean distance.
		//Euclidean Distance Algorithms
		public static double calculateEuclideanDistance(double[] v1, double[] v2) {
		    if (v1.length != v2.length) {
		        throw new IllegalArgumentException("Vectors must have the same length");
		    }
		    
		    double sum = 0.0;
		    
		    for (int i = 0; i < v1.length; i++) {
		        sum += Math.pow(v1[i] - v2[i], 2);
		    }
		    
		    return Math.sqrt(sum);
		}


	   
}
