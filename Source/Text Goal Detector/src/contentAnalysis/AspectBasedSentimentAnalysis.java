package contentAnalysis;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class AspectBasedSentimentAnalysis {


	/**
	 * writes the extracted information into a csv file
	 */
	BufferedWriter resultWriter;
	/**
	 * contains adjectives and their sentiment score
	 */
	SentimentLexicon sentimentLexicon;
	/**
	 * contains the preprocessed reviews: a sentence, its words and its POS-tags
	 */
	PreprocessedReviews reviews;		

	
	
	public AspectBasedSentimentAnalysis() throws IOException{
		reviews = new PreprocessedReviews("data/reviews.txt");
		resultWriter = new BufferedWriter ( new FileWriter (new File("data/output.csv")));
		sentimentLexicon = new SentimentLexicon("data/taboadaAdjectiveSentimentLexicon.txt");
	}

	
	/*
	 *   MAIN 
	 */
	public static void main(String[] args) throws IOException{

		AspectBasedSentimentAnalysis absa = new AspectBasedSentimentAnalysis();
		absa.startExtraction();

	}
	
	/**
	 * iterates over all sentences in the reviews and 
	 * starts the information extraction process for each sentence
	 * @throws IOException
	 */
	public void startExtraction() throws IOException{
		/*
		 * iterate over all sentences
		 */
		for (TaggedSentence sentence : this.reviews.taggedSentences){
			extractInformation(sentence);
		}
		
		this.resultWriter.close();

	}

	
	/*
	 * HERE IS WHERE IT GETS INTERESTING	
	 */
	/**
	 * Extracts information from one sentence, 
	 * the content of the sentence is given as two arrays
	 * @param words -> contains all tokens of the sentence
	 * @param posTags -> contains the pos-tags of the tokens
	 * @throws IOException 
	 */
	private void extractInformation(TaggedSentence s) throws IOException{

		String[] words = s.getWords(); // contains all words of a sentence in their order
		String[] posTags = s.getPosTags(); //contain all POS-tags of the words in their order

		/*
		 * Task 1) 
		 *  have a look at the content of the two arrays:
		 *  print each word and its posTag (word -> posTag)
		 *  by looping over the indexes
		 *  DONE! 
		 */

		for (int index = 0; index < words.length; index ++){
			System.out.println("word: " + words[index] + " POS tag: " + posTags[index]);
		}
		
		/*
		 * Task 2) 
		 * see subtasks within the loop
		 */
		for (int i = 0; i < words.length; i ++){	

			/*
			 * you will properly assign these variables during the following tasks
			 */
			String posTag; // POS-tag for word the current word (word i)  
			int nounFrequency; // frequency of the current word
			String closestAdjective; // closest adjective for the current word
			String sentiment; // sentiment of the current word


			/*
			 * Task 2.0)
			 * assign the variable posTag the part-of-speech tag of the word at position i
			 */
			
			posTag = posTags[i]; 
						
			
			/*
			 * Task 2.1)
			 * select all nouns here!
			 * if the posTag of the word is a noun:
			 * 	- assign the variable noun
			 *  - print it to the command line.
			 */
			
			if(this.isNoun(posTag))
			{
				String noun = words[i];  
				System.out.println("This word is a noun: " + noun);


				/*
				 *  Task 2.2)
				 *  Check how frequent the noun is.
				 *  Assign the variable nounFrequency.
				 *  Print the frequency to the command line. 
				 */

				nounFrequency = this.getNounFrequency(noun);
				System.out.println("Frequency: " + nounFrequency);
		
			
				/*
				 * Task 2.3)
				 * If the noun is frequent enough (above the frequencyThreshold):
				 *  - search for its closest adjective.
				 *  
				 */
				int frequencyThreshold = 5; // we only consider nouns that appear more than 5 times (arbitrarily chosen)

				if(nounFrequency >= frequencyThreshold)
				{
					int searchWindow = 2; // search the adjective within this window (arbitrarily chosen)
					int indexAdjective = this.findIndexOfClosestAdjective(posTags, i, searchWindow);
							
					
				
				
					/*
					 *  Task 2.4)
					 * 	Check if an adjective is found (otherwise, the index is -1).
					 * 	Print the noun and its adjective. 
					 */
					
					if(indexAdjective >= 0) // this.isAdjective(posTags[indexAdjective]))
					{
						closestAdjective = words[indexAdjective];
						System.out.println("Closest Adjective: " + closestAdjective );

				
						/*
						 *  Task 2.5)
						 * 	Derive the polarity of the adjective and assign the variable sentiment. 
						 */
						sentiment = this.getPolarityForAdjective(closestAdjective);
						
						/*
						 *  Task 2.6)
						 * 	Write extracted information into the result file.
						 * 	(for a better interpretation of the results, we also write 
						 *  the original sentence the information was extracted from)  
						 */
						String originalSentence = s.getSentence();
						//resultWriter.newLine();
						//resultWriter.write("Original Sentence: " + originalSentence + ", Noun: " + noun +  ", Sentiment: " + sentiment);
						this.writeExtractedInformationToFile(noun, nounFrequency, closestAdjective, sentiment, originalSentence);
						System.out.println("Original Sentence: " + originalSentence + ", Noun: " + noun +  ", Sentiment: " + sentiment);

						//write...
					}
				}


			}

		}

	}



	
	/*
	 * 	EVERYTHING BELOW THIS LINE IS JUST FOR USING, 
	 *  YOU DO NOT NECESSARILY HAVE TO CARE ABOUT THE IMPLEMENTATION DETAILS 
	 */
	
	/**
	 * returns true if posTag refers to any type of noun, false otherwise
	 * @param posTag
	 * @return
	 */
	private boolean isNoun(String posTag){
		if( posTag.equalsIgnoreCase("NN") || posTag.equalsIgnoreCase("NNP") || posTag.equalsIgnoreCase("NNPS") || posTag.equalsIgnoreCase("NNS")){
			return true;
		}
		return false;
	}

	/**
	 * returns true if posTag refers to any type of noun, false otherwise
	 * @param posTag
	 * @return
	 */
	private boolean isAdjective(String posTag){
		if( posTag.equalsIgnoreCase("JJ") || posTag.equalsIgnoreCase("JJR") || posTag.equalsIgnoreCase("JJS") ){
			return true;
		}
		return false;
	}

	/**
	 * writes the extracted information into a csv file 
	 * @param pair
	 * @throws IOException
	 */
	private void writeExtractedInformationToFile(String noun, int frequency, String adjective, String sentiment, String sentence) throws IOException{
		this.resultWriter.write(noun + "," + frequency + ","  + adjective + ","  + sentiment + "," + sentence.replace(",", "-comma-") +  "\n");
	}

	/**
	 * takes the index of a noun and returns index of closest adjective
	 * returns -1 if no adjective is found
	 * @param index
	 * @return
	 */
	private int findIndexOfClosestAdjective(String[] posTags, int index, int searchWindow){

		// start searching left and right of the word
		int leftNeighbor = index - 1; 
		int rightNeighbor = index + 1;
		int window = 0;
		System.out.println("Index of noun: " + index);

		// continue search in both directions till end of array && in a window of three words 
		while (!(leftNeighbor < 0 && rightNeighbor > posTags.length) && (window <= searchWindow) ){
			try{
				//check right neighbor for adjective
				if (isAdjective(posTags[rightNeighbor])){
					System.out.println("Found right: \t" + rightNeighbor);
					return rightNeighbor; 
				}
				//check left neighbor for adjective
				if (isAdjective(posTags[leftNeighbor])){
					System.out.println("Found left: \t" + leftNeighbor);
					return leftNeighbor; 
				}

			} catch (Exception e){
				//Don't worry, we just ran out of the array on one side
				//e.printStackTrace();

			}
			leftNeighbor --;
			rightNeighbor ++;
			window ++;
		}

		// no adjective found :(
		return -1;
	}

	/**
	 * returns the sentiment score for the adjective (as String!)
	 * @param adjective
	 * @return
	 */
	private String getPolarityForAdjective(String adjective){
		return this.sentimentLexicon.getScore(adjective);
	}

	/**
	 * returns the frequency of the noun within the whole corpus
	 * @param noun
	 */
	private int getNounFrequency(String noun){
		return this.reviews.getFrequencyForNoun(noun);
	}


}

